package be.distribusi.stok.barang.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqOrderDTO {
    @JsonProperty("kode_barang")
    @NotBlank(message = "kode_barang cannot be null")
    private String kodeBarang;

    @JsonProperty("nama_barang")
    @NotBlank(message = "nama_barang cannot be null")
    private String namaBarang;

    @JsonProperty("banyak_pesanan")
    @NotBlank(message = "banyak_pesanan cannot be null")
    private String banyakPesanan;
}
