from sqlalchemy import text
from sqlalchemy.orm import Session
from decimal import Decimal

#Query Insert Barang
def InsertStokBarang(db: Session, kodeBarang: str, namaBarang: str, hargaBeli: Decimal, hargaJual: Decimal, sisaStok: int, stokMasuk: int, stokKeluar: int):
    statement = text("""
        INSERT INTO stok_barang (kode_barang, nama_barang, harga_beli, harga_jual, sisa_stok, stok_masuk, stok_keluar, created_at) 
        VALUES (:kodeBarang, :namaBarang, :hargaBeli, :hargaJual, :sisaStok, :stokMasuk, :stokKeluar, NOW())
    """)
    db.execute(statement, {
        'kodeBarang' : kodeBarang, 
        'namaBarang' : namaBarang,
        'hargaBeli' : hargaBeli,
        'hargaJual' : hargaJual,
        'sisaStok' : sisaStok,
        'stokMasuk' : stokMasuk,
        'stokKeluar' : stokKeluar
    })
    db.commit()

#Query Select Barang by code
def SelectByCode(db: Session, kodeBarang: str) -> bool:
    statement = text("""
        SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM stok_barang WHERE kode_barang = :kodeBarang
    """)
    result = db.execute(statement, {
        'kodeBarang': kodeBarang
    })
    count = result.scalar()  # Mendapatkan hasil scalar (angka)
    return count == 1

#Query Select  All Data Barang
def FindBarangbyCodeName(db: Session, kodeBarang: str, namaBarang: str):
    statement = text("""
        SELECT * FROM stok_barang WHERE (kode_barang = :kodeBarang AND nama_barang = :namaBarang)
    """)
    result = db.execute(statement, {
        'kodeBarang' : kodeBarang,
        'namaBarang' : namaBarang
    })
    return result.fetchall()

#Query Update Add Barang
def UpdateAddBarang(db:Session, kodeBarang: str, namaBarang: str, stokMasuk: int, sisaStok: int):
    statement = text("""
        UPDATE stok_barang 
        SET sisa_stok = :sisaStok, stok_masuk = :stokMasuk, modified_at = NOW() 
        WHERE kode_barang = :kodeBarang AND nama_barang = :namaBarang
    """)
    db.execute(statement, {
        'sisaStok' : sisaStok,
        'stokMasuk' : stokMasuk,
        'kodeBarang' : kodeBarang, 
        'namaBarang' : namaBarang
    })
    db.commit()

#Query Update Order Barang
def UpdateOrderBarang(db:Session, kodeBarang: str, namaBarang: str, sisaStok: int, stokKeluar: int):
    statement = text("""
        UPDATE stok_barang 
        SET sisa_stok = :sisaStok, stok_keluar = :stokKeluar, modified_at = NOW() 
        WHERE kode_barang = :kodeBarang AND nama_barang = :namaBarang
    """)
    db.execute(statement, {
        'sisaStok' : sisaStok,
        'stokKeluar' : stokKeluar,
        'kodeBarang' : kodeBarang, 
        'namaBarang' : namaBarang
    })
    db.commit()

# Query Delete Barang
def DeleteBarangByCodeName(db: Session, kodeBarang: str, namaBarang: str):
    statement = text("""
        DELETE FROM stok_barang 
        WHERE kode_barang = :kodeBarang AND nama_barang = :namaBarang
    """)
    db.execute(statement, {
        'kodeBarang': kodeBarang,
        'namaBarang': namaBarang
    })
    db.commit()

