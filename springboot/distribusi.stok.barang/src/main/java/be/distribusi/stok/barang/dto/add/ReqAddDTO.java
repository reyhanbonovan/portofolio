package be.distribusi.stok.barang.dto.add;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqAddDTO {

    @JsonProperty("kode_barang")
    @NotBlank(message = "kode_barang cannot be null")
    private String kodeBarang;

    @JsonProperty("nama_barang")
    @NotBlank(message = "nama_barang cannot be null")
    private String namaBarang;

    @JsonProperty("stok_masuk")
    @NotBlank(message = "stok_masuk cannot be null")
    private String stokMasuk;
}
