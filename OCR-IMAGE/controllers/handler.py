from loguru import logger
from datetime import datetime
import json
from helpers.config import param_ocr_type, param_error, param_failed ,param_success, param_service_name
from helpers.exception import ocrServiceException, DatabaseException
from models.imgSwitching import insertDb
from services.switching import imgSwitching
import re
from traceback import format_exc

# async def ocr_image(req, cp_pool):
async def ocr_image(req):
    timestamp = datetime.now()
    loggerTime = timestamp.strftime("%y%m%d%H%M%S%f")
    useType = param_ocr_type
    akun = req.akun
    idRefferal = req.idRefferal
    image = req.image
    bizId = akun + loggerTime
    result = {
            "akun": akun,
            "idRefferal": idRefferal,
            "bizId": bizId,
            "ocr": {},
            "spoofResult": {},
            "transactionId": "",
            "recognitionResult": "",
            "recognitionErrorCode": "",
            "status": "",
            "errorCode": "",
            "errorMessage": "",
        }
    logger.info("{0} - {1} - OCR yang digunakan : {2}".format(loggerTime, idRefferal, useType))
    log_req = {
        "akun": akun,
        "idRefferal": idRefferal,
        "bizId": bizId,
        "image": "Image Foto"
    }
    logger.info('{0} - {1} -  Request :\n{2}'.format(loggerTime, idRefferal, json.dumps(dict(log_req), indent=2)))
    logger.info(
    "{0} - {1} - App Starting".format(loggerTime, idRefferal)
    )
    timestamp = datetime.now()
    errorCode = ""
    errorDescString = ""
    rawReq = ""
    rawRes = ""
    ocr = {}
    status = ""
    ocrDataFail = {
        "ocr_id": "",
        "provinsi": "",
        "kota_kabupaten": "",
        "nik": "",
        "nama": "",
        "tempat_lahir": "",
        "tanggal_lahir": "",
        "jenis_kelamin": "",
        "golongan_darah": "",
        "alamat": "",
        "rtrw": "",
        "kelurahan_atau_desa": "",
        "kecamatan": "",
        "agama": "",
        "status_perkawinan": "",
        "pekerjaan": "",
        "kewarganegaraan": "",
        "berlaku_hingga": "",
        "kota_penerbit": "",
        "tanggal_terbit": "",
    }

    # call OCRHandler Switcher
    if errorCode == "" or errorCode is None:
        logger.info("{0} - {1} - Start to processing OCR Handling ".format(loggerTime, idRefferal))
        try:   
            OCRRes, compressedData = imgSwitching(loggerTime, useType, akun, idRefferal, image, bizId)
            try:
                if compressedData["image"] is None or compressedData["image"]=="":
                    image = image
                else:
                    image = compressedData["image"]
            except KeyError as e:
                image = image
            if OCRRes["status_code"] == "200":
                result["transactionId"] = OCRRes["transactionId"]
                result["recognitionResult"] = OCRRes["recognitionResult"]
                result["recognitionErrorCode"] = OCRRes["recognitionErrorCode"]
                # ocr = OCRRes
                date_string = OCRRes['tanggal_lahir']
                date = extract_date(date_string)
                ocr = {
                        "ocr_id": OCRRes['ocr_id'],
                        "provinsi": OCRRes['provinsi'],
                        "kota_kabupaten": OCRRes['kota_kabupaten'],
                        "nik": OCRRes['nik'],
                        "nama": OCRRes['nama'],
                        "tempat_lahir": OCRRes['tempat_lahir'],
                        "tanggal_lahir": str(date),
                        "jenis_kelamin": OCRRes['jenis_kelamin'],
                        "golongan_darah": OCRRes['golongan_darah'],
                        "alamat": OCRRes['alamat'],
                        "rtrw": OCRRes['rtrw'],
                        "kelurahan_atau_desa": OCRRes['kelurahan_atau_desa'],
                        "kecamatan": OCRRes['kecamatan'],
                        "agama": OCRRes['agama'],
                        "status_perkawinan": OCRRes['status_perkawinan'],
                        "pekerjaan": OCRRes['pekerjaan'],
                        "kewarganegaraan": OCRRes['kewarganegaraan'],
                        "berlaku_hingga": OCRRes['berlaku_hingga'],
                        "kota_penerbit": OCRRes['kota_penerbit'],
                        "tanggal_terbit": OCRRes['tanggal_terbit']
                    }
                errorCode = ""
                status = param_success
            else:
                if OCRRes["status_code"] == param_error["general_exception"][0]:
                    errorCode = param_error["general_exception"][0]
                    errorDescString = OCRRes["status_desc"]
                elif OCRRes["status_code"] == "001" or OCRRes["status_code"] == "002" or OCRRes["status_code"] == "003" or OCRRes["status_code"] == "004":
                    errorCode = param_error["ocr"][0]
                    errorDescString = OCRRes["status_desc"]
                else:
                    errorCode = param_error["general_exception"][0]
                    errorDescString = f"(MICROSERVICE) Timeout or Please Check OCR {useType}"
                status = param_failed
                ocr = ocrDataFail
            result["status"] = status

        except KeyError as e:
            logger.error(f"{loggerTime} - {idRefferal} - An error has occured when verifying data from OCR {useType}. ")
            logger.error(f"Syserr:{e}")
            logger.error(f"Traceback::{format_exc()}")
            errorCode = param_error["general_exception"][0]
            errorDescString = f"(MICROSERVICE) Timeout or Please Check OCR {useType}"
        except ocrServiceException as e:
            logger.error("{0} - {1} - Error happened when call OCR Service ".format(loggerTime, idRefferal))
            logger.error("Syserr: {0}".format(e))
            logger.error(f"Traceback::{format_exc()}")
            errorCode = param_error["general_exception"][0]
            errorDescString = param_error["general_exception"][1]
            raise ocrServiceException(
                "GENERAL ERROR",
                " (MICROSERVICE) ",
                errorCode,
                errorMessage=errorDescString,
            )
        endDate = datetime.now()
        timeDelta = endDate - timestamp
        logger.info(
            "{0} - {1} - Process finished from OCRHandling. Time elapsed: ".format(
                loggerTime, idRefferal
            )
            + str(timeDelta.total_seconds() * 1000)
            + "ms"
        )
    
    result["ocr"] = ocr
    result["errorCode"] = errorCode
    result["errorMessage"] = errorDescString.upper()
    logger.info(
        "{0} - {1} - Response from OCR Handling is: {2}".format(
            loggerTime, idRefferal, json.dumps(dict(result), indent=2)
        )
    )

    # #Insert to Database : Oracle
    # try :
    #     idNo = ocr["nik"]
    #     nama = ocr["nama"]
    #     b_place = ocr["tempat_lahir"]
    #     bdate = ocr["tanggal_lahir"]
    #     if is_valid_date(bdate, "%Y-%m-%d %H:%M:%S"):
    #         dob = datetime.strptime(bdate, "%Y-%m-%d %H:%M:%S")
    #     elif is_valid_date(bdate, "%Y-%m-%d"):
    #         dob = datetime.strptime(bdate, "%Y-%m-%d")
    #     else:
    #         dob = ""
    #     btype = ocr["golongan_darah"]
    #     if (btype == "AB") or (btype == "A") or (btype == "B") or (btype == "O"):
    #         gol_darah = btype
    #     else: 
    #         gol_darah = "BLANK"
    #     prof = ocr["provinsi"]
    #     kota = ocr["kota_kabupaten"]
    #     kecamatan = ocr["kecamatan"]
    #     desa = ocr["kelurahan_atau_desa"]
    #     if ocr["rtrw"] != "":
    #         temp_rtrw = ocr["rtrw"].split("/") 
    #         rt = temp_rtrw[0]
    #         rw = temp_rtrw[1]
    #     else:
    #         rt = ""
    #         rw = ""
    #     alamat = ocr["alamat"]
    #     jenis_kelamin = ocr["jenis_kelamin"]
    #     agama = ocr["agama"]
    #     status = ocr["status_perkawinan"]
    #     pekerjaan = ocr["pekerjaan"]
    #     identity_county = ocr["kewarganegaraan"]
    #     berlaku = ocr["tanggal_terbit"]
        
    #     rawReq = {
    #         "akun": akun,
    #         "idRefferal": idRefferal,
    
    #         "bizId": bizId,
    #         "image" : "Base64 Image"
    #     }
    #     rawRes = result

    #     insertDb(
    #         akun,
    #         idRefferal,
    #         useType,
    #         bizId,
    #         errorCode,
    #         status,
    #         errorDescString.upper(),
    #         idNo,
    #         nama,
    #         b_place,
    #         dob,
    #         gol_darah,
    #         prof,
    #         kota,
    #         kecamatan,
    #         desa,
    #         rt,
    #         rw,
    #         alamat,
    #         jenis_kelamin,
    #         agama,
    #         status,
    #         pekerjaan,
    #         identity_county,
    #         berlaku,
    #         str(rawReq),
    #         str(rawRes),
    #         param_service_name,
    #         cp_pool
    #     )
    #     logger.info("{0} - {1} - Insert database success".format(loggerTime, idRefferal))
    # except KeyError as e:
    #     logger.error(f"{loggerTime} - {idRefferal} - An error has occured when verifying data from OCR {useType}. ")
    #     logger.error(f"Syserr:{e}")
    #     logger.error(f"Traceback::{format_exc()}")
    #     errorCode = param_error["general_exception"][0]
    #     errorDescString = f"(MICROSERVICE) Timeout or Please Check OCR {useType}"
    # except DatabaseException as e:
    #     logger.error("{0} - {1} - Failed to insert session. Database problem.".format(loggerTime, idRefferal))
    #     logger.error(f"Exception Type: {type(e).__name__} - Syserr: {e}")
    #     logger.error(f"Traceback::{format_exc()}")

    endDate = datetime.now()
    timeDelta = endDate - timestamp
    logger.info(
        "{0} - {1} - Process finished from OCR. Time elapsed: ".format(
            loggerTime, idRefferal
        )
        + str(timeDelta.total_seconds() * 1000)
        + "ms"
    )
    return result
    
def is_valid_date(date_str, date_format):
    try:
        datetime.strptime(date_str, date_format)
        return True
    except ValueError:
        return False
    
def extract_date(date_string):
    if date_string == None:
        return None

    date = None
    try:
        regex = re.compile(r"(\d{1,4}-\d{1,2}-\d{1,2})")
        tgl = re.findall(regex, date_string)
        if len(tgl) > 0:
            date = datetime.strptime(tgl[0], "%Y-%m-%d").date()
        else:
            tgl = "".join([n for n in date_string if n.isdigit()])
            if len(tgl) == 8:
                date = datetime.strptime(
                    tgl[4:] + "-" + tgl[2:4] + "-" + tgl[0:2], "%Y-%m-%d"
                ).date()
            if len(tgl) == 4:
                date = datetime.strptime(
                    tgl+"-01-01", "%Y-%m-%d"
                ).date()
        return date
    except ValueError:
        return None