import os
import re
import sys
import unicodedata
from typing import Any, Dict, List

import joblib
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

from model_wrapper import ModeloTechMind


# Ajuda o joblib a reconhecer a classe caso o .pkl tenha sido salvo a partir de notebook.
sys.modules["__main__"].ModeloTechMind = ModeloTechMind


MODEL_PATH = os.getenv("MODEL_PATH", "/app/models/modelo_techmind.pkl")

app = FastAPI(title="TechMind Model Service")

modelo_techmind = joblib.load(MODEL_PATH)


CATEGORY_MAP = {
    "Backend": "backend",
    "Frontend": "frontend",
    "DataScience": "data-science",
    "Data Science": "data-science",
    "Database": "database",
    "Cloud": "cloud",
    "Security": "security",
}


class PredictRequest(BaseModel):
    title: str
    text: str


def normalize_tag(value: str) -> str:
    value = str(value or "").strip().lower()

    value = unicodedata.normalize("NFKD", value)
    value = "".join(char for char in value if not unicodedata.combining(char))

    value = re.sub(r"[^a-z0-9]+", "-", value)
    value = re.sub(r"-+", "-", value)

    return value.strip("-")


def normalize_category(value: str) -> str:
    value = str(value or "").strip()
    return CATEGORY_MAP.get(value, normalize_tag(value))


def adapt_new_wrapper_result(result: Dict[str, Any]) -> Dict[str, Any]:
    raw_category = result.get("categoria")
    raw_probability = result.get("probabilidade")
    raw_keywords = result.get("informacoes_adicionais", [])

    category = normalize_category(raw_category)

    tags: List[str] = []
    for tag in [category, *raw_keywords]:
        normalized = normalize_tag(tag)
        if normalized and normalized not in tags:
            tags.append(normalized)

    return {
        "category": category,
        "probability": raw_probability,
        "tags": tags,
    }


@app.get("/health")
def health():
    return {
        "status": "ok",
        "model": type(modelo_techmind).__name__,
    }


@app.post("/predict")
def predict(request: PredictRequest):
    title = request.title or ""
    text = request.text or ""

    combined_text = f"{title}\n\n{text}".strip()

    if not combined_text:
        raise HTTPException(status_code=400, detail="title or text is required")

    result = modelo_techmind.predict(combined_text)

    return adapt_new_wrapper_result(result)