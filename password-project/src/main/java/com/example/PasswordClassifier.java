package com.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileWriter;
import java.util.List;

public class PasswordClassifier {
    public static void main(String[] args) throws IOException, CsvException {
        String inputFile = "passwords.csv"; // nome do arquivo no classpath
        String outputFile = "password_classifier.csv";

        // carrega o arquivo do classpath
        try (InputStream inputStream = PasswordClassifier.class.getClassLoader().getResourceAsStream(inputFile);
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {

            if (inputStream == null) {
                System.err.println("arquivo não encontrado: " + inputFile);
                return;
            }

            List<String[]> records = reader.readAll();
            System.out.println("total de registros lidos: " + records.size()); // log para depuração

            if (records.isEmpty()) {
                System.err.println("o arquivo csv está vazio.");
                return;
            }

            // adiciona o cabeçalho da nova coluna
            String[] header = records.get(0);
            String[] newHeader = new String[header.length + 1];
            System.arraycopy(header, 0, newHeader, 0, header.length);
            newHeader[header.length] = "class";
            records.set(0, newHeader);

            // classifica as senhas
            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);

                // garantir que há pelo menos três colunas (índice, senha, tamanho)
                if (record.length < 3) { 
                    System.err.println("linha inválida encontrada no índice: " + i);
                    continue;
                }

                String password = record[1].trim(); // pega a senha corretamente da segunda coluna

                String classification = classifyPassword(password);
                System.out.println("senha: " + password + " | classificação: " + classification);

                String[] newRecord = new String[record.length + 1];
                System.arraycopy(record, 0, newRecord, 0, record.length);
                newRecord[record.length] = classification;
                records.set(i, newRecord);
            }

            writer.writeAll(records); // escreve no arquivo de saída
            System.out.println("classificação concluída. arquivo gerado: " + outputFile);
        }
    }

    public static String classifyPassword(String password) {
        int length = password.length();
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");

        int types = 0;
        if (hasLetter) types++;
        if (hasNumber) types++;
        if (hasSpecial) types++;

        // log para depuração
        System.out.println("analisando senha: " + password + " | tamanho: " + length + 
            " | letras: " + hasLetter + " | números: " + hasNumber + " | especiais: " + hasSpecial + 
            " | tipos: " + types);

        // aplicar regras corretamente
        if (length < 5 && types == 1) return "muito ruim";
        if (length <= 5 && types == 1) return "ruim";
        if (length <= 6 && types == 2) return "fraca";
        if (length <= 7 && types == 3) return "boa";
        if (length > 8 && types == 3) return "muito boa";

        return "sem classificação";
    }
}
