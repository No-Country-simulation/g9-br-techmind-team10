# Pipeline de Data Science - TechMind

## 1. Visão Geral
Este repositório contém a esteira de desenvolvimento de Ciência de Dados para o MVP **TechMind**, desenvolvido no âmbito do **Hackathon ONE**.
O objetivo deste pipeline é realizar desde o teste de viabilidade do consumo de dados via API até o treinamento e exportação do modelo preditivo para produção.

---
## 🔎 2. Origem dos Dados
* **Fonte principal:** API pública (v1) da DEV Community (Forem)
* **Tipo de Acesso:** API REST (requisições HTTP / resposta em formato JSON)
* **Objetivo da Coleta:** Obter artigos e publicações técnicas da plataforma para alimentar as etapas de análise, pré-processamento e modelagem do MVP.

---

## 🔄 3. Etapas do Pipeline (Notebooks)

### Notebook 01: Teste de Consumo da API e Validação (PoC)
* **Arquivo:** `01_teste_consumo_api.ipynb`
* **Objetivo:** Realizar uma Prova de Conceito (PoC) para validar a conectividade, estabilidade e a estrutura dos dados fornecidos pela API pública do DEV Community para o Projeto TechMind (Hackathon ONE). O foco é executar um *smoke test* com uma amostra reduzida para auditar a integração e o fluxo antes de avançar para a pipeline de coleta em escala.
* **Entrada (Input):** 
    * **Endpoint Alvo:** `https://dev.to/api/articles`
    * **Tags/Categorias de Teste:** *Backend*, *Frontend*, *Data Science* e *IA* (ex: `python`, `devops`, `ai`, etc.)
    * **Tamanho da Amostra:** 20 artigos selecionados para auditoria (5 por categoria)
    * **Controle de Rate Limit:** Pausa de `0.5s` (`time.sleep`) entre requisições para respeitar os limites de taxa do servidor
* **Processamento:**
    * **Análise Exploratória e Validação Técnica:**
        1. **Contrato do Dado:** Inspeção do payload JSON retornado para garantir o recebimento do artigo completo (`body_markdown`) em vez de apenas resumos/snippets.
        2. **Privacidade e Governança (LGPD):** Identificação e isolamento de metadados de identificação pessoal de autores (PII como `user`, `profile_image` e URLs de perfil) para descarte ou ocultação.
        3. **Resiliência da Integração:** Avaliação do comportamento do endpoint sob requisições sequenciais para prevenir e mitigar erros HTTP (como 429 ou 500).
        4. **Análise de Formato:** Mapeamento do texto bruto recebido (Markdown, blocos de código, links e tags de mídia) para planejar as etapas futuras de limpeza no pipeline de NLP.
* **Saída (Output):** Parecer técnico confirmando a viabilidade da API, mapeamento das regras de negócio/restrições (parâmetros e campos nulos) e estratégia de privacidade de dados dos autores (PII).

### Notebook 02: Coleta de Dados Massiva e Filtragem Inicial
* **Arquivo:** `02_coleta_dados.ipynb`
* **Objetivo:** Realizar a extração massiva dos artigos da API REST do DEV Community com base em uma estrutura hierárquica de categorias e subtags, aplicar rotulagem (*Data Labeling*) e conduzir os primeiros filtros de idioma e deduplicação.
* **Entrada (Input):** 
    * Consumo direto da **API REST DEV Community** (`https://dev.to/api/articles`).
    * **Mapeamento de Categorias Hierárquicas (6 Categorias x 6 Subtags):**
        * **Frontend:** `frontend`, `javascript`, `react`, `css`, `typescript`, `nextjs`
        * **Backend:** `backend`, `springboot`, `java`, `api`, `php`, `csharp`
        * **Cloud:** `cloud`, `kubernetes`, `oci`, `aws`, `azure`, `docker`
        * **Database:** `database`, `postgres`, `nosql`, `sql`, `mongodb`, `crud`
        * **Security:** `security`, `mfa`, `cybersecurity`, `infosec`, `cryptography`, `ethicalhacking`
        * **DataScience:** `datascience`, `machinelearning`, `pandas`, `ai`, `python`, `deeplearning`
    * **Parâmetro de Coleta:** 60 artigos por subtag ($6 \text{ categorias} \times 6 \text{ subtags} \times 60 \text{ artigos} = 2.160 \text{ artigos total}$).
* **Processamento:**
    1. **Data Labeling & Coleta por Subcategoria:** Iteração sobre o dicionário de categorias realizando requisições HTTP e associando o rótulo da categoria principal (*target*) a cada artigo extraído, gerando o arquivo `artigos.csv` (**2.160 artigos**).
    2. **Análise Exploratória Inicial:** Inspeção preliminar do volume coletado para identificar inconsistências gerais de idioma e estrutura.
    3. **Filtro de Idioma (Inglês):** Filtragem dos textos para manter apenas artigos no idioma inglês, gerando o arquivo `artigos_en.csv` (**2.010 artigos**).
    4. **Amostragem Estratificada e Deduplicação:** 
        * Remoção de amostras duplicadas do conjunto filtrado.
        * Extração de uma amostra de **10% por subcategoria** para auditoria humana, gerando o arquivo `artigos_en_amostra.csv` (**201 artigos**).
* **Saída (Output):** 
    * `artigos.csv`: Dataset bruto com a coleta total e rotulada (**2.160 artigos**).
    * `artigos_en.csv`: Dataset filtrado contendo apenas conteúdo em inglês (**2.010 artigos**) que alimenta o Notebook 03.
    * `artigos_en_amostra.csv`: Amostra estratificada (**201 artigos**) direcionada para a etapa de **Validação Manual**. A validação manual será feita pelos membros da equipe, onde cada integrante irá analisar o texto e ver se encaixa com a categoria em questão.

### Notebook 03: Análise da Amostra e Curadoria do Dataset
* **Arquivo:** `03_analise_amostra.ipynb`
* **Objetivo:** Conduzir a validação e conferência manual da amostra extraída, auditando a qualidade do texto e a consistência das categorias para realizar o descarte de subcategorias ruidosas/irrelevantes e gerar a base consolidada e filtrada.
* **Entrada (Input):** 
    * `artigos_en.csv`: Dataset contendo todos os artigos no idioma inglês (**2.010 artigos**).
    * `artigos_en_amostra.csv`: Amostra estratificada contendo 10% da base (**201 artigos**) com o resultado da **Validação Manual**.
* **Processamento:**
    1. **Conferência e Auditoria dos Dados:** Avaliação do feedback da validação manual aplicada sobre a amostra (`artigos_en_amostra.csv`) para mapear inconsistências de rotulagem e desvios de tema.
    2. **Descarte de Subcategorias:** Filtragem e remoção de subcategorias completas que não atingiram os critérios de qualidade ou relevância previstos para o escopo do projeto.
    3. **Geração da Base Consolidada:** Aplicação das regras de corte e limpeza sobre a base total em inglês (`artigos_en.csv`), atualizando o volume de dados.
* **Saída (Output):** 
    * `artigos_en_filtrado.csv`: Dataset limpo contendo **1.851 artigos**, pronto para a etapa de pré-processamento e limpeza profunda de NLP no Notebook 04.

### Notebook 04: Pré-Processamento de NLP e Limpeza do Dataset
* **Arquivo:** `04_limpeza_dados.ipynb`
* **Objetivo:** Aplicar técnicas de diagnóstico de qualidade e pipeline de processamento de linguagem natural (NLP) sobre a base filtrada na etapa anterior para limpar o texto bruto, remover ruídos e gerar o dataset final balanceado para as etapas de modelagem e extração de palavras-chave.
* **Entrada (Input):** 
    * `artigos_en_filtrado.csv`: Dataset em inglês aprovado na etapa de validação (**1.851 artigos**).
* **Processamento:**
    1. **Diagnóstico & Análise Exploratória de Dados (EDA):**
        * Identificação e tratamento de registros duplicados remanescentes.
        * Detecção de conflitos de rotulagem e inconsistências de categoria.
        * Filtragem de campos nulos ou ausentes.
    2. **Pipeline de Pré-Processamento de NLP:**
        * Limpeza textual: remoção de URLs, caracteres especiais, pontuações e formatações indesejadas do Markdown.
        * Validação final do idioma com `langdetect` para garantia de ausência de ruídos residuais em outros idiomas.
        * Processamento linguístico com a biblioteca **spaCy** (modelo `en_core_web_sm`):
            * **Tokenização e remoção de *stop words*:** divisão do texto em unidades estruturadas (tokens) e filtragem de palavras irrelevantes.
            * **Lematização (*lemmatization*):** redução dos tokens às suas formas gramaticais base para padronização do vocabulário.
    3. **Filtro por Comprimento de Texto:**
        * Remoção de textos extremamente curtos (< 20 palavras) por falta de densidade informacional para os modelos.
        * Divisão/estruturação da base pronta para os processos de Treino e Teste.
* **Saída (Output):** 
    * `base_techmind.csv`: Dataset final higienizado e consolidado contendo **1.564 artigos**, pronto para alimentar as etapas de machine learning no Notebook 05.

### Notebook 05: Modelagem de Classificação e Extração de Palavras-Chave (TechMind)
* **Arquivo:** `05_techmind.ipynb`
* **Objetivo:** Treinar e comparar modelos de Aprendizado de Máquina para classificação de texto e avaliar técnicas avançadas de NLP para a extração de palavras-chave, consolidando a estrutura final de retorno da solução TechMind e realizando a serialização do modelo preditivo final.
* **Entrada (Input):** 
    * `base_techmind.csv`: Dataset obtido da etapa anterior contendo **1.564 artigos** processados.
* **Processamento:**
    1. **Pré-Modelagem e Classificação de Categoria (Supervisionado):**
        * Divisão da base em conjuntos de treino e teste.
        * Treinamento e avaliação de três algoritmos de classificação:
            * **Regressão Logística**
            * **Naive Bayes**
            * **Support Vector Machine (SVM)**
        * Comparação do desempenho dos modelos através de métricas de avaliação (como Acurácia, F1-Score, Precisão e Recall) e seleção do melhor classificador (**Regressão Logística**).
    2. **Extração de Palavras-Chave (Não Supervisionado / NLP):**
        * Implementação e comparação de três abordagens de extração:
            * **TF-IDF** (Abordagem estatística de frequência inversa)
            * **YAKE** (Abordagem estatística não supervisionada baseada em características textuais)
            * **KeyBERT** (Abordagem semântica baseada em *embeddings* de linguagem)
        * Comparação do desempenho dos métodos e seleção do melhor extrator semântico (**KeyBERT**).
    3. **Consolidação do Motor de Inferência (Pipelines Integradas):**
        * Definição da estrutura unificada do retorno do modelo, combinando:
            * **Categoria Prevista**
            * **Probabilidade / Confiança da Predição**
            * **Palavras-chave Extraídas**
* **Saída (Output):** 
    * `modelo.pkl`: Arquivo serializado contendo o modelo campeão de classificação (**Regressão Logística**) e o pipeline de pré-processamento pronto para produção/deploy.

---
## 🛠️ 4. Tecnologias e Dependências

* **Ambiente de Desenvolvimento:** **Jupyter Lab / VS Code**
* **Linguagem:** Python 3.10+
* **Processamento de Linguagem Natural (NLP):** `spaCy` (`en_core_web_sm`), `langdetect`
* **Vetorização e Machine Learning:** `scikit-learn` (`TfidfVectorizer`, `LogisticRegression`, `StratifiedKFold`)
* **Extração de Tópicos:** `KeyBERT` / `YAKE`
* **Manipulação de Dados & Regex:** `pandas`, `numpy`, `re`

---
## 📦 5. Como Reproduzir o Projeto

1. **Clonar o repositório e acessar o diretório de Data Science:**
   ```bash
   git clone [https://github.com/No-Country-simulation/g9-br-techmind-team10](https://github.com/No-Country-simulation/g9-br-techmind-team10)
   cd datascience
   ```

2. **Instalar as dependências e o modelo do spaCy:**
   ```bash
   pip install -r requirements.txt
   python -m spacy download en_core_web_sm
   ```
---
## 📁 6. Estrutura do Repositório

```text
datascience/
├── dados/                  # Datasets intermediários e finais (artigos.csv, base_techmind.csv, etc.)
├── modelo/                # Artefatos serializados (.pkl / .joblib)
├── notebooks/             # Notebooks de 01 a 05 desenvolvidos no Jupyter Lab / VS Code
├── requirements.txt       # Lista de bibliotecas do projeto
└── README.md              # Documentação deste módulo
```
---

## 👥 7. Equipe TechMind Data Science (Hackathon ONE)

[Patricia Duran - Kaylane Labeta - Wagner Bruni ]