package be.distribusi.stok.barang.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
    private int hargaBeli;

    @Column(name = "harga_jual")
    private int hargaJual;

    @Column(name = "stok_awal")
    private int stokAwal;

    @Column(name = "stok_masuk")
    private int stokMasuk;

    @Column(name = "stok_keluar")
    private int stokKeluar;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
}
