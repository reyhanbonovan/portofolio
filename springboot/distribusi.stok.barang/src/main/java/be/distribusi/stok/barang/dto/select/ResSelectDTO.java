package be.distribusi.stok.barang.dto.select;

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
public class ResSelectDTO {
    @JsonProperty("data")
    private ResSelectDTO.selectDataDTO data;

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
    public static class selectDataDTO {
        @JsonProperty("logger_id")
        private String loggerId;

        @JsonProperty("kode_barang")
        private String kodeBarang;

        @JsonProperty("nama_barang")
        private String namaBarang;

        @JsonProperty("harga_beli")
        private String hargaBeli;

        @JsonProperty("harga_jual")
        private String hargaJual;

        @JsonProperty("sisa_stok")
        private String sisaStok;

        @JsonProperty("stok_masuk")
        private String stokMasuk;

        @JsonProperty("stok_keluar")
        private String stokKeluar;

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("modified_at")
        private String modifiedAt;


    }
}
