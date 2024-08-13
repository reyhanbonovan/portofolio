package be.distribusi.stok.barang.exception;

import be.distribusi.stok.barang.dto.ResErrorGeneralDTO;
import be.distribusi.stok.barang.dto.insert.ResErrorExceptionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResErrorGeneralDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ResErrorGeneralDTO responseErrorDTO = new ResErrorGeneralDTO();
        // Generate or obtain loggerId dynamically
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

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResErrorExceptionDTO> handleAppException(AppException ex) {
        ResErrorExceptionDTO responseErrorDTO = new ResErrorExceptionDTO();
        responseErrorDTO.setLoggerId(ex.getLoggerId());
        responseErrorDTO.setStatusMsg("FAILED");
        responseErrorDTO.setStatusCode(ex.getStatusCode());
        responseErrorDTO.setErrorMessage(ex.getErrorMessage());
        log.error("AppException occurred: {} - {}", ex.getStatusCode(), ex.getErrorMessage());
        return new ResponseEntity<>(responseErrorDTO, ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResErrorGeneralDTO> handleGeneralException(Exception ex) {
        ResErrorGeneralDTO responseErrorDTO = new ResErrorGeneralDTO();
        responseErrorDTO.setStatusMsg("FAILED");
        responseErrorDTO.setStatusCode("GENERAL_ERROR_CODE");
        responseErrorDTO.setErrorMessage("An unexpected error occurred.");
        log.error("An unexpected error occurred: {}", ex.getMessage());
        return new ResponseEntity<>(responseErrorDTO, HttpStatus.BAD_REQUEST);
    }
}
