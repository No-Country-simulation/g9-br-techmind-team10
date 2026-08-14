class ModeloTechMind:
    def __init__(self, modelo, vectorizer, kw_model):
        self.modelo = modelo
        self.vectorizer = vectorizer
        self.kw_model = kw_model

    def predict(self, texto, top_n=3):
        texto_tfidf = self.vectorizer.transform([texto])

        categoria = self.modelo.predict(texto_tfidf)[0]

        probabilidades = self.modelo.predict_proba(texto_tfidf)[0]
        probabilidade = float(probabilidades.max())

        keywords = self.kw_model.extract_keywords(
            texto,
            keyphrase_ngram_range=(1, 2),
            top_n=top_n
        )
        palavras_chave = [item[0] for item in keywords]

        return {
            "categoria": str(categoria),
            "probabilidade": round(probabilidade, 2),
            "informacoes_adicionais": palavras_chave
        }