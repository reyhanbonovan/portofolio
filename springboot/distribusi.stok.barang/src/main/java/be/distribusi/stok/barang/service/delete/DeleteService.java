package be.distribusi.stok.barang.service.delete;

import be.distribusi.stok.barang.dto.delete.ReqDeleteDTO;
import be.distribusi.stok.barang.dto.delete.ResDeleteDTO;
import be.distribusi.stok.barang.exception.AppException;
import be.distribusi.stok.barang.model.EntityBarang;
import be.distribusi.stok.barang.repository.BarangRepository;
import be.distribusi.stok.barang.repository.ErrorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteService {
    private final ErrorRepository errorConfig;
    private final BarangRepository barangRepository;

    //Delete Barang Service
    public ResponseEntity<?> deleteBarangService(@Valid ReqDeleteDTO requestBody) {
        // Buat objek LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        // Format LocalDateTime menjadi String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String loggerId = now.format(formatter);

        String kodeBarang = requestBody.getKodeBarang().toUpperCase();
        String namaBarang = requestBody.getNamaBarang().toUpperCase();

        List<EntityBarang> barangList = barangRepository.findBarangbyCodeName(kodeBarang, namaBarang);
        //Cek Barang is Exist
        if (barangList.isEmpty()){
            log.error("{} - Barang tidak ditemukan", loggerId);
            String statusCode = errorConfig.getErrorCode("item_nfound");
            String errorMessage = errorConfig.getErrorMessage("item_nfound");
            throw new AppException(statusCode, errorMessage, HttpStatus.NOT_FOUND, loggerId);
        }
        barangRepository.deleteBarangByCodeName(kodeBarang, namaBarang);

        // Mapping request ke responseDTO
        ResDeleteDTO.deleteDataDTO deleteDataDTO = new ResDeleteDTO.deleteDataDTO();
        // Set data ke responseDTO
        deleteDataDTO.setLoggerId(loggerId);
        deleteDataDTO.setStatusData(String.format("Data dengan kode_barang = %s, nama_barang = %s berhasil dihapus", kodeBarang, namaBarang));

        ResDeleteDTO responseDTO = new ResDeleteDTO();
        responseDTO.setData(deleteDataDTO);
        responseDTO.setStatusCode("200");
        responseDTO.setStatusMsg("SUCCESS");
        responseDTO.setErrorMessage("");

        log.info("{} - End-to-End processing complete.", loggerId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
