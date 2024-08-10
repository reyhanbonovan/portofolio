package be.distribusi.stok.barang.exception;

import be.distribusi.stok.barang.dto.insert.ResErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ResErrorDTO responseErrorDTO = new ResErrorDTO();
        // Buat objek LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        // Format LocalDateTime menjadi String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        String loggerId = formattedDateTime;

        // Generate or obtain loggerId dynamically
        responseErrorDTO.setLoggerId(loggerId);
        responseErrorDTO.setStatusCode("400");
        responseErrorDTO.setStatusMsg("FAILED");

        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            //String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMessage.append(message).append(", ");
        });

        responseErrorDTO.setErrorMessage(errorMessage.toString().trim());

        return new ResponseEntity<>(responseErrorDTO, HttpStatus.BAD_REQUEST);
    }
}
