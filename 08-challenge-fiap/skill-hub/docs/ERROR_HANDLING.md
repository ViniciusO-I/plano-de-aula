# Sistema de Tratamento de Erros - Skill Hub

## Visão Geral

O Skill Hub possui um sistema robusto de tratamento de erros implementado através de:

1. **Exceções Customizadas** - Classes específicas para cada cenário de erro
2. **Global Exception Handler** - Controlador centralizado que captura e responde a exceções
3. **Message Bundles** - Mensagens externalizadas para suporte a i18n (internacionalização)
4. **Response DTO** - Formato padronizado para respostas de erro

## Arquitetura

### 1. Exceções Customizadas

Todas as exceções herdam de `BusinessException`, que estende `RuntimeException`:

```
BusinessException (classe abstrata)
├── UserNotFoundException
├── EmailAlreadyRegisteredException
├── SkillNotFoundException
├── SkillAlreadyRegisteredException
├── SkillDescriptionAlreadyInUseException
├── GroupNotFoundException
├── GroupOwnerNotFoundException
├── UserAlreadyGroupMemberException
├── GroupNoAvailableSeatsException
├── UserLacksRequiredSkillsException
├── InvalidRefreshTokenException
├── RefreshTokenNotFoundException
├── RefreshTokenMismatchException
└── TokenHashGenerationException
```

### 2. GlobalExceptionHandler

O `@ControllerAdvice` centraliza o tratamento de:
- **Exceções de negócio** - Lançadas pela aplicação
- **Exceções Spring Security** - Autenticação e autorização
- **Exceções de validação** - MethodArgumentNotValidException
- **Exceções genéricas** - Fallback para erros inesperados

## Como Usar

### Lançando Exceções

```java
@Service
public class UserService {
    
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }
    
    public User registerUser(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException("Email " + email + " already registered");
        }
        // ... criar usuário
    }
}
```

### Mensagens Externalizadas

As mensagens estão em arquivos de propriedades no classpath:

- `messages.properties` - Português Brasileiro (padrão)
- `messages_en.properties` - Inglês

#### Exemplo de Uso

```java
String message = getMessage("error.user.not_found");
```

O `LocaleContextHolder` automaticamente detecta o idioma da requisição via header `Accept-Language`.

## Resposta de Erro Padronizada

Todas as respostas de erro seguem este formato:

```json
{
  "timestamp": "2024-04-04T10:30:00",
  "status": 404,
  "code": "USER_NOT_FOUND",
  "message": "Usuário não encontrado",
  "details": null
}
```

### Campos

| Campo | Descrição | Exemplo |
|-------|-----------|---------|
| `timestamp` | Data/hora da erro | 2024-04-04T10:30:00 |
| `status` | Código HTTP | 404, 400, 500 |
| `code` | Código único do erro | USER_NOT_FOUND |
| `message` | Mensagem traduzida | "Usuário não encontrado" |
| `details` | Detalhes adicionais (opcional) | "email: Email já cadastrado" |

## Mapeamento de Status HTTP

| Tipo de Erro | Status HTTP | Exemplo |
|-------------|------------|---------|
| Not Found | 404 | UserNotFoundException |
| Conflict | 409 | EmailAlreadyRegisteredException |
| Bad Request | 400 | ValidationException, UserAlreadyGroupMemberException |
| Unauthorized | 401 | InvalidRefreshTokenException |
| Internal Server Error | 500 | TokenHashGenerationException, Exception genérica |

## Códigos de Erro

### Usuário (User)
- `USER_NOT_FOUND` - Usuário não encontrado
- `EMAIL_ALREADY_REGISTERED` - E-mail já cadastrado

### Skill
- `SKILL_NOT_FOUND` - Skill não encontrada
- `SKILL_ALREADY_REGISTERED` - Skill já cadastrada
- `SKILL_DESCRIPTION_ALREADY_IN_USE` - Descrição já em uso

### Grupo (Group)
- `GROUP_NOT_FOUND` - Grupo não encontrado
- `GROUP_OWNER_NOT_FOUND` - Owner do grupo não encontrado
- `USER_ALREADY_GROUP_MEMBER` - Usuário já é membro
- `GROUP_NO_AVAILABLE_SEATS` - Sem vagas disponíveis
- `USER_LACKS_REQUIRED_SKILLS` - Sem skills mínimas

### Autenticação (Auth)
- `INVALID_CREDENTIALS` - Credenciais inválidas
- `INVALID_REFRESH_TOKEN` - Refresh token inválido
- `REFRESH_TOKEN_NOT_FOUND` - Token não encontrado
- `REFRESH_TOKEN_MISMATCH` - Token divergente
- `TOKEN_HASH_GENERATION_ERROR` - Erro ao gerar hash

### Validação
- `VALIDATION_ERROR` - Erro na validação de dados

### Sistema
- `INTERNAL_SERVER_ERROR` - Erro interno do servidor

## Configuração de i18n

A configuração de internacionalização está em `I18nConfig`:

```java
@Bean
public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setCacheSeconds(3600);
    return messageSource;
}

@Bean
public LocaleResolver localeResolver() {
    AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
    localeResolver.setDefaultLocale(new Locale("pt", "BR"));
    return localeResolver;
}
```

### Exemplos de Requisição

**Português Brasileiro (padrão):**
```
GET /api/users/1
Accept-Language: pt-BR
```

**Inglês:**
```
GET /api/users/1
Accept-Language: en-US
```

## Adicionando Novos Erros

Para adicionar um novo erro, siga estes passos:

### 1. Criar a Exceção Customizada

```java
// src/main/java/br/com/fiap/skill_hub/exception/user/NewException.java
package br.com.fiap.skill_hub.exception.user;

import br.com.fiap.skill_hub.exception.BusinessException;

public class NewException extends BusinessException {
    
    public NewException(String message) {
        super(message, "NEW_ERROR_CODE");
    }
}
```

### 2. Adicionar Mensagens

**messages.properties:**
```properties
error.new.key=Mensagem do erro em Português
```

**messages_en.properties:**
```properties
error.new.key=Error message in English
```

### 3. Adicionar Handler no GlobalExceptionHandler

```java
@ExceptionHandler(NewException.class)
public ResponseEntity<ErrorResponseDto> handleNewException(
        NewException ex, WebRequest request) {
    logger.warn("New error: {}", ex.getMessage());
    String message = getMessage("error.new.key");
    ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            ex.getCode(),
            message
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
}
```

### 4. Lançar a Exceção no Service

```java
@Service
public class MyService {
    
    public void someMethod() {
        throw new NewException("Descrição do erro");
    }
}
```

## Logging

Todos os erros são registrados automaticamente com níveis apropriados:

- **WARN** - Erros de negócio esperados
- **ERROR** - Erros inesperados do sistema

```
logger.warn("User not found: User with ID 123 not found");
logger.error("Unexpected error: ...", ex);
```

## Validação de Dados

Erros de validação são capturados automaticamente:

```java
@PostMapping("/register")
public ResponseEntity<UserDto> register(
        @Valid @RequestBody UserRegisterDto dto) {
    // ...
}
```

**Resposta de erro:**
```json
{
  "timestamp": "2024-04-04T10:30:00",
  "status": 400,
  "code": "VALIDATION_ERROR",
  "message": "Falha na validação dos dados",
  "details": "email: Email inválido, password: Senha fraca"
}
```

## Boas Práticas

1. **Use exceções específicas** - Não lance exceções genéricas
2. **Inclua contexto** - Passe mensagens descritivas com IDs ou valores
3. **Traduza mensagens** - Adicione sempre as mensagens em PT-BR e EN
4. **Teste tratamento de erros** - Valide as respostas em testes
5. **Documente códigos de erro** - Mantenha este documento atualizado

## Exemplo Completo

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        User user = userService.findById(id); // Pode lançar UserNotFoundException
        return ResponseEntity.ok(new UserDto(user));
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(
            @Valid @RequestBody UserRegisterDto dto) {
        // Pode lançar EmailAlreadyRegisteredException
        User user = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserDto(user));
    }
}
```

## Referências

- [Spring Exception Handling](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc)
- [Global Exception Handler](https://www.baeldung.com/exception-handling-for-rest-with-spring)
- [Internationalization (i18n)](https://www.baeldung.com/spring-boot-internationalization)

