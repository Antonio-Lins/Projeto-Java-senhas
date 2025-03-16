package com.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.*;
import java.util.*;

public class PasswordSorter {
    public static void main(String[] args) throws IOException, CsvException {
        String inputFile = "passwords_formated_data.csv";
        String outputFile = "passwords_sorted.csv";

        try (CSVReader reader = new CSVReader(new FileReader(inputFile));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {

            List<String[]> records = reader.readAll();

            if (records.isEmpty()) {
                System.err.println("O arquivo CSV está vazio.");
                return;
            }

            // separa o cabeçalho dos dados
            String[] header = records.get(0);
            records.remove(0);

            // valida se o cabeçalho contém "length"
            if (header.length < 3 || !"length".equalsIgnoreCase(header[2].trim())) {
                System.err.println("Erro: Cabeçalho inesperado no CSV.");
                return;
            }

            // filtra e ordena os dados
            List<String[]> validRecords = new ArrayList<>();
            for (String[] row : records) {
                if (row.length > 2 && row[2] != null && !row[2].trim().isEmpty()) {
                    try {
                        int length = Integer.parseInt(row[2].replaceAll("\"", "").trim()); // remove aspas e converte
                        validRecords.add(row);
                    } catch (NumberFormatException e) {
                        System.err.println("Linha ignorada (valor inválido na coluna 'length'): " + Arrays.toString(row));
                    }
                }
            }

            // ordena do maior para o menor
            validRecords.sort((a, b) -> {
                int lenA = Integer.parseInt(a[2].replaceAll("\"", "").trim());
                int lenB = Integer.parseInt(b[2].replaceAll("\"", "").trim());
                return Integer.compare(lenB, lenA);
            });

            // escreve o cabeçalho e os dados ordenados
            writer.writeNext(header);
            writer.writeAll(validRecords);
        }
        System.out.println("Ordenação concluída. Arquivo gerado: " + outputFile);
    }
}
