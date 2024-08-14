package be.distribusi.stok.barang.service.order;

import be.distribusi.stok.barang.dto.order.ReqOrderDTO;
import be.distribusi.stok.barang.dto.order.ResOrderDTO;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final ErrorRepository errorConfig;
    private final BarangRepository barangRepository;
    //Order Barang Service
    public ResponseEntity<?> orderBarangService(@Valid ReqOrderDTO requestBody){
        // Buat objek LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        // Format LocalDateTime menjadi String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String loggerId = now.format(formatter);

        String kodeBarang = requestBody.getKodeBarang().toUpperCase();
        String namaBarang = requestBody.getNamaBarang().toUpperCase();
        Integer banyakPesanan = Integer.parseInt(requestBody.getBanyakPesanan());
        //Get data Barang
        List<EntityBarang> barangList = barangRepository.findBarangbyCodeName(kodeBarang, namaBarang);
        //Cek Barang is Exist
        if (barangList.isEmpty()){
            log.error("{} - Barang tidak ditemukan", loggerId);
            String statusCode = errorConfig.getErrorCode("item_nfound");
            String errorMessage = errorConfig.getErrorMessage("item_nfound");
            throw new AppException(statusCode, errorMessage, HttpStatus.BAD_REQUEST, loggerId);
        }
        EntityBarang barang = barangList.get(0);
        //Update srok Keluar dan sisa stok
        Integer stokKeluarTemp = (barang.getStokKeluar() == null || barang.getStokKeluar().equals("")) ? 0 : barang.getStokKeluar();
        int sisaStok = (barang.getSisaStok() - banyakPesanan);
        int stokKeluar = stokKeluarTemp + banyakPesanan;
        barangRepository.updateOrderBarang(sisaStok, stokKeluar, kodeBarang, namaBarang);
        // Hitung Harga Pesanan
        BigDecimal banyakPesananDecimal = BigDecimal.valueOf(banyakPesanan);
        BigDecimal totalHarga = barang.getHargaJual().multiply(banyakPesananDecimal);
//        Integer totalHarga = (banyakPesanan * barang.getHargaJual());

        // Mapping request ke responseDTO
        ResOrderDTO.orderDataDTO orderDataDTO = new ResOrderDTO.orderDataDTO();
        // Set data ke responseDTO
        orderDataDTO.setLoggerId(loggerId);
        orderDataDTO.setKodeBarang(kodeBarang);
        orderDataDTO.setNamaBarang(namaBarang);
        orderDataDTO.setHargaJual(String.valueOf(barang.getHargaJual()));
        orderDataDTO.setTotalPesanan(String.valueOf(banyakPesanan));
        orderDataDTO.setTotalHarga(totalHarga.toString());

        ResOrderDTO responseDTO = new ResOrderDTO();
        responseDTO.setData(orderDataDTO);
        responseDTO.setStatusCode("200");
        responseDTO.setStatusMsg("SUCCESS");
        responseDTO.setErrorMessage("");

        log.info("{} - End-to-End processing complete.", loggerId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
