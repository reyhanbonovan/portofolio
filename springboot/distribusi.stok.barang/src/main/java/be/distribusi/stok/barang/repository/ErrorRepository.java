package be.distribusi.stok.barang.repository;

import be.distribusi.stok.barang.config.ErrorDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Repository
public class ErrorRepository {

    private final Map<String, ErrorDetail> paramError;

    public ErrorRepository() {
        paramError = new HashMap<>();
        paramError.put("code_exist", new ErrorDetail("01", "KODE BARANG SUDAH ADA, GAGAL INPUT KE DATABASE"));
        paramError.put("item_nfound", new ErrorDetail("02", "BARANG TIDAK DITEMUKAN"));
        paramError.put("general_exception", new ErrorDetail("99", "GENERAL EXCEPTION, INTERNAL SERVER ERROR"));
    }

    public String getErrorCode(String key) {
        ErrorDetail errorDetail = paramError.get(key);
        return (errorDetail != null) ? errorDetail.getStatusCode() : "0000";
    }

    public String getErrorMessage(String key) {
        ErrorDetail errorDetail = paramError.get(key);
        return (errorDetail != null) ? errorDetail.getErrorMessage() : "Unknown error";
    }
}
