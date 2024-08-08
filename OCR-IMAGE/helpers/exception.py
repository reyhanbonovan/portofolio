class DatabaseException(Exception):
    def __init__(self, message, origin, errorCode, errorMessage, errorDetail):
        super().__init__(message)
        self.origin = origin
        self.errorCode = errorCode
        self.errorMessage = errorMessage
        self.errorDetail = errorDetail

class SchemaException(Exception):
    pass

class ocrServiceException(Exception):
    def __init__(self, message, origin, errorCode, errorMessage):
        super().__init__(message)
        self.origin = origin
        self.errorCode = errorCode
        self.errorMessage = errorMessage
