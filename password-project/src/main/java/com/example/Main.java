package com.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando o programa...");

        // Ler o arquivo CSV
        List<String[]> records = lerCSV("passwords.csv");

        // Classificar as senhas
        classificarSenhas(records);
    }

    // Função para ler o arquivo CSV
    public static List<String[]> lerCSV(String csvFile) {
        System.out.println("Lendo o arquivo CSV...");
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> records = reader.readAll();
            System.out.println("Arquivo CSV lido com sucesso!");
            return records;
        } catch (IOException | CsvException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
            return null;
        }
    }

    // Função para classificar as senhas
    public static void classificarSenhas(List<String[]> records) {
        if (records == null) {
            System.err.println("Nenhum dado para classificar.");
            return;
        }

        System.out.println("Classificando as senhas...");
        for (String[] record : records) {
            String password = record[0]; // Supondo que a senha está na primeira coluna
            String classification = classifyPassword(password);
            System.out.println("Senha: " + password + " | Classificação: " + classification);
        }
    }

    // Função para classificar uma senha
    public static String classifyPassword(String password) {
        // Aqui você implementa a lógica de classificação das senhas
        // Exemplo básico:
        if (password.length() < 5) {
            return "Muito Ruim";
        } else if (password.length() <= 6) {
            return "Fraca";
        } else {
            return "Boa";
        }
    }
}