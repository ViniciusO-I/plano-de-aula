package br.com.fiap.skill_hub.exception;

import br.com.fiap.skill_hub.controller.dto.ErrorResponseDto;
import br.com.fiap.skill_hub.exception.auth.InvalidRefreshTokenException;
import br.com.fiap.skill_hub.exception.auth.RefreshTokenMismatchException;
import br.com.fiap.skill_hub.exception.auth.RefreshTokenNotFoundException;
import br.com.fiap.skill_hub.exception.auth.TokenHashGenerationException;
import br.com.fiap.skill_hub.exception.group.*;
import br.com.fiap.skill_hub.exception.skill.SkillAlreadyRegisteredException;
import br.com.fiap.skill_hub.exception.skill.SkillDescriptionAlreadyInUseException;
import br.com.fiap.skill_hub.exception.skill.SkillNotFoundException;
import br.com.fiap.skill_hub.exception.user.EmailAlreadyRegisteredException;
import br.com.fiap.skill_hub.exception.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "java:S6831"})

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, key, locale);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {
        logger.warn("User not found: {}", ex.getMessage());
        String message = getMessage("error.user.not_found");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailAlreadyRegisteredException(
            EmailAlreadyRegisteredException ex, WebRequest request) {
        logger.warn("Email already registered: {}", ex.getMessage());
        String message = getMessage("error.email.already_registered");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(SkillNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleSkillNotFoundException(
            SkillNotFoundException ex, WebRequest request) {
        logger.warn("Skill not found: {}", ex.getMessage());
        String message = getMessage("error.skills.not_found");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(SkillAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponseDto> handleSkillAlreadyRegisteredException(
            SkillAlreadyRegisteredException ex, WebRequest request) {
        logger.warn("Skill already registered: {}", ex.getMessage());
        String message = getMessage("error.skill.already_registered");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(SkillDescriptionAlreadyInUseException.class)
    public ResponseEntity<ErrorResponseDto> handleSkillDescriptionAlreadyInUseException(
            SkillDescriptionAlreadyInUseException ex, WebRequest request) {
        logger.warn("Skill description already in use: {}", ex.getMessage());
        String message = getMessage("error.skill.description.already_in_use");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleGroupNotFoundException(
            GroupNotFoundException ex, WebRequest request) {
        logger.warn("Group not found: {}", ex.getMessage());
        String message = getMessage("error.group.not_found");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(GroupOwnerNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleGroupOwnerNotFoundException(
            GroupOwnerNotFoundException ex, WebRequest request) {
        logger.warn("Group owner not found: {}", ex.getMessage());
        String message = getMessage("error.group.owner.not_found");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UserAlreadyGroupMemberException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyGroupMemberException(
            UserAlreadyGroupMemberException ex, WebRequest request) {
        logger.warn("User already group member: {}", ex.getMessage());
        String message = getMessage("error.user.already_group_member");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(GroupNoAvailableSeatsException.class)
    public ResponseEntity<ErrorResponseDto> handleGroupNoAvailableSeatsException(
            GroupNoAvailableSeatsException ex, WebRequest request) {
        logger.warn("Group no available seats: {}", ex.getMessage());
        String message = getMessage("error.group.no_available_seats");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UserLacksRequiredSkillsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserLacksRequiredSkillsException(
            UserLacksRequiredSkillsException ex, WebRequest request) {
        logger.warn("User lacks required skills: {}", ex.getMessage());
        String message = getMessage("error.user.lacks_required_skills");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidRefreshTokenException(
            InvalidRefreshTokenException ex, WebRequest request) {
        logger.warn("Invalid refresh token: {}", ex.getMessage());
        String message = getMessage("error.auth.invalid_refresh_token");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRefreshTokenNotFoundException(
            RefreshTokenNotFoundException ex, WebRequest request) {
        logger.warn("Refresh token not found: {}", ex.getMessage());
        String message = getMessage("error.auth.refresh_token.not_found");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(RefreshTokenMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleRefreshTokenMismatchException(
            RefreshTokenMismatchException ex, WebRequest request) {
        logger.warn("Refresh token mismatch: {}", ex.getMessage());
        String message = getMessage("error.auth.refresh_token.mismatch");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(TokenHashGenerationException.class)
    public ResponseEntity<ErrorResponseDto> handleTokenHashGenerationException(
            TokenHashGenerationException ex, WebRequest request) {
        logger.error("Token hash generation error: {}", ex.getMessage(), ex);
        String message = getMessage("error.auth.token_hash.generation_error");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameNotFoundException(
            UsernameNotFoundException ex, WebRequest request) {
        logger.warn("User not found during authentication: {}", ex.getMessage());
        String message = getMessage("error.user.not_found");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                "USER_NOT_FOUND",
                message
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {
        logger.warn("Bad credentials: {}", ex.getMessage());
        String message = getMessage("error.auth.invalid_credentials");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                "INVALID_CREDENTIALS",
                message
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Validation error: {}", ex.getMessage());
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + getMessage(error.getDefaultMessage()))
                .collect(Collectors.joining(", "));
        String message = getMessage("error.validation.failed");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                message,
                details
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(
            Exception ex, WebRequest request) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        String message = getMessage("error.internal_server_error");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                message
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

