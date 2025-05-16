package com.example;

import com.example.sorters.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PasswordSorter {
    public static void main(String[] args) throws IOException {
        String inputPath = "passwords_formated_data.csv";
        List<String[]> dados = readCSV(inputPath);

        Map<String, Sorter> algoritmos = Map.of(
            "insertion", new InsertionSorter(),
            "selection", new SelectionSorter(),
            "merge", new MergeSorter(),
            "quick", new QuickSorter(),
            "quickMediana", new QuickMedianaSorter(),
            "counting", new CountingSorter(),
            "heap", new HeapSorter()
        );

        String[] criterios = { "length", "month", "data" };
        String[] casos = { "melhorCaso", "medioCaso", "piorCaso" };

        for (String criterio : criterios) {
            Comparator<String[]> comparator = switch (criterio) {
                case "length" -> Comparator.comparingInt(o -> Integer.parseInt(o[2]));
                case "month" -> Comparator.comparingInt(o -> Integer.parseInt(o[3].split("/")[1]));
                case "data" -> Comparator.comparing(o -> {
                    String[] p = o[3].split("/");
                    return String.format("%04d%02d%02d", Integer.parseInt(p[2]), Integer.parseInt(p[1]), Integer.parseInt(p[0]));
                });
                default -> throw new IllegalArgumentException("Critério inválido");
            };

            for (Map.Entry<String, Sorter> entry : algoritmos.entrySet()) {
                String nomeAlgoritmo = entry.getKey();
                if (!isAlgoritmoValido(nomeAlgoritmo, criterio)) continue;
                for (String caso : casos) {
                    List<String[]> copia = new ArrayList<>();
                    for (String[] linha : dados) copia.add(Arrays.copyOf(linha, linha.length));
                    Sorter sorter = entry.getValue();
                    sorter.sort(copia, comparator);
                    if (criterio.equals("length")) Collections.reverse(copia);
                    String nome = String.format("passwords_%s_%s_%s.csv", criterio, nomeAlgoritmo, caso);
                    writeCSV(nome, copia);
                }
            }
        }

        System.out.println("arquivos gerados com sucesso!");
    }

    public static List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> linhas = new ArrayList<>();
        for (String linha : Files.readAllLines(Paths.get(filePath))) {
            if (linha.trim().isEmpty() || linha.startsWith("\"\"")) continue;
            String[] partes = linha.split("\",\"");
            if (partes.length < 5) continue;
            for (int i = 0; i < partes.length; i++) partes[i] = partes[i].replace("\"", "");
            linhas.add(partes);
        }
        return linhas;
    }

    public static void writeCSV(String nomeArquivo, List<String[]> dados) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(nomeArquivo))) {
            for (String[] linha : dados) {
                writer.write(String.join(";", linha));
                writer.newLine();
            }
        }
    }

    public static boolean isAlgoritmoValido(String algoritmo, String criterio) {
        return switch (algoritmo) {
            case "insertion", "selection", "merge", "quick", "heap" -> true;
            case "quickMediana" -> !criterio.equals("data");
            case "counting" -> criterio.equals("length");
            default -> false;
        };
    }
}
