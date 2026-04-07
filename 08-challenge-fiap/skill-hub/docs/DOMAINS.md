# DOMAINS - Skill Hub

## Objetivo
Este documento descreve os dominios de negocio da API `skill-hub`, o contexto funcional do produto, uma jornada simples de uso e o modelo de dados (atual e alvo) para suportar os casos de uso.

## Contexto de negocio
O Skill Hub existe para conectar pessoas por competencias tecnicas e facilitar formacao de grupos mais equilibrados para projetos.

Problema que resolvemos:
- grupos montados sem criterio tecnico
- sobreposicao de habilidades no mesmo grupo
- falta de habilidades essenciais para entrega

Resultado esperado de negocio:
- cadastro de usuarios e autenticacao segura
- catalogo de skills
- associacao de skills aos usuarios
- criacao de grupos por skills necessarias
- entrada de membros condicionada a skill/perfil esperado
- o usuario que cria o grupo tem perfil de `OWNER` e pode gerenciar o grupo (adicionar/remover membros, editar descricao, etc)'

## Dominios da API

## 1) Dominio de Usuario (`User`)
Responsabilidade:
- manter dados do usuario
- definir perfil (`ADMINISTRATOR` ou `STUDENT`)

Estado no projeto:
- implementado em `UserController`, `UserService`, `UserRepository`, `UserEntity`
- endpoints existentes:
  - `POST /api/users`
  - `GET /api/users`

Campos principais:
- `id`
- `name`
- `email` (unico no banco)
- `password` (entrada no request; nao deve sair no response)
- `profile`

## 2) Dominio de Autenticacao (`Auth`)
Responsabilidade:
- login
- emissao de access token e refresh token
- refresh com rotacao
- logout com revogacao de refresh token

Estado no projeto:
- implementado em `LoginController`, `LoginService`, `JWTService`, `SecurityConfig`, `JwtAuthenticationFilter`
- endpoints existentes:
  - `POST /api/login`
  - `POST /api/login/refresh`
  - `POST /api/login/logout`

Conceitos principais:
- `access token`: usado no header `Authorization: Bearer ...`
- `refresh token`: persistido por hash para rotacao/revogacao

## 3) Dominio de Token Persistido (`RefreshToken`)
Responsabilidade:
- rastrear refresh token ativo
- permitir revogacao e controle de reuso

Estado no projeto:
- implementado em `RefreshTokenEntity`, `RefreshTokenRepository`

Campos principais:
- `user_id`
- `token_hash`
- `jti`
- `expires_at`
- `revoked`

## 4) Dominio de Skill (`Skill`) - alvo funcional
Responsabilidade:
- cadastrar e consultar habilidades tecnicas
- permitir associacao de skills aos usuarios

Estado no projeto:
- estrutura inicial existente (`SkillController`, `SkillDto`, `SkillService`)
- endpoints de negocio ainda nao implementados

## 5) Dominio de Grupo (`Group`) - alvo funcional
Responsabilidade:
- criar grupos de projeto
- definir skills necessarias
- gerenciar membros com regra de entrada por skill

Estado no projeto:
- estrutura inicial existente (`GroupController`, `GroupDto`)
- endpoints de negocio ainda nao implementados

## Jornada funcional simples (end-to-end)
Abaixo esta a jornada desejada de negocio, combinando o que ja existe e o que esta planejado.

### Passo 1: Criar usuario
1. Cliente chama `POST /api/users` com nome, email, senha e perfil.
2. API valida e persiste usuario.
3. Usuario passa a existir com identificador unico.

### Passo 2: Autenticar
1. Cliente chama `POST /api/login` com email e senha.
2. API autentica e retorna `accessToken` + `refreshToken`.
3. Cliente usa `accessToken` nas chamadas protegidas.

### Passo 3: Criar habilidades (planejado)
1. Admin (ou perfil autorizado) cria skills, por exemplo: Java, Spring Boot, SQL, UX.
2. Skills ficam disponiveis para associacao com usuarios e grupos.

### Passo 4: Associar habilidades ao usuario (planejado)
1. Usuario seleciona skills que domina.
2. Sistema salva relacao N:N entre usuario e skill.
3. Perfil tecnico do usuario fica consultavel.

### Passo 5: Criar grupo com habilidades necessarias (planejado)
1. Criador do grupo define descricao, tamanho do grupo e lista de skills obrigatorias.
2. Sistema salva grupo e suas skills requeridas.
3. Definir usuario criou o grupo como `OWNER`.
4. Gerenciar vagas disponiveis com base no tamanho definido.
5. Demais usuarios que entrar no grupo sao `MEMBER` e tem acesso limitado a gestao do grupo.

### Passo 6: Entrada condicionada por skill (planejado)
1. Usuario tenta entrar no grupo.
2. Regra de negocio verifica se o usuario possui ao menos as skills minimas exigidas.
3. Se atender ao criterio, entrada aprovada; caso contrario, rejeitada com motivo.

## Regras de negocio recomendadas
- Email de usuario deve ser unico.
- Senha sempre armazenada com hash forte (BCrypt).
- Refresh token salvo por hash, nunca em texto puro.
- Reuso de refresh token revogado deve ser bloqueado.
- Entrada em grupo deve validar skill minima exigida.
- Perfil `ADMINISTRATOR` pode gerenciar catalogo de skills e politicas de grupo.

## Status de implementacao (resumo)
- Implementado:
  - Usuario (cadastro/listagem)
  - Auth JWT (login, refresh, logout)
  - Persistencia de refresh token
- Em evolucao/planejado:
  - CRUD de skills
  - Associacao usuario-skill
  - CRUD de grupos
  - Entrada condicionada por skill

