import os
from dotenv import load_dotenv
load_dotenv()

class Config :
    app_host = os.getenv("APP_HOST")
    app_port = int(os.getenv("APP_PORT"))
    app_worker = int(os.getenv("APP_WORKER"))

    db_host = os.getenv("DB_HOST")
    db_port = os.getenv("DB_PORT")
    db_name = os.getenv("DB_NAME")
    db_user = os.getenv("DB_USER")
    db_pass = os.getenv("DB_PASS")

    param_success="SUCCESS"
    param_failed="FAILED"
    param_error = {
        "code_exist": ["01", "KODE BARANG SUDAH ADA, GAGAL INPUT KE DATABASE"],
        "item_nfound": ["02", "BARANG TIDAK DITEMUKAN"],
        "general_exception": ["99", "GENERAL EXCEPTION, INTERNAL SERVER ERROR"]
    }
    