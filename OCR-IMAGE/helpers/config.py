import os

app_host = os.getenv('APP_HOST')
app_port = int(os.getenv('APP_PORT'))
app_worker = int(os.getenv('APP_WORKERS'))
be_timeout  = int(os.getenv('PARAM_TIMEOUT'))

param_ocr_type = os.getenv("PARAM_USETYPE")
endpoint_ocr_use = os.getenv("ENDPOINT_OCR1")
path_ms = os.getenv("PATH_1")
endpoint_ocr_use2 = os.getenv("ENDPOINT_OCR2")
path_ms2 = os.getenv("PATH2")

param_success="SUCCESS"
param_failed="FAILED"
param_error = {
    "ocr": ["01", "KTP EXTRACTION FAILED"], 
    "timeout": ["02", "TIMEOUT"],
    "database_error": ["03", "DATABASE ERROR"],
    "general_exception": ["04", "GENERAL ERROR"],
    "syserr": ["05", "SYSTEM ERROR"],
    "connection": ["06", "CONECTION ERROR"],
    "timeout": ["99", "TIMEOUT ERROR"]
}

db_host = os.getenv('DB_HOST')
db_port = os.getenv('DB_PORT')
db_sid = os.getenv('DB_SID')
db_user = os.getenv('DB_USER')
db_pass = os.getenv('DB_PASS')
db_encode = os.getenv('DB_ENCODE')
db_pool_min = int(os.getenv('DB_POOL_MIN'))
db_pool_max = int(os.getenv('DB_POOL_MAX'))
db_pool_increment = int(os.getenv('DB_POOL_INCREMENT'))