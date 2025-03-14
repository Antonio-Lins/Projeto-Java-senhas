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

            // Ordena por comprimento (decrescente)
            records.sort((a, b) -> Integer.compare(b[0].length(), a[0].length()));

            writer.writeAll(records); // Escreve no arquivo de saída
        }
    }
}