package be.distribusi.stok.barang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import  be.distribusi.stok.barang.model.EntityBarang;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BarangRepository extends JpaRepository<EntityBarang, Integer> {
    // Query insert stok barang
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO stok_barang (kode_barang, nama_barang, harga_beli, harga_jual, stok_awal, stok_masuk, created_at) VALUES (?1, ?2, ?3, ?4, ?5, ?6, CURRENT_TIMESTAMP)", nativeQuery = true)
    void insertStokBarang(String kodeBarang, String namaBarang, int hargaBeli, int hargaJual, int stokAwal, int stokMasuk);

    // Query select by kode barang
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM stok_barang WHERE kode_barang = ?1", nativeQuery = true)
    int selectByKode(String kodeBarang);
}
