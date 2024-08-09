from pydantic import BaseModel
from typing import Optional

class OCRRequest(BaseModel):
    akun: Optional[str] = None
    idRefferal: Optional[str] = None
    image: Optional[str] = None
    
class OCRRes(BaseModel):
    akun: Optional[str] = None
    idRefferal: Optional[str] = None
    bizId: Optional[str] = None
    ocr: Optional[dict] = None
    status_msg: Optional[str] = None
    errorCode: Optional[str] = None
    errorMessage: Optional[str] = None