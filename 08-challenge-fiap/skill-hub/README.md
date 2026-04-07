# 🎓 Skill Hub - Plataforma de Gerenciamento de Habilidades

> Aplicação para criar grupos baseado na skill do usuário

[![Java](https://img.shields.io/badge/Java-21-orange?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-green?logo=spring)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apache-maven)](https://maven.apache.org/)

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Pré-requisitos](#-pré-requisitos)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Build da Aplicação](#-build-da-aplicação)
- [Migrações com Flyway](#-migrações-com-flyway)
- [Executar com Profile Dev](#-executar-com-profile-dev)
- [Health Check](#-health-check)
- [Swagger - Documentação da API](#-swagger---documentação-da-api)
- [Autenticação JWT](#-autenticação-jwt)
- [Documentação Técnica](#-documentação-técnica)
- [Configuração de Profiles](#-configuração-de-profiles)

---

## 🎯 Sobre o Projeto

**Skill Hub** é uma aplicação Spring Boot desenvolvida para o desafio Oracle do programa FIAP 2TDS.

**Funcionalidades:**
- ✅ Gerenciar usuários com perfis `STUDENT` e `ADMINISTRATOR`
- ✅ Catálogo de habilidades (skills) com CRUD completo
- ✅ Associar skills a usuários
- ✅ Criação e gerenciamento de grupos com skills obrigatórias
- ✅ Entrada em grupos com validação de skills mínimas e controle de vagas
- ✅ Autenticação com JWT (access token + refresh token com rotação)
- ✅ Tratamento global de exceções com códigos de erro padronizados
- ✅ Rastreabilidade por requisição via `X-Trace-Id` (MDC)
- ✅ API RESTful documentada com Swagger/OpenAPI
- ✅ Múltiplos ambientes (Dev, Prod, UAT)

---

## 📦 Pré-requisitos

- **Java 21+** - [Download](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven 3.9+** - [Download](https://maven.apache.org/download.cgi)
- **Git** - [Download](https://git-scm.com/downloads)

### Verificar Instalação

```bash
java -version
mvn -v
git --version
```

---

## 🔑 Variáveis de Ambiente

| Variável | Obrigatória | Padrão | Descrição |
|----------|-------------|--------|-----------|
| `JWT_SECRET` | ✅ Sim | — | Chave secreta para assinar os JWTs (mínimo 32 caracteres) |
| `JWT_ACCESS_EXPIRATION_MS` | ❌ Não | `900000` (15 min) | Expiração do access token em milissegundos |
| `JWT_REFRESH_EXPIRATION_MS` | ❌ Não | `604800000` (7 dias) | Expiração do refresh token em milissegundos |
| `PORT` | ❌ Não | `8080` | Porta do servidor |
| `DB_HOST` | Prod only | `localhost` | Host do MySQL (produção) |
| `DB_PORT` | Prod only | `3306` | Porta do MySQL (produção) |
| `DB_NAME` | Prod only | `skillhubdb` | Nome do banco de dados (produção) |
| `DB_USER` | Prod only | — | Usuário do MySQL (produção) |
| `DB_PASSWORD` | Prod only | — | Senha do MySQL (produção) |

### Configurar para desenvolvimento

```bash
export JWT_SECRET=minha-chave-super-secreta-de-pelo-menos-32-caracteres
```

---

## 📥 Instalação

### 1. Clone o Repositório

```bash
git clone <seu-repositorio-url>
cd skill-hub
```

### 2. Instale as Dependências

```bash
./mvnw clean install -DskipTests
```

---

## 🔨 Build da Aplicação

### Build Completo (Com Testes)

```bash
export JWT_SECRET=minha-chave-super-secreta-de-pelo-menos-32-caracteres
./mvnw clean package
```

### Build Sem Testes

```bash
./mvnw clean package -DskipTests
```

### Apenas Compilar

```bash
./mvnw clean compile
```

---

## 🛫 Migrações com Flyway

O projeto usa Flyway para versionar e aplicar schema no startup. O Hibernate fica apenas com validação (`ddl-auto: validate`).


### Fluxo para novas mudanças de banco

1. Crie um novo arquivo em `src/main/resources/db/migration`.
2. Nomeie seguindo a próxima versão, ex.: `V4__descricao.sql`.
3. Suba a aplicação; o Flyway executa a migração pendente automaticamente.

---

## 🚀 Executar com Profile Dev

### Opção 1: Maven (Recomendado)

```bash
export JWT_SECRET=minha-chave-super-secreta-de-pelo-menos-32-caracteres
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Opção 2: Variável de Ambiente

**Linux/Mac:**
```bash
export SPRING_PROFILES_ACTIVE=dev
export JWT_SECRET=minha-chave-super-secreta-de-pelo-menos-32-caracteres
./mvnw spring-boot:run
```

### Opção 3: JAR Executável

```bash
./mvnw clean package -DskipTests
java -Dspring.profiles.active=dev \
     -DJWT_SECRET=minha-chave-super-secreta-de-pelo-menos-32-caracteres \
     -jar target/skill-hub-0.0.1-SNAPSHOT.jar
```

---

## ✅ Mensagem de Sucesso

Quando a aplicação inicia com sucesso:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___| '_ | '_| '/ _` | \ \ \ \
 \\/  ___)_)  '_| | | (_ ) ) ) ) )
  '  ____ .___ __ _\__,  / / / /
 =========_==============___/=/_/_/_/

INFO : Started SkillHubApplication
```

---

## ❤️ Health Check

```
http://localhost:8080/actuator/health
```

**Resposta esperada (HTTP 200):**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "H2"
      }
    }
  }
}
```

### Outros Endpoints Actuator (dev)

```
http://localhost:8080/actuator/info
http://localhost:8080/actuator/metrics
```

---

## 📚 Swagger - Documentação da API

### Acessar a Interface Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

### Acessar JSON OpenAPI

```
http://localhost:8080/v3/api-docs
```

### Domínios documentados

| Tag | Base URL | Descrição |
|-----|----------|-----------|
| `Auth` | `/api/login` | Login, refresh token e logout |
| `Users` | `/api/users` | CRUD de usuários e associação de skills |
| `Skills` | `/api/skills` | CRUD de skills |
| `Groups` | `/api/groups` | CRUD de grupos e entrada por skill |

---

## 🔐 Autenticação JWT

### Fluxo de autenticação

```
1. POST /api/login           → retorna accessToken + refreshToken
2. Usar accessToken          → header "Authorization: Bearer <token>"
3. POST /api/login/refresh   → renova o par de tokens (rotação)
4. POST /api/login/logout    → revoga o refreshToken
```

### Criar usuário e autenticar

```bash
# 1. Criar usuário (público)
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "senha123a",
    "profile": "STUDENT"
  }'

# 2. Autenticar
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "senha123a"
  }'

# 3. Usar token nas requisições protegidas
curl http://localhost:8080/api/users \
  -H "Authorization: Bearer <access_token>"
```

---

## 📖 Documentação Técnica

A pasta `docs/` contém toda a documentação técnica do projeto:

```
docs/
├── API_DOCUMENTATION.md    # 📄 Documentação completa da API
├── DOMAINS.md              # 🗂️ Descrição dos domínios da aplicação
├── ERROR_HANDLING.md       # ❌ Guia de tratamento de erros
├── SECURITY.md             # 🔐 Documentação de segurança JWT
├── checklist.md            # ✅ Checklist do projeto
└── fluxodesistema.drawio.png # 🎨 Diagrama do fluxo do sistema
```

---

## ⚙️ Configuração de Profiles

### Development (dev) ✨

```bash
export JWT_SECRET=minha-chave-super-secreta-de-pelo-menos-32-caracteres
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

**Características:**
- 🗄️ Banco H2 em memória (`jdbc:h2:mem:skillhubdb`)
- 🛫 Migrações Flyway automáticas (V1, V2, V3)
- 🎛️ Todos endpoints Actuator ativos
- 💻 H2 Console em `/h2-console`

**Arquivo:** `src/main/resources/application-dev.yml`

---

### Production (prod) 🚀

```bash
export JWT_SECRET=sua-chave-secreta-de-producao
export DB_USER=seu_usuario
export DB_PASSWORD=sua_senha
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

**Características:**
- 🗄️ MySQL Database (`${DB_HOST}:${DB_PORT}/${DB_NAME}`)
- 🔒 Validação de schema (`ddl-auto: validate`)
- 🛫 Migrações Flyway no startup
- 📵 SQL logs desativados
- 🛡️ Actuator restrito a `health` e `info`
- ⚡ Pool de conexões HikariCP (max 20, min 5)
- 📝 Log level: `WARN` (root), `INFO` (br.com.fiap)

**Pré-requisito:** MySQL rodando
```sql
CREATE DATABASE IF NOT EXISTS skillhubdb;
```

**Arquivo:** `src/main/resources/application-prod.yml`

---

### UAT (User Acceptance Testing) 🧪

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=uat"
```

**Arquivo:** `src/main/resources/application-uat.yml`

---

## 📁 Estrutura do Projeto

```
skill-hub/
├── src/main/java/br/com/fiap/skill_hub/
│   ├── api/                         # 📄 Contratos Swagger (interfaces)
│   │   ├── UserApi.java
│   │   ├── SkillApi.java
│   │   ├── GroupApi.java
│   │   └── LoginApi.java
│   ├── controller/                  # 🔌 REST Controllers
│   │   ├── UserController.java
│   │   ├── SkillController.java
│   │   ├── GroupController.java
│   │   ├── LoginController.java
│   │   └── dto/                     # Data Transfer Objects
│   │       ├── UserDto.java
│   │       ├── SkillDto.java
│   │       ├── GroupDto.java
│   │       ├── LoginDto.java
│   │       ├── TokenResponseDto.java
│   │       ├── RefreshTokenRequestDto.java
│   │       ├── ErrorResponseDto.java
│   │       └── ProfileEnum.java
│   ├── service/                     # 💼 Lógica de Negócio
│   │   ├── UserService.java
│   │   ├── SkillService.java
│   │   ├── GroupService.java
│   │   ├── LoginService.java
│   │   └── JwtService.java
│   ├── repository/                  # 🗄️ Acesso a Dados
│   │   ├── UserRepository.java
│   │   ├── SkillRepository.java
│   │   ├── GroupRepository.java
│   │   ├── RefreshTokenRepository.java
│   │   └── entities/
│   │       ├── UserEntity.java
│   │       ├── SkillEntity.java
│   │       ├── GroupEntity.java
│   │       ├── RefreshTokenEntity.java
│   │       ├── AuditDataEntity.java
│   │       └── StatusEnum.java
│   ├── mapper/                      # 🔄 MapStruct Mappers
│   │   ├── UserMapper.java
│   │   ├── SkillMapper.java
│   │   └── GroupMapper.java
│   ├── exception/                   # ❌ Hierarquia de Exceções
│   │   ├── BusinessException.java   # Classe base (abstract)
│   │   ├── GlobalExceptionHandler.java
│   │   ├── auth/
│   │   │   ├── InvalidRefreshTokenException.java
│   │   │   ├── RefreshTokenMismatchException.java
│   │   │   ├── RefreshTokenNotFoundException.java
│   │   │   └── TokenHashGenerationException.java
│   │   ├── group/
│   │   │   ├── GroupNoAvailableSeatsException.java
│   │   │   ├── GroupNotFoundException.java
│   │   │   ├── GroupOwnerNotFoundException.java
│   │   │   ├── UserAlreadyGroupMemberException.java
│   │   │   └── UserLacksRequiredSkillsException.java
│   │   ├── skill/
│   │   │   ├── SkillAlreadyRegisteredException.java
│   │   │   ├── SkillDescriptionAlreadyInUseException.java
│   │   │   └── SkillNotFoundException.java
│   │   └── user/
│   │       ├── EmailAlreadyRegisteredException.java
│   │       └── UserNotFoundException.java
│   ├── security/                    # 🔐 Filtros de Segurança
│   │   ├── JwtAuthenticationFilter.java
│   │   └── TraceIdFilter.java
│   ├── config/                      # ⚙️ Configurações
│   │   ├── SecurityConfig.java
│   │   ├── OpenApiConfig.java
│   │   ├── I18nConfig.java
│   │   └── TraceContext.java
│   └── SkillHubApplication.java
│
├── src/main/resources/
│   ├── application.yml              # Config base + JWT + logging
│   ├── application-dev.yml          # Config desenvolvimento (H2)
│   ├── application-prod.yml         # Config produção (MySQL)
│   ├── application-uat.yml          # Config UAT
│   ├── messages.properties          # Mensagens i18n (PT-BR)
│   ├── messages_en.properties       # Mensagens i18n (EN)
│   └── db/migration/
│
├── src/test/java/
│   └── br/com/fiap/skill_hub/
│       ├── controller/              # Testes dos controllers
│       ├── service/                 # Testes dos services
│       ├── mapper/                  # Testes dos mappers
│       ├── exception/               # Testes do GlobalExceptionHandler
│       ├── security/                # Testes dos filtros
│       ├── ApiErrorHandlingIntegrationTest.java
│       └── SkillHubApplicationTests.java
│
├── docs/                            # 📖 Documentação técnica
│   ├── API_DOCUMENTATION.md
│   ├── DOMAINS.md
│   ├── ERROR_HANDLING.md
│   ├── SECURITY.md
│   ├── checklist.md
│   └── fluxodesistema.drawio.png
│
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── README.md
├── mvnw
└── mvnw.cmd
```

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 21 | Linguagem |
| Spring Boot | 4.0.3 | Framework |
| Spring Data JPA | 4.0.3 | ORM |
| Spring Security | 4.0.3 | Autenticação e autorização |
| JJWT | 0.12.7 | Geração e validação de tokens JWT |
| Flyway | managed by Spring Boot | Controle de versões do banco |
| MapStruct | 1.6.3 | Mapeamento DTO ↔ Entity |
| Lombok | Latest | Boilerplate reduction |
| H2 Database | managed | Banco em memória (dev/test) |
| MySQL | 8.0+ | Banco produção |
| SpringDoc OpenAPI | 2.0.2 | Swagger/OpenAPI |
| JaCoCo | 0.8.12 | Cobertura de testes |
| Maven | 3.9+ | Build tool |

---

## 🧪 Testes

### Executar todos os testes

```bash
export JWT_SECRET=minha-chave-super-secreta-de-pelo-menos-32-caracteres
./mvnw clean test
```

O JaCoCo gera relatório de cobertura em `target/site/jacoco/index.html`.

A verificação de cobertura (`jacoco:check`) exige **mínimo 80% de cobertura de linhas** nas classes de service, controller, mapper e filtro.

---

## 🐋 Docker

### Executar com Docker Compose

```bash
docker-compose up --build
```

---

## 🐛 Troubleshooting

### Porta 8080 já está em uso

```bash
lsof -i :8080
# Usar outra porta:
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev --server.port=8080"
```

### JWT_SECRET não configurado

```
Caused by: java.lang.IllegalArgumentException: JWT_SECRET environment variable is required
```
**Solução:**
```bash
export JWT_SECRET=minha-chave-super-secreta-de-pelo-menos-32-caracteres
```

### Erro de Conexão MySQL em Produção

```bash
mysql -u root -p
CREATE DATABASE IF NOT EXISTS skillhubdb;
```

---

## 📞 Suporte

Para dúvidas:

1. 📚 Consulte `docs/API_DOCUMENTATION.md`
2. 🌐 Acesse Swagger em `http://localhost:8080/swagger-ui/index.html`
3. ❤️ Verifique Health em `http://localhost:8080/actuator/health`

---

**Última atualização:** 04 de Abril de 2026

Made with ❤️ by Skill Hub Team
