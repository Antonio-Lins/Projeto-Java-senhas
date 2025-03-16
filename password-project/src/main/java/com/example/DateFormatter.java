package com.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class DateFormatter {
    public static void main(String[] args) throws IOException, CsvException {
        String inputFile = "password_classifier.csv";
        String outputFile = "passwords_formated_data.csv";

        try (CSVReader reader = new CSVReader(new FileReader(inputFile));
             CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {

            List<String[]> records = reader.readAll();
            
            if (records.isEmpty()) {
                System.err.println("O arquivo CSV está vazio.");
                return;
            }

            // Remove o cabeçalho
            String[] header = records.remove(0);

            for (String[] record : records) {
                if (record.length >= 4) { // Verifica se há pelo menos 4 colunas
                    String date = record[3]; // Data está na quarta coluna
                    record[3] = formatDate(date); // Formata a data
                } else {
                    System.err.println("Linha mal formatada: " + String.join(", ", record));
                }
            }

            // Escreve o cabeçalho e os dados formatados
            writer.writeNext(header);
            writer.writeAll(records);

            System.out.println("Datas formatadas e arquivo gerado: " + outputFile);
        }
    }

    public static String formatDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            System.err.println("Erro ao formatar data: " + date);
            return date; // Retorna a data original em caso de erro
        }
    }
}
