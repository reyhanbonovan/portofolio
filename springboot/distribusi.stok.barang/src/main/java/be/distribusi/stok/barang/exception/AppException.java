package be.distribusi.stok.barang.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
    private final String statusCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final String loggerId;

    // Constructor
    public AppException(String statusCode, String errorMessage, HttpStatus httpStatus, String loggerId) {
        super(errorMessage); // Pass the error message to the superclass
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
        this.loggerId = loggerId;
    }

}


