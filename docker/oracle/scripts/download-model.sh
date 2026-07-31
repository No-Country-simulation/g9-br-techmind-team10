#!/bin/sh
set -e

MODEL_DIR="/models"
MODEL_NAME="multilingual-e5-base.onnx"
MODEL_URL="https://github.com/No-Country-simulation/g9-br-techmind-team10/releases/download/v1.0-models/multilingual-e5-base.onnx"

mkdir -p "$MODEL_DIR"

if [ -f "$MODEL_DIR/$MODEL_NAME" ]; then
    echo "Model already exists"
    exit 0
fi

echo "Downloading Oracle ONNX model..."

curl -fL "$MODEL_URL" -o "$MODEL_DIR/$MODEL_NAME"

echo "Model downloaded successfully"