# 💻 T2 - Projeto Passwords

## 📋 descrição

este projeto tem como objetivo aplicar transformações e classificações em um dataset contendo mais de 600 mil senhas, com o propósito de estudar o desempenho de algoritmos de ordenação, utilizando apenas arrays como estrutura de dados.

as operações envolvem:

- **classificação** de senhas com base em critérios de segurança.
- **formatação** de datas para o padrão brasileiro.
- **filtragem** das senhas classificadas como "boa" e "muito boa".

---

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
├── passwords.csv                       # arquivo original com as senhas (obtido via kaggle)
├── password_classifier.csv             # saída com classificação das senhas
├── passwords_formated_data.csv         # saída com datas formatadas
├── passwords_classifier.csv            # saída com senhas "boa" e "muito boa"
├── pom.xml                             # configuração do projeto com Maven (inclui dependência OpenCSV)
└── README.md                           # este arquivo
