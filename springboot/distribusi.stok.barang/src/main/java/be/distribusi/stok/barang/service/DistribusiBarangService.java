package be.distribusi.stok.barang.service;

import be.distribusi.stok.barang.dto.insert.ReqInsertDTO;
import be.distribusi.stok.barang.dto.insert.ResErrorDTO;
import be.distribusi.stok.barang.dto.insert.ResInsertDTO;
import be.distribusi.stok.barang.exception.AppException;
import be.distribusi.stok.barang.repository.BarangRepository; // Import repository
import be.distribusi.stok.barang.repository.ErrorRepository;
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
public class DistribusiBarangService {

    private final ErrorRepository errorConfig;
    private final BarangRepository barangRepository; // Tambahkan repository

    public ResponseEntity<?> insertBarangService(ReqInsertDTO requestBody) {
        // Buat objek LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        // Format LocalDateTime menjadi String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        String loggerId = formattedDateTime;

        try {
            String kodeBarang = requestBody.getKodeBarang();
            String namaBarang = requestBody.getNamaBarang();
            int hargaBeli = Integer.parseInt(requestBody.getHargaBeli());
            int hargaJual = Integer.parseInt(requestBody.getHargaJual());
            int stokMasuk = Integer.parseInt(requestBody.getStokMasuk());
            int stokAwal = stokMasuk;
            int stokKeluar = 0;

            // Cek apakah kode_barang sudah ada
            int isExist = barangRepository.selectByKode(kodeBarang);
            if (isExist == 0) {
                // Lakukan insert barang
                barangRepository.insertStokBarang(kodeBarang, namaBarang, hargaBeli, hargaJual, stokAwal, stokMasuk);

                // Mapping request ke responseDTO
                ResInsertDTO.insertDataDTO dataDTO = new ResInsertDTO.insertDataDTO();
                // Set data ke responseDTO
                dataDTO.setLoggerId(loggerId);
                dataDTO.setKodeBarang(kodeBarang);
                dataDTO.setNamaBarang(namaBarang);
                dataDTO.setHargaBeli(String.valueOf(hargaBeli));
                dataDTO.setHargaJual(String.valueOf(hargaJual));
                dataDTO.setStokAwal(String.valueOf(stokAwal));
                dataDTO.setStokMasuk(String.valueOf(stokMasuk));
                dataDTO.setStokKeluar(String.valueOf(stokKeluar));

                ResInsertDTO responseDTO = new ResInsertDTO();
                responseDTO.setData(dataDTO);
                responseDTO.setStatusCode("200");
                responseDTO.setStatusMsg("SUCCESS");
                responseDTO.setErrorMessage("");

                // Return successful response
                return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
            } else {
                // Kode barang sudah ada
                log.error("{} - KODE BARANG telah terdaftar", loggerId);
                String statusCode = errorConfig.getErrorCode("code_exist");
                String errorMessage = errorConfig.getErrorMessage("code_exist");
                throw new AppException(statusCode, errorMessage, HttpStatus.BAD_REQUEST);
            }
        } catch (AppException e) {
            log.error("{} - AppException occurred: {} - {}", loggerId, e.getStatusCode(), e.getErrorMessage());
            ResErrorDTO responseErrorDTO = new ResErrorDTO();
            responseErrorDTO.setLoggerId(loggerId);
            responseErrorDTO.setStatusMsg("FAILED");
            responseErrorDTO.setStatusCode(e.getStatusCode());
            responseErrorDTO.setErrorMessage(e.getErrorMessage());
            // Return error response
            return ResponseEntity.status(e.getHttpStatus()).body(responseErrorDTO);
        } catch (Exception e) {
            log.error("{} - An unexpected error occurred. {}", loggerId, e.getMessage());
            // Set data ke responseErrorDTO
            ResErrorDTO responseErrorDTO = new ResErrorDTO();
            responseErrorDTO.setLoggerId(loggerId);
            responseErrorDTO.setStatusMsg("FAILED");
            responseErrorDTO.setStatusCode(errorConfig.getErrorCode("general_exception"));
            responseErrorDTO.setErrorMessage(errorConfig.getErrorMessage("general_exception"));
            // Return error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseErrorDTO);
        } finally {
            // Logging final process
            log.info("{} - End-to-End processing complete.", loggerId);
        }
    }
}
