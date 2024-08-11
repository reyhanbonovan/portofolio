package be.distribusi.stok.barang.service;

import be.distribusi.stok.barang.dto.add.ReqAddDTO;
import be.distribusi.stok.barang.dto.add.ResAddDTO;
import be.distribusi.stok.barang.dto.insert.ReqInsertDTO;
import be.distribusi.stok.barang.dto.insert.ResInsertDTO;
import be.distribusi.stok.barang.dto.insert.ResErrorDTO;
import be.distribusi.stok.barang.dto.order.ReqOrderDTO;
import be.distribusi.stok.barang.dto.order.ResOrderDTO;
import be.distribusi.stok.barang.exception.AppException;
import be.distribusi.stok.barang.model.EntityBarang;
import be.distribusi.stok.barang.repository.BarangRepository; // Import repository
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
public class DistribusiBarangService {

    private final ErrorRepository errorConfig;
    private final BarangRepository barangRepository; // Tambahkan repository

    // Insert Barang Service
    public ResponseEntity<?> insertBarangService(@Valid ReqInsertDTO requestBody) {
        // Buat objek LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        // Format LocalDateTime menjadi String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String loggerId = now.format(formatter);

        try {
            String kodeBarang = requestBody.getKodeBarang();
            String namaBarang = requestBody.getNamaBarang();
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

    // Add Barang Service
    public ResponseEntity<?> addBarangService(@Valid ReqAddDTO requestBody){
        // Buat objek LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        // Format LocalDateTime menjadi String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String loggerId = now.format(formatter);
        try {
            String kodeBarang = requestBody.getKodeBarang();
            String namaBarang = requestBody.getNamaBarang();
            Integer stokMasuk = Integer.parseInt(requestBody.getStokMasuk());
            //Get data Barang
            List<EntityBarang> barangList = barangRepository.findBarangbyCodeName(kodeBarang, namaBarang);
            //Cek Barang is Exist
            if (barangList.isEmpty()){
                log.error("{} - Barang tidak ditemukan", loggerId);
                String statusCode = errorConfig.getErrorCode("item_nfound");
                String errorMessage = errorConfig.getErrorMessage("item_nfound");
                throw new AppException(statusCode, errorMessage, HttpStatus.NOT_FOUND);
            }
            EntityBarang barang = barangList.get(0);
            Integer stokMasukTemp = stokMasuk;
            Integer sisaStok = (barang.getSisaStok()+stokMasukTemp);
            stokMasuk = (barang.getStokMasuk()+stokMasukTemp);
            barangRepository.updateAddBarang(sisaStok, stokMasuk, kodeBarang, namaBarang);

            // Mapping request ke responseDTO
            ResAddDTO.addDataDTO addDataDTO = new ResAddDTO.addDataDTO();
            // Set data ke responseDTO
            addDataDTO.setLoggerId(loggerId);
            addDataDTO.setKodeBarang(kodeBarang);
            addDataDTO.setNamaBarang(namaBarang);
            addDataDTO.setHargaBeli(String.valueOf(barang.getHargaBeli()));
            addDataDTO.setHargaJual(String.valueOf(barang.getHargaJual()));
            addDataDTO.setSisaStok(String.valueOf(sisaStok));
            addDataDTO.setStokMasuk(String.valueOf(stokMasuk));
            addDataDTO.setStokKeluar(String.valueOf(barang.getStokKeluar()));

            ResAddDTO responseDTO = new ResAddDTO();
            responseDTO.setData(addDataDTO);
            responseDTO.setStatusCode("200");
            responseDTO.setStatusMsg("SUCCESS");
            responseDTO.setErrorMessage("");

            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
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

    public ResponseEntity<?> orderBarangService(@Valid ReqOrderDTO requestBody) {
        // Buat objek LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        // Format LocalDateTime menjadi String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String loggerId = now.format(formatter);
        try {
            String kodeBarang = requestBody.getKodeBarang();
            String namaBarang = requestBody.getNamaBarang();
            Integer banyakPesanan = Integer.parseInt(requestBody.getBanyakPesanan());
            //Get data Barang
            List<EntityBarang> barangList = barangRepository.findBarangbyCodeName(kodeBarang, namaBarang);
            //Cek Barang is Exist
            if (barangList.isEmpty()){
                log.error("{} - Barang tidak ditemukan", loggerId);
                String statusCode = errorConfig.getErrorCode("item_nfound");
                String errorMessage = errorConfig.getErrorMessage("item_nfound");
                throw new AppException(statusCode, errorMessage, HttpStatus.NOT_FOUND);
            }
            EntityBarang barang = barangList.get(0);
            //Update srok Keluar dan sisa stok
            Integer stokKeluarTemp = (barang.getStokKeluar() == null || barang.getStokKeluar().equals("")) ? 0 : barang.getStokKeluar();
            int sisaStok = (barang.getSisaStok() - banyakPesanan);
            int stokKeluar = stokKeluarTemp + banyakPesanan;
            barangRepository.updateOrderBarang(sisaStok, stokKeluar, kodeBarang, namaBarang);
            // Hitung Harga Pesanan
            Integer totalHarga = (banyakPesanan * barang.getHargaJual());

            // Mapping request ke responseDTO
            ResOrderDTO.orderDataDTO orderDataDTO = new ResOrderDTO.orderDataDTO();
            // Set data ke responseDTO
            orderDataDTO.setLoggerId(loggerId);
            orderDataDTO.setKodeBarang(kodeBarang);
            orderDataDTO.setNamaBarang(namaBarang);
            orderDataDTO.setHargaJual(String.valueOf(barang.getHargaJual()));
            orderDataDTO.setTotalPesanan(String.valueOf(banyakPesanan));
            orderDataDTO.setTotalHarga(String.valueOf(totalHarga));

            ResOrderDTO responseDTO = new ResOrderDTO();
            responseDTO.setData(orderDataDTO);
            responseDTO.setStatusCode("200");
            responseDTO.setStatusMsg("SUCCESS");
            responseDTO.setErrorMessage("");

            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
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
