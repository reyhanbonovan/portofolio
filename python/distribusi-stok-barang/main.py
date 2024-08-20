from fastapi import FastAPI, Depends
import uvicorn
from sqlalchemy.orm import Session
from typing import Union

from exception.globalException import global_exception
from schemas.distribusiSchemas import ReqInsertDTO, ReqSelectDTO, ReqAddDTO, ReqOrderDTO, ReqDeleteDTO, ResponseDTO, ResErrAppModel
from services.insertService import InsertDistribusiBarang
from services.selectService import SelectDistribusiBarang
from services.addService import AddDistribusiBarang
from services.orderService import OrderDistribusiBarang
from services.deleteService import DeleteDistribusiBarang
from helpers.config import Config as config
from models.databaseConnection import get_db



def create_app() -> FastAPI:
    app = FastAPI(title='Microservice Distribusi Barang Service', debug=True) # Dev Settings
    return app

tags_metadata = [
    {
        "name": "Distribusi Barang",
        "description": "Service Pengadaan dan Pemesanan Barang",
    }
]

app = create_app()
global_exception(app)

@app.post('/distribusi_barang/insertBarang', response_model=Union[ResponseDTO, ResErrAppModel])
def MsInsertBarang(req:ReqInsertDTO, db: Session = Depends(get_db)):
    return InsertDistribusiBarang(req, db)

@app.post('/distribusi_barang/selectBarang', response_model=Union[ResponseDTO, ResErrAppModel])
def MsSelectBarang(req:ReqSelectDTO, db: Session = Depends(get_db)):
    return SelectDistribusiBarang(req, db)  

@app.post('/distribusi_barang/addBarang', response_model=Union[ResponseDTO, ResErrAppModel])
def MsAddBarang(req:ReqAddDTO, db: Session = Depends(get_db)):
    return AddDistribusiBarang(req, db)  

@app.post('/distribusi_barang/orderBarang', response_model=Union[ResponseDTO, ResErrAppModel])
def MsOrderBarang(req:ReqOrderDTO, db: Session = Depends(get_db)):
    return OrderDistribusiBarang(req, db)  

@app.post('/distribusi_barang/deleteBarang', response_model=Union[ResponseDTO, ResErrAppModel])
def MsDeleteBarang(req:ReqDeleteDTO, db: Session = Depends(get_db)):
    return DeleteDistribusiBarang(req, db)  

if __name__ == "__main__":
    uvicorn.run("__main__:app", host=config.app_host, port=config.app_port, workers=config.app_worker)


