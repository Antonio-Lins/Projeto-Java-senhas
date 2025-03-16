package com.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando o programa...");

        // ler o arquivo CSV
        List<String[]> records = lerCSV("passwords.csv");

        // classificar as senhas
        classificarSenhas(records);
    }

    // função para ler o arquivo CSV
    public static List<String[]> lerCSV(String csvFile) {
        System.out.println("Lendo o arquivo CSV...");

        // tenta carregar o arquivo do classpath
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(csvFile)) {
            if (inputStream == null) {
                System.err.println("Erro: O arquivo CSV não foi encontrado em " + csvFile);
                return null;
            }

            try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
                List<String[]> records = reader.readAll();
                System.out.println("Arquivo CSV lido com sucesso!");
                return records;
            }
        } catch (IOException | CsvException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
            return null;
        }
    }

    // função para classificar as senhas
    public static void classificarSenhas(List<String[]> records) {
        if (records == null) {
            System.err.println("Nenhum dado para classificar.");
            return;
        }

        System.out.println("Classificando as senhas...");
        for (String[] record : records) {
            String password = record[1]; // a senha está na segunda coluna
            String classification = classifyPassword(password);
            System.out.println("Senha: " + password + " | Classificação: " + classification);
        }
    }

    // função para classificar uma senha
    public static String classifyPassword(String password) {
        if (password.length() < 5) {
            return "Muito Ruim";
        } else if (password.length() <= 6) {
            return "Fraca";
        } else {
            return "Boa";
        }
    }
}
