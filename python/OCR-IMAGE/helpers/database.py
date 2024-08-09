import cx_Oracle as oraclelib
from helpers.config import db_host, db_port, db_sid, db_user, db_pass, db_pool_min, db_pool_max, db_pool_increment, db_encode, param_error
from helpers.exception import DatabaseException

try:
    # dsn_tns = oraclelib.makedsn(db_host, db_port, service_name=db_sid)
    # cp_pool = oraclelib.SessionPool(
    #     user=db_user, 
    #     password=db_pass, 
    #     dsn=dsn_tns,
    #     min=db_pool_min, 
    #     max=db_pool_max, 
    #     increment=db_pool_increment,
    #     encoding=db_encode,
    #     threaded=True
    #     )
    print("poolConnection")
except Exception as e:
    error_code = param_error["database_error"][0]
    error_desc = param_error["database_error"][1]
    raise DatabaseException(message=error_desc, origin="DATABASE", errorCode=error_code, errorMessage=error_desc, errorDetail="Syserr: {0}".format(e))
    
