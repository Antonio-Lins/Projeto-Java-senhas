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

caso utilize maven, adicione a dependência no `pom.xml`:

```xml
<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.7.1</version>
</dependency>
```

---

## 💻 execução passo a passo

1. clone ou baixe o projeto.
2. certifique-se de ter o arquivo `passwords.csv` no diretório raiz.
3. compile os arquivos java com:

```bash
javac -cp ".:path/to/opencsv.jar" src/main/java/com/example/*.java
```

4. execute o classificador:

```bash
java -cp ".:path/to/opencsv.jar:src/main/java" com.example.PasswordClassifier
```

5. execute o formatador:

```bash
java -cp ".:path/to/opencsv.jar:src/main/java" com.example.DateFormatter
```

🔁 substitua `path/to/opencsv.jar` pelo caminho da lib opencsv no seu sistema.

---

## 🧪 exemplos de entrada e saída

### 📥 entrada (`passwords.csv`)

```csv
id,username,password,created_at
1,joao123,senha123,2023-06-15 14:23:10
2,ana_b,Abc@2023,2022-09-10 09:11:47
3,lucasx,lucasx,2021-01-02 22:00:00
```

---

## 📤 saída 1: `password_classifier.csv`

arquivo com coluna adicional `"class"`:

```csv
id,username,password,created_at,class
1,joao123,senha123,2023-06-15 14:23:10,fraca
2,ana_b,Abc@2023,2022-09-10 09:11:47,muito boa
3,lucasx,lucasx,2021-01-02 22:00:00,ruim
```

---

## 📤 saída 2: `passwords_formated_data.csv`

arquivo com data formatada:

```csv
id,username,password,created_at,class
1,joao123,senha123,15/06/2023,fraca
2,ana_b,Abc@2023,10/09/2022,muito boa
3,lucasx,lucasx,02/01/2021,ruim
```

---

## 📤 saída 3: `passwords_classifier.csv`

apenas senhas boas ou muito boas:

```csv
id,username,password,created_at,class
2,ana_b,Abc@2023,10/09/2022,muito boa
```

---

## 📁 estrutura esperada

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

---
