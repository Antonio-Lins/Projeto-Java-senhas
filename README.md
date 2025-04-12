# 🔐 t2 - projeto passwords

este projeto em java realiza a leitura, classificação, formatação e filtragem de senhas a partir de um arquivo `.csv`. o objetivo é manipular os dados de maneira eficiente, aplicando regras de classificação de senhas, formatando datas e gerando arquivos de saída organizados.

---

## 📚 estrutura do projeto

o projeto é dividido em duas etapas principais:

### 1. classificação das senhas (`PasswordClassifier.java`)
- lê o arquivo `passwords.csv`
- avalia a força de cada senha com base em:
  - quantidade de caracteres
  - presença de letras, números e caracteres especiais
- adiciona uma nova coluna `"class"` com a classificação:
  - muito ruim
  - ruim
  - fraca
  - boa
  - muito boa
- gera o arquivo `password_classifier.csv`

### 2. formatação de datas e filtragem (`DateFormatter.java`)
- lê o arquivo `password_classifier.csv`
- converte a data da coluna de criação da senha (`yyyy-MM-dd HH:mm:ss`) para o formato brasileiro (`dd/MM/yyyy`)
- filtra apenas senhas classificadas como "boa" ou "muito boa"
- gera dois novos arquivos:
  - `passwords_formated_data.csv` (com a data formatada)
  - `passwords_classifier.csv` (apenas senhas boas e muito boas)

---

## 🧠 arrays utilizados no projeto

neste projeto foram utilizados arrays do tipo `String[]` para representar as linhas dos arquivos csv:

### no `PasswordClassifier.java`
- `String[] header`: representa o cabeçalho original do arquivo.
- `String[] newHeader`: novo cabeçalho com a coluna `"class"`.
- `String[] record`: cada linha lida do csv.
- `String[] newRecord`: cópia da linha com a classificação adicionada.

### no `DateFormatter.java`
- `String[] header`: linha de cabeçalho lida do csv.
- `String[] record`: cada linha de dados que será processada.
- `Arrays.toString(record)`: usado para imprimir linhas mal formatadas no console.

---

## ▶️ como executar o projeto

### ✅ pré-requisitos

- **jdk 17** ou superior
- **biblioteca opencsv**

se estiver usando **maven**, adicione ao `pom.xml`:

```xml
<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.7.1</version>
</dependency>


## 📁 estrutura dos arquivos

```bash
📦 projeto-passwords/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   ├── DateFormatter.java         # formata datas e filtra senhas boas/muito boas
│                   └── PasswordClassifier.java    # classifica senhas do arquivo passwords.csv
├── target/
│   ├── classes/
│   │   └── com/
│   │       └── example/
│   │           ├── DateFormatter.class
│   │           └── PasswordClassifier.class
│   ├── generated-sources/
│   │   └── annotations/
│   ├── maven-status/
│   │   └── maven-compiler-plugin/
│   │       └── compile/
│   │           └── default-compile/
│   │               ├── createdFiles.lst
│   │               └── inputFiles.lst
│   └── test-classes/                             # classes compiladas dos testes (caso existam)
├── passwords.csv                       # arquivo original com as senhas (obtido via kaggle)
├── password_classifier.csv             # saída com classificação das senhas
├── passwords_formated_data.csv         # saída com datas formatadas
├── passwords_classifier.csv            # saída com senhas "boa" e "muito boa"
├── pom.xml                             # configuração do projeto com Maven (inclui dependência OpenCSV)
└── README.md                           # este arquivo
```
