package br.com.fiap.skill_hub.exception;

/**
 * Classe base para todas as exceções de negócio da aplicação.
 * Herda de RuntimeException para não exigir tratamento declarado.
 */
public abstract class BusinessException extends RuntimeException {
    
    private final String code;
    
    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }
    
    public BusinessException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
}

