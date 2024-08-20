from fastapi.responses import JSONResponse
from loguru import logger

from exception.appException import AppException
from schemas.distribusiSchemas import ResErrAppModel
from helpers.config import Config as cf

def global_exception(app):
    @app.exception_handler(Exception)
    async def general_exception_handler(request, exc: Exception):
        logger.error(f"Exception type: {type(exc).__name__}")
        logger.error(f"Exception message: {str(exc)}")
        response_error = ResErrAppModel(logger_id="", status_code=500, status_msg=cf.param_failed, error_message="INTERNAL SERVER ERROR")
        return JSONResponse(status_code=500, content=response_error.dict())
    @app.exception_handler(AppException)
    async def custom_exception_handler(request, exc: AppException):
        logger.error(f"{exc.logger_id} - {exc.error_msg}")
        error_response = ResErrAppModel(logger_id= exc.logger_id, status_code=exc.status_code, status_msg=exc.status_msg, error_message=exc.error_msg)
        logger.info(f"{exc.logger_id} - Failed processing. Response:\n{error_response.dict()}")
        return JSONResponse(status_code=400, content=error_response.dict())
