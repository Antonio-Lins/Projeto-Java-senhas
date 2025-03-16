package com.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PasswordFilter {
    public static void main(String[] args) throws IOException, CsvException {
        String inputFile = "password_classifier.csv"; // Arquivo de entrada
        String outputFile = "passwords_classifier.csv"; // Arquivo de saída

        System.out.println("Iniciando filtragem de senhas...");

        try (CSVReader reader = new CSVReader(new FileReader(inputFile));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {

            // Lê todas as linhas do arquivo CSV
            List<String[]> records = reader.readAll();
            System.out.println("Total de registros lidos: " + records.size());

            if (records.isEmpty()) {
                System.err.println("O arquivo CSV está vazio. Verifique o arquivo de entrada.");
                return;
            }

            // Remove o cabeçalho se existir
            String[] header = records.remove(0);

            // Filtra as senhas classificadas como "Boa" ou "Muito Boa"
            List<String[]> filteredRecords = new ArrayList<>();
            for (String[] record : records) {
                // Verifica se a linha tem pelo menos 5 colunas (índice 4 é válido)
                if (record.length >= 5) {
                    String classification = record[4]; // Classificação está na quinta coluna
                    if ("Boa".equalsIgnoreCase(classification) || "Muito Boa".equalsIgnoreCase(classification)) {
                        filteredRecords.add(record);
                    }
                } else {
                    System.err.println("Linha mal formatada (" + record.length + " colunas): " + String.join(", ", record));
                }
            }

            System.out.println("Total de registros filtrados: " + filteredRecords.size());

            // Escreve o cabeçalho novamente
            writer.writeNext(header);

            // Escreve os registros filtrados no arquivo de saída
            writer.writeAll(filteredRecords);
            System.out.println("Filtragem concluída. Arquivo gerado: " + outputFile);
        }
    }
}
