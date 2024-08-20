from datetime import datetime
from decimal import Decimal
from models.databaseModels import FindBarangbyCodeName, UpdateOrderBarang
from helpers.config import Config as config
from exception.appException import AppException
from loguru import logger as log
import json

def OrderDistribusiBarang(req, db):
    result = {
        "data":{},
        "status_code": "",
        "status_msg": "",
        "error_msg": "",
    }
    loggerId = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    log.info(f"{loggerId} - Start to processing select service. Request: \n{json.dumps(dict(req))}")

    kodeBarang = req.kode_barang.upper()
    namaBarang = req.nama_barang.upper()
    banyakPesanan = int(req.banyak_pesanan)

    listBarang = FindBarangbyCodeName(db, kodeBarang, namaBarang)
    
    if not listBarang:
        log.error(f"{loggerId} - Barang tidak ditemukan")
        raise AppException(loggerId, config.param_error['item_nfound'][0], config.param_failed, config.param_error['item_nfound'][1])
    
    barang = listBarang[0]
    
    stokKeluarTemp = 0 if barang['stok_keluar'] in [None, ''] else barang['stok_keluar'] 

    sisaStok = (barang['sisa_stok'] - banyakPesanan)
    stokKeluar = (stokKeluarTemp + banyakPesanan)

    UpdateOrderBarang(
        db,
        kodeBarang,
        namaBarang,
        sisaStok,
        stokKeluar
    )
    banyakPesananDecimal = Decimal(banyakPesanan)
    totalHarga = (barang['harga_jual'] * banyakPesananDecimal)
    
    data = {
        "logger_id" : loggerId,
        "kode_barang": kodeBarang,
        "nama_barang": namaBarang,
        "harga_jual": str(barang['harga_jual']),
        "total_pesanan": str(banyakPesanan),
        "total_harga": str(totalHarga)
    }
    result["data"] = data
    result["status_code"] = "200"
    result["status_msg"] = config.param_success
    result["error_msg"] = ""
    log.info(f"{loggerId} - End-to-End processing complete. Result: \n{json.dumps(dict(result))}")
    return result

    
