package be.distribusi.stok.barang.dto.order;

import be.distribusi.stok.barang.dto.insert.ResInsertDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResOrderDTO {

    @JsonProperty("data")
    private orderDataDTO data;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("status_msg")
    private String statusMsg;

    @JsonProperty("error_message")
    private String errorMessage;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class orderDataDTO{
        @JsonProperty("logger_id")
        private String loggerId;

        @JsonProperty("kode_barang")
        private String kodeBarang;

        @JsonProperty("nama_barang")
        private String namaBarang;

        @JsonProperty("harga_jual")
        private String hargaJual;

        @JsonProperty("total_pesanan")
        private String totalPesanan;

        @JsonProperty("total_harga")
        private String totalHarga;
    }
}
