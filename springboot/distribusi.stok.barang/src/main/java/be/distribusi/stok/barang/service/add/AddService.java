package be.distribusi.stok.barang.service.add;

import be.distribusi.stok.barang.dto.add.ReqAddDTO;
import be.distribusi.stok.barang.dto.add.ResAddDTO;
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
public class AddService {
    private final ErrorRepository errorConfig;
    private final BarangRepository barangRepository;

    // Add Barang Service
    public ResponseEntity<?> addBarangService(@Valid ReqAddDTO requestBody){
        // Buat objek LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        // Format LocalDateTime menjadi String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String loggerId = now.format(formatter);

        String kodeBarang = requestBody.getKodeBarang().toUpperCase();
        String namaBarang = requestBody.getNamaBarang().toUpperCase();
        Integer stokMasuk = Integer.parseInt(requestBody.getStokMasuk());
        //Get data Barang
        List<EntityBarang> barangList = barangRepository.findBarangbyCodeName(kodeBarang, namaBarang);
        //Cek Barang is Exist
        if (barangList.isEmpty()){
            log.error("{} - Barang tidak ditemukan", loggerId);
            String statusCode = errorConfig.getErrorCode("item_nfound");
            String errorMessage = errorConfig.getErrorMessage("item_nfound");
            throw new AppException(statusCode, errorMessage, HttpStatus.NOT_FOUND, loggerId);
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
        log.info("{} - End-to-End processing complete.", loggerId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
