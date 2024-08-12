package be.distribusi.stok.barang.dto.select;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqSelectDTO {
    @JsonProperty("kode_barang")
    @NotBlank(message = "kode_barang cannot be null")
    private String kodeBarang;

    @JsonProperty("nama_barang")
    @NotBlank(message = "nama_barang cannot be null")
    private String namaBarang;
}
