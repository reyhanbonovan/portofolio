package be.distribusi.stok.barang.dto.insert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ReqInsertDTO {

    @JsonProperty("kode_barang")
    private String kodeBarang;

    @JsonProperty("nama_barang")
    private String namaBarang;

    @JsonProperty("harga_beli")
    private String hargaBeli;

    @JsonProperty("harga_jual")
    private String hargaJual;

    @JsonProperty("stok_masuk")
    private String stokMasuk;
}
