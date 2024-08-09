package be.distribusi.stok.barang.exception;

import org.springframework.http.HttpStatus;

public class AppException extends Exception {
    private final String statusCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    // Constructor
    public AppException(String statusCode, String errorMessage, HttpStatus httpStatus) {
        super(errorMessage); // Pass the error message to the superclass
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

    // Getter for status code
    public String getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // Getter for HTTP status
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}


