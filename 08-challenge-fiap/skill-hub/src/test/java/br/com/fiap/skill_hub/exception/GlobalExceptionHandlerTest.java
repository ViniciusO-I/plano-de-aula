package br.com.fiap.skill_hub.exception;

import br.com.fiap.skill_hub.controller.dto.ErrorResponseDto;
import br.com.fiap.skill_hub.exception.auth.InvalidRefreshTokenException;
import br.com.fiap.skill_hub.exception.auth.RefreshTokenMismatchException;
import br.com.fiap.skill_hub.exception.auth.RefreshTokenNotFoundException;
import br.com.fiap.skill_hub.exception.auth.TokenHashGenerationException;
import br.com.fiap.skill_hub.exception.group.GroupNoAvailableSeatsException;
import br.com.fiap.skill_hub.exception.group.GroupNotFoundException;
import br.com.fiap.skill_hub.exception.group.GroupOwnerNotFoundException;
import br.com.fiap.skill_hub.exception.group.UserAlreadyGroupMemberException;
import br.com.fiap.skill_hub.exception.group.UserLacksRequiredSkillsException;
import br.com.fiap.skill_hub.exception.skill.SkillAlreadyRegisteredException;
import br.com.fiap.skill_hub.exception.skill.SkillDescriptionAlreadyInUseException;
import br.com.fiap.skill_hub.exception.skill.SkillNotFoundException;
import br.com.fiap.skill_hub.exception.user.EmailAlreadyRegisteredException;
import br.com.fiap.skill_hub.exception.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private WebRequest webRequest;

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler(messageSource);
        when(messageSource.getMessage(anyString(), any(), anyString(), any()))
                .thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void shouldHandleUserNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException("User not found");

        ResponseEntity<ErrorResponseDto> response = handler.handleUserNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("USER_NOT_FOUND", response.getBody().code());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().status());
    }

    @Test
    void shouldHandleEmailAlreadyRegisteredException() {
        EmailAlreadyRegisteredException ex = new EmailAlreadyRegisteredException("Email already registered");

        ResponseEntity<ErrorResponseDto> response = handler.handleEmailAlreadyRegisteredException(ex, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EMAIL_ALREADY_REGISTERED", response.getBody().code());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().status());
    }

    @Test
    void shouldHandleSkillNotFoundException() {
        SkillNotFoundException ex = new SkillNotFoundException("Skill not found");

        ResponseEntity<ErrorResponseDto> response = handler.handleSkillNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("SKILL_NOT_FOUND", response.getBody().code());
    }

    @Test
    void shouldHandleSkillAlreadyRegisteredException() {
        SkillAlreadyRegisteredException ex = new SkillAlreadyRegisteredException("Skill already registered");

        ResponseEntity<ErrorResponseDto> response = handler.handleSkillAlreadyRegisteredException(ex, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("SKILL_ALREADY_REGISTERED", response.getBody().code());
    }

    @Test
    void shouldHandleSkillDescriptionAlreadyInUseException() {
        SkillDescriptionAlreadyInUseException ex = new SkillDescriptionAlreadyInUseException("Description already in use");

        ResponseEntity<ErrorResponseDto> response = handler.handleSkillDescriptionAlreadyInUseException(ex, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("SKILL_DESCRIPTION_ALREADY_IN_USE", response.getBody().code());
    }

    @Test
    void shouldHandleGroupNotFoundException() {
        GroupNotFoundException ex = new GroupNotFoundException("Group not found");

        ResponseEntity<ErrorResponseDto> response = handler.handleGroupNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("GROUP_NOT_FOUND", response.getBody().code());
    }

    @Test
    void shouldHandleGroupOwnerNotFoundException() {
        GroupOwnerNotFoundException ex = new GroupOwnerNotFoundException("Owner not found");

        ResponseEntity<ErrorResponseDto> response = handler.handleGroupOwnerNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("GROUP_OWNER_NOT_FOUND", response.getBody().code());
    }

    @Test
    void shouldHandleUserAlreadyGroupMemberException() {
        UserAlreadyGroupMemberException ex = new UserAlreadyGroupMemberException("Already a member");

        ResponseEntity<ErrorResponseDto> response = handler.handleUserAlreadyGroupMemberException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("USER_ALREADY_GROUP_MEMBER", response.getBody().code());
    }

    @Test
    void shouldHandleGroupNoAvailableSeatsException() {
        GroupNoAvailableSeatsException ex = new GroupNoAvailableSeatsException("No seats available");

        ResponseEntity<ErrorResponseDto> response = handler.handleGroupNoAvailableSeatsException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("GROUP_NO_AVAILABLE_SEATS", response.getBody().code());
    }

    @Test
    void shouldHandleUserLacksRequiredSkillsException() {
        UserLacksRequiredSkillsException ex = new UserLacksRequiredSkillsException("User lacks required skills");

        ResponseEntity<ErrorResponseDto> response = handler.handleUserLacksRequiredSkillsException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("USER_LACKS_REQUIRED_SKILLS", response.getBody().code());
    }

    @Test
    void shouldHandleInvalidRefreshTokenException() {
        InvalidRefreshTokenException ex = new InvalidRefreshTokenException("Invalid refresh token");

        ResponseEntity<ErrorResponseDto> response = handler.handleInvalidRefreshTokenException(ex, webRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INVALID_REFRESH_TOKEN", response.getBody().code());
    }

    @Test
    void shouldHandleRefreshTokenNotFoundException() {
        RefreshTokenNotFoundException ex = new RefreshTokenNotFoundException("Refresh token not found");

        ResponseEntity<ErrorResponseDto> response = handler.handleRefreshTokenNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("REFRESH_TOKEN_NOT_FOUND", response.getBody().code());
    }

    @Test
    void shouldHandleRefreshTokenMismatchException() {
        RefreshTokenMismatchException ex = new RefreshTokenMismatchException("Refresh token mismatch");

        ResponseEntity<ErrorResponseDto> response = handler.handleRefreshTokenMismatchException(ex, webRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("REFRESH_TOKEN_MISMATCH", response.getBody().code());
    }

    @Test
    void shouldHandleTokenHashGenerationException() {
        RuntimeException cause = new RuntimeException("SHA-256 not available");
        TokenHashGenerationException ex = new TokenHashGenerationException("Token hash generation error", cause);

        ResponseEntity<ErrorResponseDto> response = handler.handleTokenHashGenerationException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("TOKEN_HASH_GENERATION_ERROR", response.getBody().code());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void shouldHandleUsernameNotFoundException() {
        UsernameNotFoundException ex = new UsernameNotFoundException("User not found during authentication");

        ResponseEntity<ErrorResponseDto> response = handler.handleUsernameNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("USER_NOT_FOUND", response.getBody().code());
    }

    @Test
    void shouldHandleBadCredentialsException() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");

        ResponseEntity<ErrorResponseDto> response = handler.handleBadCredentialsException(ex, webRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INVALID_CREDENTIALS", response.getBody().code());
    }

    @Test
    void shouldHandleMethodArgumentNotValidExceptionWithFieldErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("loginDto", "email", "validation.login.email.required");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponseDto> response = handler.handleMethodArgumentNotValidException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("VALIDATION_ERROR", response.getBody().code());
        assertNotNull(response.getBody().details());
    }

    @Test
    void shouldHandleGenericException() {
        Exception ex = new RuntimeException("Unexpected error occurred");

        ResponseEntity<ErrorResponseDto> response = handler.handleGlobalException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().code());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status());
    }
}

