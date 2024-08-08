from helpers.exception import DatabaseException
from loguru import logger

async def insertDb(
        akun,
        idRefferal,
        useType,
        bizId,
        errorCode,
        status_msg,
        errorDescString,
        idNo,
        nama,
        b_place,
        dob,
        gol_darah,
        prof,
        kota,
        kecamatan,
        desa,
        rt,
        rw,
        alamat,
        jenis_kelamin,
        agama,
        status,
        pekerjaan,
        berlaku,
        rawReq,
        rawRes,
        param_service_name,
        cp_pool
):
    
    try:
        errorCode = "200" if errorCode == "" or errorCode is None else errorCode
        connection = cp_pool.acquire() # request connection pool
        cursor = connection.cursor()
        statement = "INSERT INTO tabel_ocr (AKUN, ID_REFEFERAL, OCR_IDENTITY_TP, BIZ_ID, RESPONSE_CODE, RESPONSE_STATUS, RESPONSE_MESSAGE, ID_NO, IDENTITY_NM, IDENTITY_BIRTH_PLACE, IDENTITY_BIRTH_DATE, IDENTITY_BLOOD_TYPE, PROVINCE, KOTA, KEC, DESA, RW, RT, ALAMAT, JK, AGAMA, STATUS, PEKERJAAN, BERLAKU_HINGGA, API_RAW_REQ, API_RAW_RES, CREATED_AT, CREATED_BY) \
        VALUES (:akun, :idRefferal, :useType, :bizId, :errorCode, :status_msg, :errorDescString, :idNo, :nama, :b_place, :dob, :gol_darah, :prof, :kota, :kecamatan, :desa, :rw, :rt, :alamat, :jenis_kelamin, :agama, :status, :pekerjaan, :berlaku, :rawReq, :rawRes, CURRENT_TIMESTAMP, :param_service_name)" 
        cursor.execute(
            statement,
            {
                "akun" : akun, 
                "idRefferal" : idRefferal, 
                "useType" : useType, 
                "bizId" : bizId, 
                "errorCode" : errorCode, 
                "status_msg": status_msg,
                "errorDescString" : errorDescString, 
                "idNo" : idNo, 
                "nama" : nama, 
                "b_place" : b_place, 
                "dob" : dob, 
                "gol_darah" : gol_darah, 
                "prof" : prof, 
                "kota" : kota, 
                "kecamatan" : kecamatan, 
                "desa" : desa, 
                "rw" : rw, 
                "rt" : rt, 
                "alamat" : alamat, 
                "jenis_kelamin" : jenis_kelamin, 
                "agama" : agama, 
                "status" : status, 
                "pekerjaan" : pekerjaan, 
                "berlaku" : berlaku, 
                "rawReq" : rawReq, 
                "rawRes" : rawRes, 
                "param_service_name" : param_service_name
            },
        )
        connection.commit()
    except Exception as e:
        logger.info('{0} - Error happened when inserting data to Redis - {1}'.format(idRefferal, akun))
        logger.info('{0}'.format(e))
    finally:
        cursor.close()
        cp_pool.release(connection)
async def health_check(cp_pool):
    try:
        connection = cp_pool.acquire()
        cursor = connection.cursor()
        cur = cursor.execute("SELECT 1 FROM DUAL")
        rows = cur.fetchone()
        if rows:
            status_code = 200
        else:
            status_code = 500
    except Exception as e:
        logger.error(f"Exception Type: {type(e).__name__} - Syserr: {e}")
        status_code = 500
        raise DatabaseException(message="Database Error", origin="MICROSERVICE", errorCode="9288", errorMessage="Database Error", errorDetail="Syserr: {0}".format(e))
    finally:
        cursor.close()
        cp_pool.release(connection)
        
    return status_code


