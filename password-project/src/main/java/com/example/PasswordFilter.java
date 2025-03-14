package com.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.*;
import java.util.*;

public class PasswordFilter {
    public static void main(String[] args) throws IOException, CsvException {
        String inputFile = "password_classifier.csv";
        String outputFile = "passwords_classifier.csv";

        try (CSVReader reader = new CSVReader(new FileReader(inputFile));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {
            List<String[]> records = reader.readAll();
            List<String[]> filteredRecords = new ArrayList<>();

            for (String[] record : records) {
                String classification = record[2];
                if ("Boa".equals(classification) || "Muito Boa".equals(classification)) {
                    filteredRecords.add(record);
                }
            }

            writer.writeAll(filteredRecords); // Escreve no arquivo de saída
        }
    }
}