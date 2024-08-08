from fastapi import FastAPI
import uvicorn

from schemas.schemas import OCRRequest, OCRRes
from controllers.handler import ocr_image
from helpers.database import cp_pool
from models.imgSwitching import health_check
from helpers.config import app_host, app_port, param_workers, cp_pool


def create_app() -> FastAPI:
    app = FastAPI(title='Image OCR', debug=True) # Dev Settings

app = create_app()

@app.post("/ocr/image", response_model=OCRRes, response_model_exclude_unset=True)
async def ms_ocr_image(req: OCRRequest):
    result = await ocr_image(req, cp_pool)
    return result

@app.get("/hlt_db")
async def db_health():
    return health_check(cp_pool)

@app.get("/hlt_svc")
async def svc_health():
    return {"ok"}

if __name__ == "__main__":
    uvicorn.run("__main__:app", host=app_host, port=app_port, workers=param_workers)


