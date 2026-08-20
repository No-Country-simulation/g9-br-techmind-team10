# g9-br-techmind-team10

# TechMind — Plataforma de Catalogação Inteligente de Conteúdos Técnicos

## Experimente o TechMind na OCI

O projeto está disponível online, hospedado na **Oracle Cloud Infrastructure (OCI)**.

### 🌐 Frontend

👉 [**Acessar o TechMind**](https://objectstorage.sa-saopaulo-1.oraclecloud.com/n/grjan2o1kcmy/b/techmind-frontend/o/index.html)

> Frontend hospedado via **OCI Object Storage**.

## Visão geral

O **TechMind** é uma aplicação desenvolvida para organizar, classificar e consultar conteúdos técnicos de forma mais eficiente. A proposta do projeto é permitir que usuários cadastrem textos, artigos, anotações ou materiais de estudo, e que esses conteúdos sejam categorizados por tags técnicas, facilitando futuras buscas e revisões.

O projeto está sendo desenvolvido como parte do desafio da **NoCountry**, pela equipe **G9 Team10**.

## Objetivo do projeto

O objetivo principal é criar uma biblioteca técnica inteligente, onde o usuário possa:

- Cadastrar conteúdos técnicos.
- Consultar conteúdos por tags e categorias.
- Visualizar detalhes completos dos conteúdos.
- Manter histórico de leitura.
- Favoritar conteúdos importantes.
- Organizar conteúdos com tags personalizadas próprias.

A ideia é que a plataforma funcione como um ambiente de apoio ao estudo, à pesquisa e à revisão de temas técnicos.

## Tecnologias utilizadas

### Backend

- Java 17
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Flyway
- Oracle Database
- Docker
- FastAPI
- Gemini
- Mockito
- Testes Unitários

### Frontend

- HTML
- CSS
- JavaScript
- Nginx para servir o frontend em ambiente Docker

### Banco de dados

- Oracle Database Free em ambiente local via Docker
- Estrutura versionada com migrations Flyway

## Funcionalidades implementadas até o momento

### Autenticação

O sistema possui autenticação com JWT, permitindo que usuários criem conta, façam login e acessem endpoints protegidos.

Funcionalidades disponíveis:

- Cadastro de usuário.
- Login.
- Proteção de rotas autenticadas.
- Identificação do usuário logado por token.

### Conteúdos técnicos

O usuário pode cadastrar conteúdos técnicos na plataforma. Cada conteúdo possui informações como:

- Título.
- Texto completo.
- Categoria.
- Probabilidade/confiança da classificação.
- Tags técnicas associadas.
- Data de processamento.

A API também permite consultar o detalhe completo de um conteúdo específico.

### Tags globais do sistema

O projeto possui um sistema de tags globais, associadas aos conteúdos técnicos. Essas tags representam a classificação geral do conteúdo, como por exemplo:

- backend
- frontend
- java
- spring-boot
- cloud
- docker
- database
- security

Essas tags são utilizadas para a busca principal da biblioteca.

### Busca por tags

A biblioteca permite buscar conteúdos a partir de uma ou mais tags. O backend retorna conteúdos que correspondem aos filtros selecionados pelo usuário.

Essa funcionalidade já está integrada ao frontend e permite consultar os conteúdos disponíveis na base.

### Histórico de leitura

Foi implementado um sistema de histórico, no qual o backend registra quando o usuário acessa o detalhe de um conteúdo. Com isso, o usuário consegue consultar os conteúdos que já visualizou anteriormente.

Endpoint principal:

```http
GET /content/history
```

### Favoritos

O sistema também permite que o usuário favorite e remova conteúdos dos favoritos.

Endpoints principais:

```http
GET    /content/favorites
POST   /content/{id}/favorite
DELETE /content/{id}/favorite
```

Essa funcionalidade já está integrada ao frontend.

### Contador de conteúdos por categoria

Foi adicionado um endpoint para retornar a quantidade de conteúdos agrupados por categoria.

Endpoint:

```http
GET /content/count
```

Esse recurso pode ser utilizado futuramente para dashboards, indicadores ou visualizações estatísticas da biblioteca.

## Tags personalizadas do usuário

Também foi iniciado o backend para um sistema de **tags personalizadas**.

Diferente das tags globais do sistema, as tags personalizadas pertencem exclusivamente ao usuário logado. Elas permitem que cada pessoa organize conteúdos de acordo com sua própria lógica, por exemplo:

- Ler depois.
- Revisar.
- Importante.
- Projeto pessoal.
- Estudo faculdade.

Para evitar conflito com as tags globais, foi criada uma estrutura separada no banco de dados.

Tabela criada:

```text
user_content_tag
```

Essa tabela relaciona:

```text
usuário + conteúdo + tag personalizada
```

Endpoints planejados/implementados no backend:

```http
GET    /content/{contentId}/personal-tags
POST   /content/{contentId}/personal-tags
DELETE /content/{contentId}/personal-tags/{tagId}

GET    /content/personal-tags
GET    /content/personal-tags/search?tags=ler-depois
```

Regras principais:

- O frontend não envia `userId`.
- O usuário é identificado pelo token JWT.
- Um usuário não pode ver nem remover tags personalizadas de outro usuário.
- Tags iguais com variações de maiúsculas, acentos ou espaços são normalizadas para evitar duplicidade.

Exemplo:

```text
"Ler depois"
"LER   DEPOIS"
"ler depois"
```

Todas são tratadas como:

```text
ler-depois
```

## Organização atual da arquitetura

O backend segue uma estrutura em camadas:

```text
Controller → Service → Repository → Database
```

Principais responsabilidades:

- **Controller:** expõe os endpoints da API.
- **Service:** concentra as regras de negócio.
- **Repository:** realiza consultas no banco.
- **DTOs:** padronizam os dados recebidos e enviados pela API.
- **Migrations Flyway:** versionam a estrutura do banco de dados.

## Ambiente local

O projeto pode ser executado localmente com Docker Compose.

Comando principal:

```bash
docker compose up --build
```

A aplicação sobe com:

- Backend Spring Boot.
- Frontend servido por Nginx.
- Banco Oracle local.
- Migrations aplicadas automaticamente pelo Flyway.

URLs principais em ambiente local:

```text
Frontend: http://localhost/
Backend:  http://localhost:8080
Swagger:  http://localhost:8080/swagger-ui/index.html
```

## Status atual

Atualmente o projeto já possui:

- Backend estruturado com Spring Boot.
- Banco Oracle com migrations.
- Autenticação JWT.
- Cadastro e login de usuários.
- Cadastro de conteúdos.
- Busca por tags globais.
- Histórico de leitura.
- Favoritos.
- Contagem de conteúdos por categoria.
- Backend inicial para tags personalizadas do usuário.
- Frontend funcional para biblioteca, envio de conteúdo, favoritos e histórico.
- Testes Unitários ,usando Mackito, dos principais endpoints da API.
- Integra o backend com um serviço Python de predição baseado em modelo .pkl.

## Implementação do modelo com PKL e FastAPI

O modelo utilizado nesta etapa é resultado de um pipeline de Data Science desenvolvido em notebooks sequenciais, que inclui coleta de dados via API do Dev.to, validação manual de amostra, limpeza e pré-processamento NLP, treinamento e comparação de algoritmos de classificação (Regressão Logística, Naive Bayes e SVM), e extração de palavras-chave com KeyBERT. O modelo utilizado em produção é o de **Regressão Logística**.

Todo o pipeline, incluindo notebooks, datasets intermediários (.csv), geração dos artefatos e descrições, está disponível na pasta [`datascience/`](./datascience/) e em [`datascience/README.md`](./datascience/README.md).

Após o treinamento e a escolha do melhor modelo, o classificador, o vetorizador TF-IDF e o extrator de palavras-chave foram encapsulados em uma única classe e serializados em Python no formato **`.pkl`**. Esse artefato armazena todo o pipeline treinado, permitindo que ele seja reutilizado para gerar novas previsões (categoria, probabilidade e palavras-chave) sem necessidade de novo treinamento.

Para disponibilizar esse artefato ao backend, foi desenvolvido um serviço utilizando **Python e FastAPI**. A API recebe o título e o texto técnico enviado pela aplicação, realiza o processamento necessário e utiliza o modelo serializado para gerar a previsão da categoria, a probabilidade de confiança e as palavras-chave.

Dessa forma, o serviço Python funciona como uma camada de integração entre o modelo de Data Science e o backend Spring Boot, permitindo que a classificação automática seja utilizada diretamente pela plataforma TechMind.

## Rodando o projeto localmente com Docker Compose

O projeto pode ser executado localmente com Docker Compose, subindo automaticamente:

```text
- Oracle Database 26ai Free
- Backend Spring Boot
- Frontend Nginx
```

### Pré-requisitos

É necessário ter instalado:

```text
- Docker Desktop
- Docker Compose
```

No Windows, o Docker Desktop deve estar rodando com Linux containers.

### Subir o projeto

Na raiz do projeto, execute:

```bash
docker compose up --build
```

Na primeira execução, o Docker fará o download da imagem do Oracle Database 26ai Free, o que pode demorar alguns minutos.

Quando os containers estiverem rodando, acesse:

```text
Frontend:
http://localhost/

Backend:
http://localhost:8080

Swagger:
http://localhost:8080/swagger-ui/index.html
```

### Verificar containers

Em outro terminal, na raiz do projeto:

```bash
docker compose ps
```

O esperado é ver os três containers rodando:

```text
techmind-oracle-db
techmind-backend
techmind-frontend
```

### Parar o projeto

No terminal onde o Docker Compose está rodando:

```text
Ctrl + C
```

Ou, em outro terminal:

```bash
docker compose down
```

### Subir novamente sem rebuild

Depois da primeira execução, normalmente basta rodar:

```bash
docker compose up
```

Use `--build` novamente quando alterar Dockerfile, dependências ou configurações relevantes.

### Resetar o banco local

Para apagar o banco local e recriar tudo do zero:

```bash
docker compose down -v
docker compose up --build
```

Atenção: o comando `docker compose down -v` remove o volume do Oracle e apaga os dados locais.
