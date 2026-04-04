# Security - JWT no Skill Hub

## Objetivo
Este documento descreve a estrategia de autenticacao/autorizacao com JWT no projeto `skill-hub`, incluindo:
- emissao de `access token` e `refresh token`
- validacao do header `Authorization`
- rotacao e revogacao de refresh token
- configuracoes no `application.yml`
- passos de teste (curl e Swagger)

## Visao geral da estrategia
- A API usa **Spring Security stateless** (sem sessao HTTP).
- O login gera um par de tokens:
  - **Access token (JWT)**: curto, usado no header `Authorization: Bearer ...`.
  - **Refresh token (JWT)**: usado para obter novo par de tokens em `/api/login/refresh`.
- O refresh token e persistido no banco em `refresh_token` com:
  - `jti` (id do token)
  - `token_hash` (SHA-256 do token)
  - `expires_at`
  - `revoked`
- A cada novo login, refresh tokens ativos do usuario sao revogados.
- A cada refresh, o token antigo e revogado e um novo token e emitido (rotacao).

## Componentes principais

### 1) Endpoints de autenticacao
Arquivo: `src/main/java/br/com/fiap/skill_hub/controller/LoginController.java`

- `POST /api/login`
  - Request: `LoginDto`
  - Response: `TokenResponseDto`
- `POST /api/login/refresh`
  - Request: `RefreshTokenRequestDto`
  - Response: `TokenResponseDto`
- `POST /api/login/logout`
  - Request: `RefreshTokenRequestDto`
  - Response: `204 No Content`

### 2) Regras de negocio de autenticacao
Arquivo: `src/main/java/br/com/fiap/skill_hub/service/LoginService.java`

- `login(...)`
  - autentica credenciais com `AuthenticationManager`
  - busca usuario por email
  - revoga refresh tokens ativos
  - gera `accessToken` + `refreshToken`
  - persiste refresh token (hash + jti + expiracao)
- `refresh(...)`
  - extrai `email` e `jti` do refresh token
  - valida assinatura, tipo `REFRESH` e expiracao
  - compara `hash(token recebido)` com `token_hash` do banco
  - revoga token antigo
  - gera novo par de tokens
- `logout(...)`
  - marca refresh token como revogado pelo `jti`

### 3) Geracao e validacao JWT
Arquivo: `src/main/java/br/com/fiap/skill_hub/service/JWTService.java`

Claims utilizadas:
- `sub`: email do usuario
- `iss`: issuer configurado
- `exp`: data de expiracao
- `type`: `ACCESS` ou `REFRESH`
- `jti`: somente no refresh token
- `profile`: somente no access token

Configuracoes consumidas:
- `security.jwt.secret`
- `security.jwt.issuer`
- `security.jwt.access-token-expiration-ms`
- `security.jwt.refresh-token-expiration-ms`

### 4) Filtro do header Authorization
Arquivo: `src/main/java/br/com/fiap/skill_hub/security/JwtAuthenticationFilter.java`

Fluxo do filtro:
1. Le header `Authorization`.
2. Se nao comecar com `Bearer `, segue sem autenticar.
3. Se token nao for tipo `ACCESS`, ignora autenticacao.
4. Extrai email e busca usuario.
5. Valida token (`assinatura`, `expiracao`, `sub`, `type`).
6. Preenche `SecurityContext` com `ROLE_<PROFILE>`.

### 5) Configuracao do Spring Security
Arquivo: `src/main/java/br/com/fiap/skill_hub/config/SecurityConfig.java`

- CSRF desabilitado.
- `SessionCreationPolicy.STATELESS`.
- Rotas liberadas:
  - `/api/login/**`
  - `/v3/api-docs/**`
  - `/swagger-ui/**`
  - `/swagger-ui.html`
  - `/actuator/health`
  - `/h2-console/**`
  - `POST /api/users`
- Demais rotas exigem autenticacao.

## DTOs de autenticacao

### `RefreshTokenRequestDto`
Arquivo: `src/main/java/br/com/fiap/skill_hub/controller/dto/RefreshTokenRequestDto.java`

Campos:
- `refreshToken` (`@NotBlank`)

### `TokenResponseDto`
Arquivo: `src/main/java/br/com/fiap/skill_hub/controller/dto/TokenResponseDto.java`

Campos:
- `accessToken` (`@NotBlank`)
- `refreshToken` (`@NotBlank`)
- `tokenType` (`@NotBlank`)
- `expiresIn` (`@NotNull`, `@Positive`)

## Persistencia de refresh token

### Entidade
Arquivo: `src/main/java/br/com/fiap/skill_hub/repository/entities/RefreshTokenEntity.java`

Colunas principais:
- `user_id`
- `token_hash` (unique)
- `jti` (unique)
- `expires_at`
- `revoked`

### Repositorio
Arquivo: `src/main/java/br/com/fiap/skill_hub/repository/RefreshTokenRepository.java`

Consultas usadas:
- `findByJtiAndRevokedFalse(...)`
- `findAllByUserIdAndRevokedFalse(...)`

### Migration
Arquivo: `src/main/resources/db/migration/V2__create_refresh_token_and_unique_email.sql`

- cria tabela `refresh_token`
- cria unique em `user_entity.email`

## Configuracao (`application.yml`)
Arquivo: `src/main/resources/application.yml`

```yaml
security:
  jwt:
    issuer: skill-hub-api
    secret: ${JWT_SECRET}
    access-token-expiration-ms: ${JWT_ACCESS_EXPIRATION_MS:900000}
    refresh-token-expiration-ms: ${JWT_REFRESH_EXPIRATION_MS:604800000}
```

Variaveis esperadas:
- `JWT_SECRET` (obrigatoria)
- `JWT_ACCESS_EXPIRATION_MS` (opcional, default 900000)
- `JWT_REFRESH_EXPIRATION_MS` (opcional, default 604800000)

Gerar segredo (Linux/macOS):
```bash
openssl rand -base64 64
```

## Fluxo completo (resumo)
1. Usuario envia email/senha em `POST /api/login`.
2. API autentica, gera tokens e salva refresh token hash no banco.
3. Cliente chama endpoints protegidos com `Authorization: Bearer <accessToken>`.
4. Quando access token expira, cliente chama `POST /api/login/refresh` com refresh token.
5. API valida e rotaciona refresh token, retornando novo par.
6. No logout, refresh token e revogado em `POST /api/login/logout`.

## Como testar

### 1) Subir a aplicacao
```bash
cd /home/luis-garcia/workspace/github/preply/vinicius/plano-de-aula/08-challenge-fiap/skill-hub
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### 2) Criar usuario
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Joao",
    "email": "joao@example.com",
    "password": "123456",
    "profile": "STUDENT"
  }'
```

### 3) Login (gerar tokens)
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "123456"
  }'
```

### 4) Consumir endpoint protegido
```bash
curl http://localhost:8080/api/users \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 5) Refresh token
```bash
curl -X POST http://localhost:8080/api/login/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "<REFRESH_TOKEN>"
  }'
```

### 6) Logout
```bash
curl -X POST http://localhost:8080/api/login/logout \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "<REFRESH_TOKEN>"
  }'
```

## Checklist de validacao
- [ ] Login com credenciais validas retorna `accessToken` e `refreshToken`.
- [ ] Endpoint protegido sem token retorna `401`.
- [ ] Endpoint protegido com token valido retorna `200`.
- [ ] Refresh token valido retorna novo par de tokens.
- [ ] Reuso de refresh token antigo (apos rotacao) falha.
- [ ] Logout invalida refresh token para novas rotacoes.

## Observacoes importantes
- O `accessToken` nao e persistido no banco (modelo stateless).
- Apenas o refresh token e controlado no banco (hash + revogacao).
- A expiracao persistida do refresh token usa o `exp` do JWT gerado, portanto segue o valor configurado em `security.jwt.refresh-token-expiration-ms`.

