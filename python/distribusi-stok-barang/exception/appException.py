class AppException(Exception):
    def __init__(self, loggerId: str,  statusCode: str, statusMessage: str, errorMessage: str):
        self.logger_id = loggerId
        self.status_code = statusCode
        self.status_msg = statusMessage
        self.error_msg = errorMessage