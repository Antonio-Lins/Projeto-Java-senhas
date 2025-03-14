package com.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CSVReaderExample {

    public List<String[]> readCSV(String filePath) throws IOException, CsvException {
        // Cria um leitor CSV para o arquivo especificado
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            // Lê todas as linhas do arquivo CSV
            return reader.readAll();
        }
    }

    public static void main(String[] args) {
        String filePath = "src/main/resources/passwords.csv"; // Caminho do arquivo CSV

        CSVReaderExample csvReader = new CSVReaderExample();
        try {
            // Lê o arquivo CSV
            List<String[]> records = csvReader.readCSV(filePath);

            // Exibe as linhas do arquivo CSV
            for (String[] record : records) {
                for (String field : record) {
                    System.out.print(field + " | ");
                }
                System.out.println();
            }
        } catch (IOException | CsvException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        }
    }
}