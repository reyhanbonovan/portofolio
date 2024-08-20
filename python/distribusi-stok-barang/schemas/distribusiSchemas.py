from pydantic import BaseModel
from typing import Optional

class ReqInsertDTO(BaseModel):
    kode_barang: str 
    nama_barang: str 
    harga_beli: str 
    harga_jual: str 
    stok_masuk: str 

class ReqSelectDTO(BaseModel):
    kode_barang: str 
    nama_barang: str

class ReqAddDTO(BaseModel):
    kode_barang: str 
    nama_barang: str
    stok_masuk: str

class ReqOrderDTO(BaseModel):
    kode_barang: str 
    nama_barang: str
    banyak_pesanan: str  

class ReqDeleteDTO(BaseModel):
    kode_barang: str 
    nama_barang: str

class ResponseDTO(BaseModel):
    data: Optional[dict] = None
    status_code: Optional[str] = None
    status_msg: Optional[str] = None
    error_message: Optional[str] = None


class ResErrAppModel(BaseModel):
    logger_id: Optional[str] = None
    status_msg: Optional[str] = None
    status_code: str = None
    error_message: str = None
