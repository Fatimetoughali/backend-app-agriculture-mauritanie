package com.agriculture.mauritanie.exception;

import com.agriculture.mauritanie.dto.auth.ApiResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponseWrapper<Object>> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {

        log.error("Erreur d'authentification: {}", ex.getMessage());

        ApiResponseWrapper<Object> response = ApiResponseWrapper.builder()
                .success(false)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        log.error("Ressource non trouvée: {}", ex.getMessage());

        ApiResponseWrapper<Object> response = ApiResponseWrapper.builder()
                .success(false)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponseWrapper<Object>> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {

        log.error("Ressource dupliquée: {}", ex.getMessage());

        ApiResponseWrapper<Object> response = ApiResponseWrapper.builder()
                .success(false)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponseWrapper<Map<String, String>>> handleValidationException(
            ValidationException ex, WebRequest request) {

        log.error("Erreur de validation: {}", ex.getMessage());

        ApiResponseWrapper<Map<String, String>> response = ApiResponseWrapper.<Map<String, String>>builder()
                .success(false)
                .message(ex.getMessage())
                .data(ex.getErrors())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseWrapper<Map<String, String>>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {

        log.error("Erreur de validation des arguments: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponseWrapper<Map<String, String>> response = ApiResponseWrapper.<Map<String, String>>builder()
                .success(false)
                .message("Erreurs de validation")
                .data(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseWrapper<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        log.error("Argument illégal: {}", ex.getMessage());

        ApiResponseWrapper<Object> response = ApiResponseWrapper.builder()
                .success(false)
                .message("Paramètre invalide: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseWrapper<Object>> handleGlobalException(
            Exception ex, WebRequest request) {

        log.error("Erreur interne du serveur: {}", ex.getMessage(), ex);

        ApiResponseWrapper<Object> response = ApiResponseWrapper.builder()
                .success(false)
                .message("Une erreur interne s'est produite")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseWrapper<Object>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        log.error("Erreur d'exécution: {}", ex.getMessage(), ex);

        ApiResponseWrapper<Object> response = ApiResponseWrapper.builder()
                .success(false)
                .message("Erreur d'exécution: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}