package com.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PasswordSorter {

    public static void main(String[] args) throws IOException, CsvException {
        String inputFile = "passwords_formated_data.csv";

        List<String[]> originalData;
        try (CSVReader reader = new CSVReader(new FileReader(inputFile))) {
            originalData = reader.readAll();
        }

        if (originalData.isEmpty()) {
            System.err.println("arquivo csv vazio.");
            return;
        }

        String[] header = originalData.get(0);
        originalData.remove(0); // remove o cabeçalho

        // gerar para length
        gerarArquivosOrdenados(originalData, header, "length", Comparator.comparingInt(a -> Integer.parseInt(a[2].replaceAll("\"", "").trim())));
        // gerar para data completa
        gerarArquivosOrdenados(originalData, header, "data", Comparator.comparing(a -> parseData(a[3])));
        // gerar para mês
        gerarArquivosOrdenados(originalData, header, "month", Comparator.comparing(a -> extrairMes(a[3])));
    }

    private static void gerarArquivosOrdenados(List<String[]> originalData, String[] header, String campo, Comparator<String[]> comparatorBase) {
        Map<String, Comparator<String[]>> casos = new HashMap<>();
        casos.put("melhorCaso", comparatorBase);
        casos.put("medioCaso", comparatorBase.reversed());
        casos.put("piorCaso", (a, b) -> 0); // nenhum swap: pior para bubble/insertion

        for (String algoritmo : List.of("insertionSort", "selectionSort", "bubbleSort")) {
            for (Map.Entry<String, Comparator<String[]>> entry : casos.entrySet()) {
                String tipoCaso = entry.getKey();
                Comparator<String[]> comparator = entry.getValue();

                List<String[]> copia = new ArrayList<>(originalData); // clona os dados
                copia.sort(comparator); // ordena antes de aplicar algoritmo (simula os casos)

                // aplica o algoritmo real (só pra simular o processo real)
                switch (algoritmo) {
                    case "insertionSort" -> insertionSort(copia, comparatorBase);
                    case "selectionSort" -> selectionSort(copia, comparatorBase);
                    case "bubbleSort" -> bubbleSort(copia, comparatorBase);
                }

                // escreve o resultado
                String nomeArquivo = String.format("passwords_%s_%s_%s.csv", campo, algoritmo, tipoCaso);
                try (CSVWriter writer = new CSVWriter(new FileWriter(nomeArquivo))) {
                    writer.writeNext(header);
                    writer.writeAll(copia);
                    System.out.println("arquivo gerado: " + nomeArquivo);
                } catch (IOException e) {
                    System.err.println("erro ao escrever o arquivo: " + nomeArquivo);
                }
            }
        }
    }

    private static void insertionSort(List<String[]> list, Comparator<String[]> comparator) {
        for (int i = 1; i < list.size(); i++) {
            String[] key = list.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    private static void selectionSort(List<String[]> list, Comparator<String[]> comparator) {
        for (int i = 0; i < list.size() - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < list.size(); j++) {
                if (comparator.compare(list.get(j), list.get(minIdx)) < 0) {
                    minIdx = j;
                }
            }
            String[] temp = list.get(i);
            list.set(i, list.get(minIdx));
            list.set(minIdx, temp);
        }
    }

    private static void bubbleSort(List<String[]> list, Comparator<String[]> comparator) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    String[] temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    private static Date parseData(String s) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(s);
        } catch (ParseException e) {
            return new Date(0); // caso erro, data mínima
        }
    }

    private static int extrairMes(String data) {
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(data);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.MONTH); // 0 = janeiro
        } catch (ParseException e) {
            return -1;
        }
    }
}
