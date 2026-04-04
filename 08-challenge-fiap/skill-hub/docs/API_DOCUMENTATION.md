# 📚 Documentação da API - Skill Hub

## 🎯 Sobre a Documentação

A documentação da API Skill Hub é gerada automaticamente usando **Swagger/OpenAPI** com **SpringDoc**. A interface está em contratos (`UserApi`, `SkillApi`, `GroupApi`, `LoginApi`) que os respectivos controllers implementam.

---

## 📖 Estrutura de Contratos e Implementações

### 1. **UserApi.java** (Interface de Contrato)
- Localização: `src/main/java/br/com/fiap/skill_hub/api/UserApi.java`
- Endpoints:
  - `POST /api/users` — Criar novo usuário *(público)*
  - `GET /api/users` — Listar todos os usuários *(autenticado)*
  - `PUT /api/users/{idUser}/skills` — Associar skills ao usuário *(autenticado)*
  - `GET /api/users/{idUser}/skills` — Listar IDs de skills do usuário *(autenticado)*

### 2. **SkillApi.java** (Interface de Contrato)
- Localização: `src/main/java/br/com/fiap/skill_hub/api/SkillApi.java`
- Endpoints:
  - `POST /api/skills` — Criar skill *(autenticado)*
  - `GET /api/skills` — Listar todas as skills *(autenticado)*
  - `GET /api/skills/{id}` — Buscar skill por ID *(autenticado)*
  - `PUT /api/skills/{id}` — Atualizar skill *(autenticado)*
  - `DELETE /api/skills/{id}` — Excluir skill *(autenticado)*

### 3. **GroupApi.java** (Interface de Contrato)
- Localização: `src/main/java/br/com/fiap/skill_hub/api/GroupApi.java`
- Endpoints:
  - `POST /api/groups` — Criar grupo *(autenticado)*
  - `GET /api/groups` — Listar grupos *(autenticado)*
  - `GET /api/groups/{id}` — Buscar grupo por ID *(autenticado)*
  - `PUT /api/groups/{id}` — Atualizar grupo *(autenticado)*
  - `DELETE /api/groups/{id}` — Excluir grupo *(autenticado)*
  - `POST /api/groups/{groupId}/join/{userId}` — Entrar no grupo *(autenticado)*

### 4. **LoginApi.java** (Interface de Contrato)
- Localização: `src/main/java/br/com/fiap/skill_hub/api/LoginApi.java`
- Endpoints:
  - `POST /api/login` — Autenticar usuário e obter tokens JWT *(público)*
  - `POST /api/login/refresh` — Renovar access token e refresh token *(público)*
  - `POST /api/login/logout` — Revogar refresh token *(público)*

### 5. **Controllers** (Implementações)
- `UserController` → implementa `UserApi` — `@RequestMapping("/api/users")`
- `SkillController` → implementa `SkillApi` — `@RequestMapping("/api/skills")`
- `GroupController` → implementa `GroupApi` — `@RequestMapping("/api/groups")`
- `LoginController` → implementa `LoginApi` — `@RequestMapping("/api/login")`

### 6. **OpenApiConfig.java** (Configuração)
- Localização: `src/main/java/br/com/fiap/skill_hub/config/OpenApiConfig.java`
- Configura título, versão, contato e esquema de segurança JWT

---

## 🚀 Como Acessar a Documentação

### 1. Iniciar a Aplicação

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

> **Atenção:** a aplicação requer a variável de ambiente `JWT_SECRET` para iniciar.
> ```bash
> export JWT_SECRET=minha-chave-super-secreta-de-pelo-menos-32-caracteres
> ```

### 2. Acessar a Interface Swagger UI

```
http://localhost:8080/swagger-ui.html
```

### 3. Acessar o JSON OpenAPI (máquina)

```
http://localhost:8080/v3/api-docs
```

---

## 🔐 Autenticação JWT

A API usa **JWT Bearer Token** para autenticação. Rotas públicas:
- `POST /api/login`
- `POST /api/login/refresh`
- `POST /api/login/logout`
- `POST /api/users`
- `GET /swagger-ui/**`, `GET /v3/api-docs/**`
- `GET /actuator/health`
- `GET /h2-console/**`

Todas as demais rotas exigem o header:
```
Authorization: Bearer <access_token>
```

O sistema usa **dois tokens**: `accessToken` (expiração padrão: 15 min) e `refreshToken` (expiração padrão: 7 dias), ambos persistidos e revogados via banco de dados.

---

## 📝 Exemplos de Uso

### Autenticar (POST /api/login)

**Request Body:**
```json
{
  "email": "joao.silva@example.com",
  "password": "senha123a"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900000
}
```

---

### Renovar Tokens (POST /api/login/refresh)

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Response (200 OK):** novo par de tokens (`accessToken` + `refreshToken`).

---

### Logout (POST /api/login/logout)

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Response:** `204 No Content`

---

### Criar Usuário (POST /api/users) — público

**Request Body:**
```json
{
  "name": "João Silva",
  "email": "joao.silva@example.com",
  "password": "senha123a",
  "profile": "STUDENT"
}
```

> `profile` aceita: `STUDENT` ou `ADMINISTRATOR`  
> `password` deve ter mínimo 8 e máximo 64 caracteres, ao menos 1 letra e 1 número.  
> `password` não é retornado nas respostas (`WRITE_ONLY`).

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao.silva@example.com",
  "profile": "STUDENT"
}
```

---

### Associar Skills ao Usuário (PUT /api/users/{idUser}/skills)

**Headers:** `Authorization: Bearer <access_token>`

**Request Body:** lista de IDs de skills
```json
[1, 2, 3]
```

**Response (200 OK):** `UserDto` com as skills atualizadas.

---

### Criar Skill (POST /api/skills)

**Headers:** `Authorization: Bearer <access_token>`

**Request Body:**
```json
{
  "description": "Java"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "description": "Java"
}
```

---

### Criar Grupo (POST /api/groups)

**Headers:** `Authorization: Bearer <access_token>`

**Request Body:**
```json
{
  "description": "Grupo de Java Avançado",
  "maxMembers": 10,
  "ownerId": 1,
  "skills": [
    { "id": 1, "description": "Java" }
  ]
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "description": "Grupo de Java Avançado",
  "maxMembers": 10,
  "ownerId": 1,
  "users": [],
  "skills": [{ "id": 1, "description": "Java" }]
}
```

---

### Entrar no Grupo (POST /api/groups/{groupId}/join/{userId})

**Headers:** `Authorization: Bearer <access_token>`

O usuário só consegue entrar se:
1. Possuir **todas** as skills exigidas pelo grupo (`USER_LACKS_REQUIRED_SKILLS` → 400)
2. Existirem vagas disponíveis (`GROUP_NO_AVAILABLE_SEATS` → 400)
3. Não for já membro (`USER_ALREADY_GROUP_MEMBER` → 400)

**Response (200 OK):** `GroupDto` atualizado com o novo membro.

---

## ❌ Tratamento de Erros

Todas as respostas de erro seguem o padrão `ErrorResponseDto`:

```json
{
  "status": 404,
  "code": "USER_NOT_FOUND",
  "message": "Usuário não encontrado",
  "details": null
}
```

| Código HTTP | `code` | Situação |
|-------------|--------|----------|
| 400 | `VALIDATION_ERROR` | Falha de validação de campos (detalhes em `details`) |
| 400 | `USER_ALREADY_GROUP_MEMBER` | Usuário já é membro do grupo |
| 400 | `GROUP_NO_AVAILABLE_SEATS` | Grupo sem vagas disponíveis |
| 400 | `USER_LACKS_REQUIRED_SKILLS` | Usuário sem as skills exigidas pelo grupo |
| 401 | `USER_NOT_FOUND` | Usuário não encontrado durante autenticação |
| 401 | `INVALID_CREDENTIALS` | Credenciais inválidas |
| 401 | `INVALID_REFRESH_TOKEN` | Refresh token inválido ou expirado |
| 401 | `REFRESH_TOKEN_NOT_FOUND` | Refresh token não encontrado |
| 401 | `REFRESH_TOKEN_MISMATCH` | Refresh token não pertence ao usuário |
| 404 | `USER_NOT_FOUND` | Usuário não encontrado |
| 404 | `SKILL_NOT_FOUND` | Skill não encontrada |
| 404 | `GROUP_NOT_FOUND` | Grupo não encontrado |
| 404 | `GROUP_OWNER_NOT_FOUND` | Owner do grupo não encontrado |
| 409 | `EMAIL_ALREADY_REGISTERED` | E-mail já cadastrado |
| 409 | `SKILL_ALREADY_REGISTERED` | Skill já cadastrada |
| 409 | `SKILL_DESCRIPTION_ALREADY_IN_USE` | Descrição de skill já em uso |
| 500 | `TOKEN_HASH_GENERATION_ERROR` | Erro ao gerar hash do token |
| 500 | `INTERNAL_SERVER_ERROR` | Erro interno inesperado |

---

## 🔍 Anotações Swagger Utilizadas

### Em cada `*Api.java`:

1. **@Tag** — Agrupa endpoints por domínio (`Users`, `Skills`, `Groups`, `Auth`)
2. **@Operation** — Descreve a operação (summary + description)
3. **@ApiResponses / @ApiResponse** — Define possíveis respostas HTTP
4. **@Schema** — Documenta estrutura dos DTOs (automático via SpringDoc)

---

## 🎨 Mapa de Endpoints

```
api/
├── UserApi      @Tag("Users")
│   ├── POST   /api/users                         (público)
│   ├── GET    /api/users                         (autenticado)
│   ├── PUT    /api/users/{idUser}/skills         (autenticado)
│   └── GET    /api/users/{idUser}/skills         (autenticado)
│
├── SkillApi     @Tag("Skills")
│   ├── POST   /api/skills                        (autenticado)
│   ├── GET    /api/skills                        (autenticado)
│   ├── GET    /api/skills/{id}                   (autenticado)
│   ├── PUT    /api/skills/{id}                   (autenticado)
│   └── DELETE /api/skills/{id}                   (autenticado)
│
├── GroupApi     @Tag("Groups")
│   ├── POST   /api/groups                        (autenticado)
│   ├── GET    /api/groups                        (autenticado)
│   ├── GET    /api/groups/{id}                   (autenticado)
│   ├── PUT    /api/groups/{id}                   (autenticado)
│   ├── DELETE /api/groups/{id}                   (autenticado)
│   └── POST   /api/groups/{groupId}/join/{userId}(autenticado)
│
└── LoginApi     @Tag("Auth")
    ├── POST   /api/login                         (público)
    ├── POST   /api/login/refresh                 (público)
    └── POST   /api/login/logout                  (público)
```

---

## ✅ Checklist de Implementação

- ✅ Interface `UserApi.java` com anotações Swagger
- ✅ Interface `SkillApi.java` com anotações Swagger
- ✅ Interface `GroupApi.java` com anotações Swagger
- ✅ Interface `LoginApi.java` com anotações Swagger
- ✅ Controllers implementam as respectivas interfaces
- ✅ `OpenApiConfig.java` configurado
- ✅ `GlobalExceptionHandler` com todos os erros de negócio mapeados
- ✅ Autenticação JWT com access token + refresh token
- ✅ `TraceIdFilter` injeta `traceId` por requisição via MDC e header
- ✅ `JwtAuthenticationFilter` valida Bearer token em cada requisição
- ✅ Dependência `springdoc-openapi-starter-webmvc-ui:2.0.2` no `pom.xml`
- ✅ Documentação acessível em `/swagger-ui.html`

---

## 🔗 Links Úteis

- [SpringDoc OpenAPI](https://springdoc.org/)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)
- [OpenAPI Specification](https://spec.openapis.org/)
- [Spring Boot Security](https://docs.spring.io/spring-security/reference/)
- [JJWT Library](https://github.com/jwtk/jjwt)

---

**Última atualização:** 04 de Abril de 2026
