# 🛡️ Zenon Fraud Detector

> Detector de fraudes bancárias construído sobre o dataset PaySim, desenvolvido como projeto prático da disciplina de Fundamentos Java da pós-graduação em Java da UNIPDS.

![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.4-4479A1?logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)

## 📋 Sobre o projeto

O **Zenon Fraud Detector** simula o backend de análise antifraude de uma fintech fictícia. O projeto parte do repositório-base **Zenon Bank**, disponibilizado pelo professor na disciplina de Fundamentos Java, e foi evoluído para processar mais de 6 milhões de transações reais do dataset PaySim — cobrindo desde a modelagem de domínio até processamento concorrente de alta performance.

## 🧱 Arquitetura

| Pacote | Responsabilidade |
|---|---|
| `model` | Domínio modelado com **Records** (`Transaction`, `Customer`, `ReportResult`), com validação de invariantes no construtor compacto |
| `repository` | Interface `TransactionRepository` com **três implementações** intercambiáveis — `List`, `Map` e `SQL` — usadas para comparar na prática o custo de busca de cada estrutura |
| `service` | Regras de negócio: parsing de CSV, ingestão (versão simples vs. versão com Streams + lazy loading), análise de fraudes e geração de relatórios |
| `cli` | Pontos de entrada executáveis, um por cenário: ingestão, benchmark, relatório e persistência |
| `exception` | Exceções de domínio (`RepositoryException`), preservando a causa original |

> 💡 O ponto mais interessante para quem for revisar o código: há duas versões do ingestor (`TransactionIngestor` x `EfficientTransactionIngestor`) e três implementações de repositório, propositalmente, para deixar visível o trade-off entre uma solução ingênua e uma otimizada.

## ⚙️ Funcionalidades

- Ingestão e análise de transações do dataset PaySim
- Processamento de arquivos com **Java NIO/Streams** e leitura lazy (`Files.lines`), sem carregar o arquivo inteiro em memória
- Relatórios internacionalizados em **PT-BR** e **EN-US** via `ResourceBundle` e `NumberFormat`
- Persistência de transações com **JDBC** e MySQL
- Inserção em lote (**JDBC Batch**) com controle transacional (commit/rollback)
- Processamento concorrente com `ExecutorService` (`ThreadPoolExecutor` + `CallerRunsPolicy`)
- Comparação de performance de busca entre `List` e `Map`

## 🧰 Tecnologias

- **Java 25**, incluindo o recurso finalizado pela JEP 512 (*Compact Source Files and Instance Main Methods*, `IO.println`)
- Maven
- MySQL 8.4
- Docker / Docker Compose
- JDBC
- Java NIO / Streams
- `ExecutorService`
- `ResourceBundle` / i18n

## 🗃️ Dataset

O projeto utiliza o dataset **[PaySim](LINK_DO_DATASET_AQUI)**, um simulador de transações financeiras móveis:

- **6.362.620** transações
- **8.213** transações fraudulentas
- Valor total processado: **1.144.392.944.759,77**

> O arquivo `dataset.csv` não está versionado no repositório por seu tamanho. Baixe-o e salve em `data/dataset.csv` antes de executar o projeto.

## 🚀 Como executar

1. Clone o repositório
2. Suba o banco de dados: `docker-compose up -d`
3. Crie a tabela executando `script.sql` no MySQL (porta `3307`)
4. Baixe o dataset PaySim e salve em `data/dataset.csv`
5. Compile o projeto com Maven
6. Execute a classe correspondente ao cenário desejado:
    - `IngestionMain` — ingestão concorrente com persistência em lote
    - `TransactionReportMain pt` / `TransactionReportMain en` — relatório internacionalizado
    - `TransactionBenchmarkMain` — comparação de busca `List` x `Map`
    - `DBMain` — ingestão simples + busca no banco

## 📊 Benchmark

Execução com processamento concorrente e inserção em lote:

| Parâmetro | Valor |
|---|---|
| Batch size | 10.000 transações |
| Threads | 32 |
| Transações processadas | 6.362.620 |
| Tempo total | ~109 segundos |

Os dados persistidos no MySQL foram validados diretamente contra o arquivo CSV de origem.

## 🎓 Origem

Projeto desenvolvido a partir do repositório-base **Zenon Bank**, disponibilizado pelo professor na disciplina de Fundamentos Java da pós-graduação em Java da UNIPDS, e evoluído ao longo das atividades práticas do módulo. o passo a passo do quadro de atividades (Trello) do projeto prático dessa disciplina, disponível nos vídeos de projeto prático da disciplina.

## 📦 Requisitos

* **Git e GitHub**: para controle de versões e portifólio de repositórios.
* **Java JDK**: 25 (LTS).
* **IDE**: IntelliJ IDEA ou Eclipse.
* **Docker e Docker Compose**: Para a tarefa de Banco de Dados.
