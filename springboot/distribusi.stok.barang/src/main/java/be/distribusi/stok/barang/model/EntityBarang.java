package be.distribusi.stok.barang.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stok_barang")
@Getter
@Setter
public class EntityBarang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "kode_barang")
    private String kodeBarang;

    @Column(name = "nama_barang")
    private String namaBarang;

    @Column(name = "harga_beli")
    private BigDecimal hargaBeli;  // Menggunakan BigDecimal

    @Column(name = "harga_jual")
    private BigDecimal hargaJual;  // Menggunakan BigDecimal

    @Column(name = "sisa_stok")
    private Integer sisaStok;

    @Column(name = "stok_masuk")
    private Integer stokMasuk;

    @Column(name = "stok_keluar")
    private Integer stokKeluar;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
}