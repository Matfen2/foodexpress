package com.foodexpress.exception;

import com.foodexpress.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
        log.warn("Ressource non trouvée : {}", ex.getMessage());
        return ErrorResponse.builder()
                .error("NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidState(InvalidOrderStateException ex) {
        log.warn("Transition de statut invalide : {}", ex.getMessage());
        return ErrorResponse.builder()
                .error("INVALID_ORDER_STATE")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @SuppressWarnings("deprecation")
    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleBusinessRule(BusinessRuleException ex) {
        log.warn("Règle métier violée : {}", ex.getMessage());
        return ErrorResponse.builder()
                .error("BUSINESS_RULE_VIOLATION")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        return ErrorResponse.builder()
                .error("VALIDATION_FAILED")
                .message("Les données envoyées sont invalides")
                .timestamp(LocalDateTime.now())
                .details(errors)
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneral(Exception ex) {
        log.error("Erreur inattendue", ex);
        return ErrorResponse.builder()
                .error("INTERNAL_ERROR")
                .message("Une erreur interne est survenue")
                .timestamp(LocalDateTime.now())
                .build();
    }
}