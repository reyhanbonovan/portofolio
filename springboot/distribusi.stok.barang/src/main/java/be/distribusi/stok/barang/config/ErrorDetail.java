package be.distribusi.stok.barang.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class ErrorDetail {
    private String statusCode;
    private String errorMessage;
}
