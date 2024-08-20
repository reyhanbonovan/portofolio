from datetime import datetime
from decimal import Decimal
from models.databaseModels import FindBarangbyCodeName, UpdateAddBarang
from helpers.config import Config as config
from exception.appException import AppException
from loguru import logger as log
import json

def AddDistribusiBarang(req, db):
    result = {
        "data":{},
        "status_code": "",
        "status_msg": "",
        "error_msg": "",
    }
    loggerId = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    log.info(f"{loggerId} - Start to processing add service. Request: \n{json.dumps(dict(req))}")

    kodeBarang = req.kode_barang.upper()
    namaBarang = req.nama_barang.upper()
    stokMasuk = int(req.stok_masuk)

    
    listBarang = FindBarangbyCodeName(db, kodeBarang, namaBarang)
    
    if not listBarang:
        log.error(f"{loggerId} - Barang tidak ditemukan")
        raise AppException(loggerId, config.param_error['item_nfound'][0], config.param_failed, config.param_error['item_nfound'][1])
    
    barang = listBarang[0]

    stokMasukTemp = stokMasuk
    sisaStok = (barang['sisa_stok'] + stokMasukTemp)
    stokMasuk = (barang['stok_masuk'] + stokMasukTemp)

    UpdateAddBarang(
        db,
        kodeBarang,
        namaBarang,
        stokMasuk,
        sisaStok
    )

    data = {
        "logger_id" : loggerId,
        "kode_barang": kodeBarang,
        "nama_barang": namaBarang,
        "harga_beli": str(barang['harga_beli']),
        "harga_jual": str(barang['harga_jual']),
        "sisa_stok": str(sisaStok),
        "stok_keluar": str(barang['stok_keluar'])
    }
    result["data"] = data
    result["status_code"] = "200"
    result["status_msg"] = config.param_success
    result["error_msg"] = ""
    log.info(f"{loggerId} - End-to-End processing complete. Result: \n{json.dumps(dict(result))}")
    return result


