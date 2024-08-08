from datetime import datetime
import requests
from loguru import logger
import concurrent.futures
from helpers.config import param_failed, param_error, endpoint_ocr_use, endpoint_ocr_use2, be_timeout, path_ms, path_ms2

def imgSwitching(loggerTime, useType, akun, idRefferal, image, bizId):
    if useType == "":
        body = {"akun": akun, "image": image}
        url_ocr = endpoint_ocr_use + path_ms
    elif useType == "ZOLOZ":
        body = {"bizId": bizId, "akun": akun, "image": image}
        url_ocr = endpoint_ocr_use2 + path_ms2
    status = ""
    errorCode = ""
    errorMessage = ""
    result = {}
    try:
        body["image"] = image    
        #Start Hit OCR Surrounding
        startHitOCR = datetime.now()
        logger.info("{0} - {1} - Start to hit OCR {2} : ".format(loggerTime, idRefferal, useType))
        with concurrent.futures.ThreadPoolExecutor() as executor:
            future = executor.submit(requests.post, url=url_ocr, json=body, timeout=be_timeout)
            endHitOCR = datetime.now()
            timeHitOCR = endHitOCR - startHitOCR
            logger.info("{0} - {1} - End hit OCR {2} : ".format(loggerTime, idRefferal, useType)+ str(timeHitOCR.total_seconds() * 1000)+ "ms")
            OCRRes = future.result()
            OCRRes = OCRRes.json()
            logger.info("{0} - {1} - OCR {2} Response : \n{3}".format(loggerTime, idRefferal, useType, OCRRes, indent=2))
            try:
                #Get Value OCR Surrounding Response
                result["ocr_id"] = ""
                result["provinsi"] = OCRRes["data"]["provinsi"]
                result["kota_kabupaten"] = OCRRes["data"]["kabupaten"]
                result["nik"] = OCRRes["data"]["nik"]
                # koreksi nama
                result["nama"] = OCRRes["data"]["nama"]
                if OCRRes["data"]["tmpt/tglLahir"] != "" :
                    result["tempat_lahir"] = OCRRes["data"]["tmpt/tglLahir"].split("/")
                    result["tempat_lahir"] = result["tempat_lahir"][0].strip()
                    result["tanggal_lahir"] = OCRRes["data"]["tmpt/tglLahir"].split(
                        "/"
                    )
                    result["tanggal_lahir"] = result["tanggal_lahir"][1].strip()
                else:
                    result["tempat_lahir"] = ""
                    result["tanggal_lahir"] = ""
                result["jenis_kelamin"] = OCRRes["data"]["kelamin"]
                result["golongan_darah"] = OCRRes["data"]["golDarah"]
                result["alamat"] = OCRRes["data"]["alamat"]
                result["rtrw"] = OCRRes["data"]["rt/rw"]
                result["kelurahan_atau_desa"] = OCRRes["data"]["kel/desa"]
                result["kecamatan"] = OCRRes["data"]["kecamatan"]
                result["agama"] = OCRRes["data"]["agama"]
                result["status_perkawinan"] = OCRRes["data"]["perkawinan"]
                try: 
                    result["pekerjaan"] = OCRRes["data"]["perkerjaan"]
                except: 
                    result["pekerjaan"] = OCRRes["data"]["pekerjaan"]
                result["kewarganegaraan"] = OCRRes["data"]["kewarganegaraan"]
                result["berlaku_hingga"] = OCRRes["data"]["berlaku"]
                try:
                    result["kota_penerbit"] = OCRRes["data"]["KotaTerbit"]
                except:
                    result["kota_penerbit"] = ""
                try:
                    result["tanggal_terbit"] = OCRRes["data"]["TanggalTerbit"]
                except:
                    result["tanggal_terbit"] = ""
                    errorCode = OCRRes["statusCode"]
                errorMessage = OCRRes["statusDesc"]
                
            except KeyError as e:
                logger.info(
                    "An error from MICROSERVICE OCR: ({0} - {1}".format(
                        OCRRes["statusCode"], OCRRes["statusDesc"]
                    )
                )
                logger.info(
                    "{0} - Error from OCR is {1}. - {2}".format(
                        OCRRes["statusCode"], OCRRes["statusDesc"], akun
                    )
                )
                errorCode = OCRRes["statusCode"]
                errorMessage = OCRRes["statusDesc"]

            logger.info("{0} - {1} - OCR Switcher Response Result : \n{2}".format(loggerTime, idRefferal, result, indent=2))
    except requests.ConnectionError as e:
        logger.error(f"{loggerTime} - {idRefferal} - CONNECTION ERROR TO OCR {useType}")
        logger.error(f"Syserr:{e}")
        errorCode = param_error["connection"][0]
        errorMessage = "(MICROSERVICE) " + param_error["connection"][1]
        status = param_failed
    except requests.Timeout as e:
        logger.error(f"{loggerTime} - {idRefferal} - REQUEST TIMEOUT TO OCR {useType}")
        logger.error(f"Syserr:{e}")
        errorCode = param_error["timeout"][0]
        errorMessage = "(MICROSERVICE) " + param_error["timeout"][1]
        status = param_failed
    except requests.exceptions.RequestException as e:
        logger.error(f"{loggerTime} - {idRefferal} - An error has occured when verifying data from OCR {useType}. ")
        logger.error(f"Syserr:{e}")
        status= param_error["general_exception"][0]
        errorMessage = f"(MICROSERVICE) Timeout or Please Check OCR {useType}"
    except Exception as e:
        logger.error(f"{loggerTime} - {idRefferal} - SYSTEM ERROR")
        logger.error(f"Exception Type: {type(e).__name__} - Syserr: {e}")
        errorCode = param_error["syserr"][0]
        errorMessage = "(MICROSERVICE) " + param_error["syserr"][1]
        status = param_failed

    result["status"] = status
    result["status_code"] = errorCode
    result["status_desc"] = errorMessage

    return result