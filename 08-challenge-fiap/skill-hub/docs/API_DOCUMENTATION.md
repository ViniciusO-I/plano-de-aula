# 📚 Documentação da API - Skill Hub

## 🎯 Sobre a Documentação

A documentação da API Skill Hub é gerada automaticamente usando **Swagger/OpenAPI** com **SpringDoc**. A interface está em um contrato (`UserApi`) que a `UserController` implementa.

---

## 📖 Arquivos Criados

### 1. **UserApi.java** (Interface de Contrato)
- Localização: `src/main/java/br/com/fiap/skill_hub/controller/UserApi.java`
- Define todas as operações disponíveis para o usuário
- Contém anotações Swagger para documentação
- Endpoints:
  - `POST /api/users` - Criar novo usuário
  - `GET /api/users` - Listar todos os usuários

### 2. **UserController.java** (Implementação)
- Localização: `src/main/java/br/com/fiap/skill_hub/controller/UserController.java`
- Implementa a interface `UserApi`
- Herda automaticamente toda a documentação Swagger

### 3. **OpenApiConfig.java** (Configuração)
- Localização: `src/main/java/br/com/fiap/skill_hub/config/OpenApiConfig.java`
- Configura a documentação OpenAPI
- Define informações gerais da API (título, versão, contato, etc.)

### 4. **pom.xml** (Dependência)
- Adicionada dependência: `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2`

---

## 🚀 Como Acessar a Documentação

### 1. **Iniciar a Aplicação**

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

Ou com seu profile preferido:
```bash
# Produção
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"

# Teste
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=test"
```

### 2. **Acessar a Interface Swagger UI**

Abra seu navegador e acesse:

```
http://localhost:8080/swagger-ui.html
```

Você verá a documentação interativa com:
- ✅ Listagem de todos os endpoints
- ✅ Descrição detalhada de cada operação
- ✅ Schemas dos DTOs (UserDto)
- ✅ Botão "Try it out" para testar direto na interface

### 3. **Acessar o JSON OpenAPI (máquina)**

```
http://localhost:8080/v3/api-docs
```

---

## 📝 Exemplos de Uso

### **Criar Usuário (POST)**

**URL:** `POST http://localhost:8080/api/users`

**Request Body:**
```json
{
  "name": "João Silva",
  "email": "joao.silva@example.com",
  "password": "senha123",
  "profile": "STUDENT"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao.silva@example.com",
  "password": null,
  "profile": null
}
```

### **Listar Usuários (GET)**

**URL:** `GET http://localhost:8080/api/users`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "João Silva",
    "email": "joao.silva@example.com",
    "password": null,
    "profile": null
  },
  {
    "id": 2,
    "name": "Maria Santos",
    "email": "maria.santos@example.com",
    "password": null,
    "profile": null
  }
]
```

---

## 🔍 Anotações Swagger Utilizadas

### **Em `UserApi.java`:**

1. **@Tag** - Agrupa endpoints relacionados
   ```java
   @Tag(
       name = "Users",
       description = "Endpoints para gerenciar usuários..."
   )
   ```

2. **@Operation** - Descreve a operação
   ```java
   @Operation(
       summary = "Criar novo usuário",
       description = "Cria um novo usuário no sistema..."
   )
   ```

3. **@ApiResponse** - Define possíveis respostas
   ```java
   @ApiResponse(
       responseCode = "200",
       description = "Usuário criado com sucesso"
   )
   ```

4. **@Schema** - Define estrutura de dados (automático para DTOs)

---

## 🎨 Estrutura de Documentação

```
UserApi (Interface)
├── @Tag("Users")
│   ├── create()
│   │   ├── @Operation
│   │   ├── @ApiResponse(200 - Sucesso)
│   │   ├── @ApiResponse(400 - Dados inválidos)
│   │   └── @ApiResponse(500 - Erro servidor)
│   │
│   └── list()
│       ├── @Operation
│       ├── @ApiResponse(200 - Sucesso)
│       └── @ApiResponse(500 - Erro servidor)
│
UserController (Implementação)
└── Implementa UserApi
    ├── create(UserDto)
    └── list()

OpenApiConfig (Configuração)
└── Customiza título, versão, contato, etc.
```

---

## ✅ Checklist de Implementação

- ✅ Interface `UserApi.java` criada com anotações Swagger
- ✅ `UserController` implementa `UserApi`
- ✅ `OpenApiConfig.java` configurado
- ✅ Dependência `springdoc-openapi-starter-webmvc-ui` adicionada ao `pom.xml`
- ✅ Documentação acessível em `/swagger-ui.html`

---

## 📚 Próximas Melhorias

Para adicionar mais endpoints documentados, basta:

1. Adicionar novo método na interface `UserApi`
2. Decorar com `@Operation` e `@ApiResponse`
3. Implementar na `UserController`

Exemplo:
```java
// Em UserApi.java
@GetMapping("/{id}")
@Operation(summary = "Buscar usuário por ID")
ResponseEntity<UserDto> findById(@PathVariable Integer id);

// Em UserController.java
@Override
public ResponseEntity<UserDto> findById(Integer id) {
    // implementação
}
```

---

## 🔗 Links Úteis

- [SpringDoc OpenAPI](https://springdoc.org/)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)
- [OpenAPI Specification](https://spec.openapis.org/)

---

**Documentação gerada automaticamente com Swagger/OpenAPI**

