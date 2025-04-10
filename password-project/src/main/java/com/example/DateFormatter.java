package com.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateFormatter {
    public static void main(String[] args) throws IOException, CsvException {
        String inputFile = "password_classifier.csv";
        String outputFileFormatted = "passwords_formated_data.csv";
        String outputFileFiltered = "passwords_classifier.csv";

        try (
            CSVReader reader = new CSVReader(new FileReader(inputFile));
            CSVWriter writerFormatted = new CSVWriter(new FileWriter(outputFileFormatted));
            CSVWriter writerFiltered = new CSVWriter(new FileWriter(outputFileFiltered))
        ) {
            List<String[]> records = reader.readAll();

            if (records.isEmpty()) {
                System.err.println("O arquivo CSV está vazio.");
                return;
            }

            // cabeçalho
            String[] header = records.remove(0);
            writerFormatted.writeNext(header);
            writerFiltered.writeNext(header);

            for (String[] record : records) {
                if (record.length >= 5) {
                    // formatar data (coluna 3 -> índice 3)
                    String formattedDate = formatDate(record[3]);
                    record[3] = formattedDate;

                    // grava no arquivo de datas formatadas
                    writerFormatted.writeNext(record);

                    // verifica classificação
                    String classification = record[4].toLowerCase();
                    if (classification.equals("boa") || classification.equals("muito boa")) {
                        writerFiltered.writeNext(record); // grava no arquivo filtrado
                    }
                } else {
                    System.err.println("Linha mal formatada: " + Arrays.toString(record));
                }
            }

            System.out.println("✅ Arquivos gerados com sucesso:");
            System.out.println("- " + outputFileFormatted);
            System.out.println("- " + outputFileFiltered);
        }
    }

    public static String formatDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            System.err.println("Erro ao formatar data: " + date);
            return date;
        }
    }
}
