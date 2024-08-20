from datetime import datetime
from decimal import Decimal
from models.databaseModels import SelectByCode, InsertStokBarang
from helpers.config import Config as config
from exception.appException import AppException
from loguru import logger as log
import json

def InsertDistribusiBarang(req, db):
    result = {
        "data":{},
        "status_code": "",
        "status_msg": "",
        "error_msg": "",
    }
    loggerId = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    log.info(f"{loggerId} - Start to processing insert service. Request: \n{json.dumps(dict(req))}")

    kodeBarang = req.kode_barang.upper()
    namaBarang = req.nama_barang.upper()
    hargaBeli = Decimal(req.harga_beli)
    hargaJual = Decimal(req.harga_jual)
    stokMasuk = int(req.stok_masuk)
    sisaStok = stokMasuk
    stokKeluar = 0

    isExist = SelectByCode(db, kodeBarang)

    if (isExist == True):
        log.info(f"{loggerId} - KODE BARANG telah terdaftar")
        raise AppException(loggerId, config.param_error['code_exist'][0], config.param_failed, config.param_error['code_exist'][1])
    
    InsertStokBarang(
        db,
        kodeBarang,
        namaBarang,
        hargaBeli,
        hargaJual,
        stokMasuk,
        sisaStok,
        stokKeluar
    ) 
    
    data = {
        "logger_id" : loggerId,
        "kode_barang": kodeBarang,
        "nama_barang": namaBarang,
        "harga_beli": str(hargaBeli),
        "harga_jual": str(hargaJual),
        "sisa_stok": str(sisaStok),
        "stok_keluar": str(stokKeluar)
    }
    result["data"] = data
    result["status_code"] = "200"
    result["status_msg"] = config.param_success
    result["error_msg"] = ""
    log.info(f"{loggerId} - End-to-End processing complete. Result: \n{json.dumps(dict(result))}")
    return result
        

    

