from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from helpers.config import Config as config
# from helpers.exception import DatabaseException

dburl = f"postgresql+psycopg2://{config.db_user}:{config.db_pass}@{config.db_host}:{config.db_port}/{config.db_name}"

engine = create_engine(dburl)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

