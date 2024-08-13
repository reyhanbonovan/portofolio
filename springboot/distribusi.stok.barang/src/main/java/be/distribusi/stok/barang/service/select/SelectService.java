package be.distribusi.stok.barang.service.select;

import be.distribusi.stok.barang.dto.select.ReqSelectDTO;
import be.distribusi.stok.barang.dto.select.ResSelectDTO;
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
public class SelectService {
    private final ErrorRepository errorConfig;
    private final BarangRepository barangRepository; // Tambahkan repository

    public ResponseEntity<?> selectBarangService(@Valid ReqSelectDTO requestBody) {
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
        EntityBarang barang = barangList.get(0);

        ResSelectDTO.selectDataDTO selectDataDTO = new ResSelectDTO.selectDataDTO();
        selectDataDTO.setLoggerId(loggerId);
        selectDataDTO.setKodeBarang(barang.getKodeBarang());
        selectDataDTO.setNamaBarang(barang.getNamaBarang());
        selectDataDTO.setHargaBeli(String.valueOf(barang.getHargaBeli()));
        selectDataDTO.setHargaJual(String.valueOf(barang.getHargaJual()));
        selectDataDTO.setSisaStok(String.valueOf(barang.getSisaStok()));
        selectDataDTO.setStokMasuk(String.valueOf(barang.getStokMasuk()));
        selectDataDTO.setStokKeluar(String.valueOf(barang.getStokKeluar()));
        selectDataDTO.setCreatedAt(String.valueOf(barang.getCreatedAt()));
        selectDataDTO.setModifiedAt(String.valueOf(barang.getModifiedAt()));

        ResSelectDTO responseDTO = new ResSelectDTO();
        responseDTO.setData(selectDataDTO);
        responseDTO.setStatusCode("200");
        responseDTO.setStatusMsg("SUCCESS");
        responseDTO.setErrorMessage("");
        log.info("{} - End-to-End processing complete.", loggerId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);

        }
}
