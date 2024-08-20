from datetime import datetime
from decimal import Decimal
from models.databaseModels import FindBarangbyCodeName
from helpers.config import Config as config
from exception.appException import AppException
from loguru import logger as log
import json

def SelectDistribusiBarang(req, db):
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

    listBarang = FindBarangbyCodeName(db, kodeBarang, namaBarang)
    
    if not listBarang:
        log.error(f"{loggerId} - Barang tidak ditemukan")
        raise AppException(loggerId, config.param_error['item_nfound'][0], config.param_failed, config.param_error['item_nfound'][1])

    barang = listBarang[0]

    data = {
        "logger_id" : loggerId,
        "kode_barang": barang['kode_barang'],
        "nama_barang": barang['nama_barang'],
        "harga_beli": str(barang['harga_beli']),
        "harga_jual": str(barang['harga_jual']),
        "sisa_stok": str(barang['sisa_stok']),
        "stok_masuk": str(barang['stok_masuk']),
        "stok_keluar": str(barang['stok_keluar']),
        "created_at": str(barang['created_at']),
        "modified_at": str(barang['modified_at'])
    }

    result["data"] = data
    result["status_code"] = "200"
    result["status_msg"] = config.param_success
    result["error_msg"] = ""
    log.info(f"{loggerId} - End-to-End processing complete. Result: \n{json.dumps(dict(result))}")
    return result