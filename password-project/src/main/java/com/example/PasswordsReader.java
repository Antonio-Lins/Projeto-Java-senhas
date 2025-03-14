package com.example;

import com.opencsv.CSVReader; // Adicionando a importação da classe CSVReader
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PasswordsReader {
    public static void main(String[] args) throws CsvException {
        String csvFile = "passwords.csv"; // Caminho do seu arquivo CSV
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> records = reader.readAll(); // Lê todas as linhas do arquivo CSV
            for (String[] record : records) {
                // Aqui você pode acessar cada campo das senhas
                System.out.println("Senha: " + record[0] + " | Data: " + record[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}