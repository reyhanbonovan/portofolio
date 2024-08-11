package be.distribusi.stok.barang.dto.insert;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqInsertDTO {


    @JsonProperty("kode_barang")
    @NotBlank(message = "kode_barang cannot be null")
    private String kodeBarang;

    @JsonProperty("nama_barang")
    @NotBlank(message = "nama_barang cannot be null")
    private String namaBarang;

    @JsonProperty("harga_beli")
    @NotBlank(message = "harga_beli cannot be null")
    private String hargaBeli;

    @JsonProperty("harga_jual")
    @NotBlank(message = "harga_jual cannot be null")
    private String hargaJual;

    @JsonProperty("stok_masuk")
    @NotBlank(message = "stok_masuk cannot be null")
    private String stokMasuk;
}
