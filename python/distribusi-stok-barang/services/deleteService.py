from datetime import datetime
from decimal import Decimal
from models.databaseModels import FindBarangbyCodeName, DeleteBarangByCodeName
from helpers.config import Config as config
from exception.appException import AppException
from loguru import logger as log
import json

def DeleteDistribusiBarang(req, db):
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
    
    DeleteBarangByCodeName(db, kodeBarang, namaBarang)

    data = {
        "logger_id" : loggerId,
        "status_data": f"Data dengan kode_barang = {kodeBarang}, nama_barang = {namaBarang} berhasil dihapus"
    }
    result["data"] = data
    result["status_code"] = "200"
    result["status_msg"] = config.param_success
    result["error_msg"] = ""
    log.info(f"{loggerId} - End-to-End processing complete. Result: \n{json.dumps(dict(result))}")
    return result