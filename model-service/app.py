import os
import re
import unicodedata
from typing import List

import joblib
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel


MODEL_PATH = os.getenv("MODEL_PATH", "/app/models/modelo_techmind.pkl")

app = FastAPI(title="TechMind Model Service")

artifacts = joblib.load(MODEL_PATH)

model = artifacts["modelo"]
vectorizer = artifacts["vectorizer"]


CATEGORY_MAP = {
    "Backend": "backend",
    "Frontend": "frontend",
    "DataScience": "data-science",
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


def extract_keywords(text: str, top_n: int = 3) -> List[str]:
    tfidf_vector = vectorizer.transform([text])
    scores = tfidf_vector.toarray()[0]
    terms = vectorizer.get_feature_names_out()

    top_indices = scores.argsort()[-top_n:][::-1]

    keywords = []
    for index in top_indices:
        if scores[index] > 0:
            normalized = normalize_tag(terms[index])
            if normalized and normalized not in keywords:
                keywords.append(normalized)

    return keywords


@app.get("/health")
def health():
    return {
        "status": "ok",
        "model": type(model).__name__,
        "vectorizer": type(vectorizer).__name__,
        "classes": list(model.classes_),
    }


@app.post("/predict")
def predict(request: PredictRequest):
    title = request.title or ""
    text = request.text or ""

    combined_text = f"{title}\n\n{text}".strip()

    if not combined_text:
        raise HTTPException(status_code=400, detail="title or text is required")

    vector = vectorizer.transform([combined_text])

    raw_category = str(model.predict(vector)[0])
    category = CATEGORY_MAP.get(raw_category, normalize_tag(raw_category))

    probability = None
    if hasattr(model, "predict_proba"):
        probability = float(model.predict_proba(vector)[0].max())

    keywords = extract_keywords(combined_text, top_n=3)

    tags = []
    for tag in [category, *keywords]:
        normalized = normalize_tag(tag)
        if normalized and normalized not in tags:
            tags.append(normalized)

    return {
        "category": category,
        "probability": probability,
        "tags": tags,
    }