# 🎓 Skill Hub - Plataforma de Gerenciamento de Habilidades

> Aplicação para criar grupos baseado na skill do usuário

[![Java](https://img.shields.io/badge/Java-21-orange?logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-green?logo=spring)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apache-maven)](https://maven.apache.org/)

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Pré-requisitos](#-pré-requisitos)
- [Build da Aplicação](#-build-da-aplicação)
- [Migrações com Flyway](#-migrações-com-flyway)
- [Executar com Profile Dev](#-executar-com-profile-dev)
- [Health Check](#-health-check)
- [Swagger - Documentação da API](#-swagger---documentação-da-api)
- [Documentação Técnica](#-documentação-técnica)
- [Configuração de Profiles](#-configuração-de-profiles)

---

## 🎯 Sobre o Projeto

**Skill Hub** é uma aplicação Spring Boot desenvolvida para o desafio Oracle do programa FIAP 2TDS.

**Funcionalidades:**
- ✅ Gerenciar usuários com diferentes perfis (STUDENT, ADMINISTRATOR)
- ✅ Mapeamento de usuários para habilidades específicas
- ✅ Criação de grupos baseado em skills compartilhadas
- ✅ API RESTful documentada com Swagger/OpenAPI
- ✅ Múltiplos ambientes (Dev, Prod, UAT, Test)

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

## 📥 Instalação

### 1. Clone o Repositório

```bash
git clone <seu-repositorio-url>
cd skill-hub
```

### 2. Instale as Dependências

```bash
./mvnw clean install
```

Ou no Windows:
```cmd
mvnw.cmd clean install
```

---

## 🔨 Build da Aplicação

### Build Completo (Com Testes)

```bash
./mvnw clean package
```

### Build Sem Testes

```bash
./mvnw clean package -DskipTests
```

### Build de Desenvolvimento

```bash
./mvnw clean compile
```

---

## 🛫 Migrações com Flyway

O projeto usa Flyway para versionar e aplicar schema no startup. O Hibernate fica apenas com validação (`ddl-auto: validate`).

### Script inicial

- Local: `src/main/resources/db/migration/V1__create_user_entity.sql`
- Convenção: `V<versao>__<descricao>.sql`

### Fluxo para novas mudanças de banco

1. Crie um novo arquivo em `src/main/resources/db/migration`.
2. Nomeie seguindo a próxima versão, por exemplo `V2__add_user_unique_email.sql`.
3. Suba a aplicação normalmente; o Flyway executa a migração pendente automaticamente.

### Comandos úteis

```bash
./mvnw clean test
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

---

## 🚀 Executar com Profile Dev

### Opção 1: Maven (Recomendado)

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Opção 2: Variável de Ambiente

**Linux/Mac:**
```bash
export SPRING_PROFILES_ACTIVE=dev
./mvnw spring-boot:run
```

**Windows (CMD):**
```cmd
set SPRING_PROFILES_ACTIVE=dev
mvnw spring-boot:run
```

**Windows (PowerShell):**
```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
mvnw spring-boot:run
```

### Opção 3: JAR Executável

```bash
# Build primeiro
./mvnw clean package

# Executar
java -Dspring.profiles.active=dev -jar target/skill-hub-0.0.1-SNAPSHOT.jar
```

### Opção 4: IDE (IntelliJ IDEA)

1. Clique em `Run` → `Edit Configurations...`
2. Em `VM options`, adicione: `-Dspring.profiles.active=dev`
3. Clique `OK` e execute (Shift + F10)

---

## ✅ Mensagem de Sucesso

Quando a aplicação inicia com sucesso, você verá:

```
✅ Active Profile: dev

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___| '_ | '_| '/ _` | \ \ \ \
 \\/  ___)_)  '_| | | (_ ) ) ) ) )
  '  ____ .___ __ _\__,  / / / /
 =========_==============___/=/_/_/_/

2026-03-28T14:59:59.419-03:00  INFO ... : Started SkillHubApplication
```

---

## ❤️ Health Check

Para verificar se a aplicação está saudável:

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

### Outros Endpoints Úteis

```
http://localhost:8080/actuator/info
http://localhost:8080/actuator/metrics
http://localhost:8080/actuator/health/details
```

---

## 📚 Swagger - Documentação da API

A aplicação possui documentação automática com **Swagger/OpenAPI**.

### ✨ Acessar a Interface Swagger UI

Com a aplicação rodando, abra seu navegador:

```
http://localhost:8080/swagger-ui.html
```

### Na Interface Swagger você pode:

✅ Ver todos os endpoints disponíveis  
✅ Ler descrição e documentação de cada operação  
✅ Ver exemplos de requisição/resposta  
✅ **Testar endpoints direto** (botão "Try it out")  
✅ Baixar a especificação OpenAPI em JSON  

### Acessar JSON OpenAPI

```
http://localhost:8080/v3/api-docs
```

### Exemplos de Requisições

#### Criar Usuário (POST)

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "senha123",
    "profile": "STUDENT"
  }'
```

#### Listar Usuários (GET)

```bash
curl http://localhost:8080/api/users
```

---

## 📖 Documentação Técnica

A pasta `docs/` contém toda a documentação técnica do projeto:

```
docs/
├── API_DOCUMENTATION.md              # 📄 Documentação detalhada da API
├── CHALLENGE*.pdf                    # 📋 Especificação completa do desafio
├── checklist.md                      # ✅ Checklist do projeto
└── fluxodesistema.drawio.png         # 🎨 Diagrama do fluxo do sistema
```

### 📄 API_DOCUMENTATION.md

Documentação completa sobre como usar a API:

```bash
cat docs/API_DOCUMENTATION.md
```

Contém:
- Como acessar Swagger UI
- Exemplos de requisições/respostas
- Explicação das anotações Swagger
- Checklist de implementação

### 📋 CHALLENGE ORACLE

Especificação completa do desafio FIAP Oracle com todos os requisitos:

```bash
# Abrir o PDF
open docs/CHALLENGE\ ORACLE*.pdf    # Mac
xdg-open docs/CHALLENGE\ ORACLE*.pdf # Linux
```

### ✅ Checklist

Acompanhe o progresso do projeto:

```bash
cat docs/checklist.md
```

### 🎨 Fluxo do Sistema

Visualize a arquitetura e fluxo:

```bash
# Abrir a imagem
open docs/fluxodesistema.drawio.png
```

---

## ⚙️ Configuração de Profiles

A aplicação suporta múltiplos profiles para diferentes ambientes:

### Development (dev) ✨

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

**Características:**
- 🗄️ Banco H2 em memória
- 🛫 Migrações Flyway automáticas
- 📝 SQL logs ativados
- 🎛️ Todos endpoints Actuator ativos
- 💻 H2 Console em `/h2-console`
- 🐛 Debug mode ativo

**Arquivo:** `src/main/resources/application-dev.yml`

---

### Production (prod) 🚀

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

**Características:**
- 🗄️ MySQL Database
- 🔒 Validação de schema
- 🛫 Migrações Flyway no startup
- 📵 SQL logs desativados
- 🛡️ Actuator restrito
- ⚡ Pool de conexões otimizado

**Pré-requisito:** MySQL rodando localmente
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

### Test (test) 🔬

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=test"
```

**Arquivo:** `src/main/resources/application-test.yml`

---

## 📁 Estrutura do Projeto

```
skill-hub/
├── src/main/java/br/com/fiap/skill_hub/
│   ├── controller/              # 🔌 REST Controllers
│   │   ├── UserController.java
│   │   ├── UserApi.java         # Interface com documentação Swagger
│   │   └── dto/                 # Data Transfer Objects
│   ├── service/                 # 💼 Lógica de Negócio
│   │   └── UserService.java
│   ├── repository/              # 🗄️ Acesso a Dados
│   │   ├── UserRepository.java
│   │   └── entities/            # Entidades JPA
│   ├── mapper/                  # 🔄 MapStruct Mappers
│   │   └── UserMapper.java
│   ├── config/                  # ⚙️ Configurações
│   │   └── OpenApiConfig.java
│   └── SkillHubApplication.java
│
├── src/main/resources/
│   ├── application.yml          # Config base
│   ├── application-dev.yml      # Config desenvolvimento
│   ├── application-prod.yml     # Config produção
│   ├── application-uat.yml      # Config UAT
│   └── application-test.yml     # Config testes
│
├── docs/                        # 📖 DOCUMENTAÇÃO TÉCNICA
│   ├── API_DOCUMENTATION.md
│   ├── CHALLENGE*.pdf
│   ├── checklist.md
│   └── fluxodesistema.drawio.png
│
├── pom.xml                      # Maven config
├── README.md                    # Este arquivo
├── mvnw                         # Maven Wrapper (Unix)
└── mvnw.cmd                     # Maven Wrapper (Windows)
```

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 21 | Linguagem |
| Spring Boot | 4.0.3 | Framework |
| Spring Data JPA | 4.0.3 | ORM |
| Flyway | managed by Spring Boot | Controle de versões do banco |
| MapStruct | 1.6.3 | Mapeamento DTO ↔ Entity |
| Lombok | Latest | Boilerplate reduction |
| H2 Database | - | Banco em memória (dev) |
| MySQL | 8.0+ | Banco produção |
| SpringDoc OpenAPI | 2.0.2 | Swagger/OpenAPI |
| Maven | 3.9+ | Build tool |

---

## 🐛 Troubleshooting

### Porta 8080 já está em uso

```bash
# Encontrar processo
lsof -i :8080

# Usar outra porta
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev --server.port=8081"
```

### Swagger UI não aparece

```bash
./mvnw clean compile
# Acesse: http://localhost:8080/swagger-ui.html
```

### Erro de Conexão MySQL em Produção

```bash
# Criar banco
mysql -u root -p
CREATE DATABASE skillhubdb;
```

---

## 📞 Suporte

Para dúvidas:

1. 📚 Consulte `docs/API_DOCUMENTATION.md`
2. 🌐 Acesse Swagger em `http://localhost:8080/swagger-ui.html`
3. ❤️ Verifique Health em `http://localhost:8080/actuator/health`

---

**Última atualização:** 28 de Março de 2026

Made with ❤️ by Skill Hub Team
