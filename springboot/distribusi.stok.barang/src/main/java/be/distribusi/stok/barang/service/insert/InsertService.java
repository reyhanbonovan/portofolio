package be.distribusi.stok.barang.service.insert;

import be.distribusi.stok.barang.dto.insert.ReqInsertDTO;
import be.distribusi.stok.barang.dto.insert.ResInsertDTO;
import be.distribusi.stok.barang.exception.AppException;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class InsertService {
    private final ErrorRepository errorConfig;
    private final BarangRepository barangRepository;

    public ResponseEntity<?> insertBarangService(@Valid ReqInsertDTO requestBody){
        // Buat objek LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        // Format LocalDateTime menjadi String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String loggerId = now.format(formatter);

        String kodeBarang = requestBody.getKodeBarang().toUpperCase();
        String namaBarang = requestBody.getNamaBarang().toUpperCase();
        Integer hargaBeli = Integer.parseInt(requestBody.getHargaBeli());
        Integer hargaJual = Integer.parseInt(requestBody.getHargaJual());
        Integer stokMasuk = Integer.parseInt(requestBody.getStokMasuk());
        Integer sisaStok = Integer.parseInt(requestBody.getStokMasuk());
        Integer stokKeluar = 0;

        // Cek apakah kode_barang sudah ada
        int isExist = barangRepository.selectByKode(kodeBarang);
        if (isExist == 0) {
            // Lakukan insert barang
            barangRepository.insertStokBarang(kodeBarang, namaBarang, hargaBeli, hargaJual, sisaStok, stokMasuk);

            // Mapping request ke responseDTO
            ResInsertDTO.insertDataDTO insertDataDTO = new ResInsertDTO.insertDataDTO();
            // Set data ke responseDTO
            insertDataDTO.setLoggerId(loggerId);
            insertDataDTO.setKodeBarang(kodeBarang);
            insertDataDTO.setNamaBarang(namaBarang);
            insertDataDTO.setHargaBeli(String.valueOf(hargaBeli));
            insertDataDTO.setHargaJual(String.valueOf(hargaJual));
            insertDataDTO.setSisaStok(String.valueOf(sisaStok));
            insertDataDTO.setStokMasuk(String.valueOf(stokMasuk));
            insertDataDTO.setStokKeluar(String.valueOf(stokKeluar));

            ResInsertDTO responseDTO = new ResInsertDTO();
            responseDTO.setData(insertDataDTO);
            responseDTO.setStatusCode("200");
            responseDTO.setStatusMsg("SUCCESS");
            responseDTO.setErrorMessage("");
            log.info("{} - End-to-End processing complete.", loggerId);
            // Return successful response
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } else {
            // Kode barang sudah ada
            log.error("{} - KODE BARANG telah terdaftar", loggerId);
            String statusCode = errorConfig.getErrorCode("code_exist");
            String errorMessage = errorConfig.getErrorMessage("code_exist");
            throw new AppException(statusCode, errorMessage, HttpStatus.BAD_REQUEST, loggerId);
        }
    }
}
