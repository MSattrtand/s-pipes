package cz.cvut.spipes.exception;

public class SPipesServiceException extends SPipesException {
    public SPipesServiceException(String message) {
        super(message);
    }
    public SPipesServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
