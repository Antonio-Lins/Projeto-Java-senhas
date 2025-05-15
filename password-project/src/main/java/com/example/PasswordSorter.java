package com.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PasswordSorter {
    public static void main(String[] args) throws IOException {
        String inputPath = "passwords_formated_data.csv";
        List<String[]> dados = readCSV(inputPath);

        String[] algoritmos = { "insertion", "selection", "merge", "quick", "quickMediana", "counting", "heap" };
        String[] casos = { "melhorCaso", "medioCaso", "piorCaso" };
        String[] criterios = { "length", "month", "data" };

        for (String criterio : criterios) {
            for (String algoritmo : algoritmos) {
                if (!isAlgoritmoValido(algoritmo, criterio)) continue;
                for (String caso : casos) {
                    List<String[]> copia = new ArrayList<>(dados);
                    sortData(copia, algoritmo, criterio);
                    String nome = String.format("passwords_%s_%s_%s.csv", criterio, algoritmo, caso);
                    writeCSV(nome, copia);
                }
            }
        }
        System.out.println("arquivos gerados com sucesso!");
    }

    public static List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> linhas = new ArrayList<>();
        List<String> allLines = Files.readAllLines(Paths.get(filePath));
        for (String linha : allLines) {
            if (linha.trim().isEmpty() || linha.startsWith("\"\"")) continue; // pula cabeçalho e vazias
            String[] partes = linha.split("\",\"");
            if (partes.length < 5) continue;
            for (int i = 0; i < partes.length; i++) {
                partes[i] = partes[i].replace("\"", ""); // remove aspas
            }
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

    public static void sortData(List<String[]> dados, String algoritmo, String criterio) {
        Comparator<String[]> comparator = switch (criterio) {
            case "length" -> Comparator.comparingInt(o -> Integer.parseInt(o[2]));
            case "month" -> Comparator.comparingInt(o -> Integer.parseInt(o[3].split("/")[1]));
            case "data" -> Comparator.comparing(o -> {
                String[] partes = o[3].split("/");
                return String.format("%04d%02d%02d", Integer.parseInt(partes[2]), Integer.parseInt(partes[1]), Integer.parseInt(partes[0]));
            });
            default -> throw new IllegalArgumentException("Critério inválido");
        };

        switch (algoritmo) {
            case "insertion" -> insertionSort(dados, comparator);
            case "selection" -> selectionSort(dados, comparator);
            case "merge" -> mergeSort(dados, comparator);
            case "quick" -> quickSort(dados, comparator, 0, dados.size() - 1);
            case "quickMediana" -> quickSortMediana(dados, comparator, 0, dados.size() - 1);
            case "counting" -> {
                if (criterio.equals("length"))
                    countingSortLength(dados);
            }
            case "heap" -> heapSort(dados, comparator);
        }

        // length em ordem decrescente
        if (criterio.equals("length")) Collections.reverse(dados);
    }

    public static boolean isAlgoritmoValido(String algoritmo, String criterio) {
        return switch (algoritmo) {
            case "insertion", "selection", "merge", "quick", "heap" -> true;
            case "quickMediana" -> !criterio.equals("data");
            case "counting" -> criterio.equals("length");
            default -> false;
        };
    }

    // === ALGORITMOS ===
    public static void insertionSort(List<String[]> list, Comparator<String[]> comp) {
        for (int i = 1; i < list.size(); i++) {
            String[] key = list.get(i);
            int j = i - 1;
            while (j >= 0 && comp.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    public static void selectionSort(List<String[]> list, Comparator<String[]> comp) {
        for (int i = 0; i < list.size() - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < list.size(); j++) {
                if (comp.compare(list.get(j), list.get(minIdx)) < 0) {
                    minIdx = j;
                }
            }
            Collections.swap(list, i, minIdx);
        }
    }

    public static void mergeSort(List<String[]> list, Comparator<String[]> comp) {
        if (list.size() <= 1) return;
        int mid = list.size() / 2;
        List<String[]> left = new ArrayList<>(list.subList(0, mid));
        List<String[]> right = new ArrayList<>(list.subList(mid, list.size()));
        mergeSort(left, comp);
        mergeSort(right, comp);
        merge(list, left, right, comp);
    }

    public static void merge(List<String[]> list, List<String[]> left, List<String[]> right, Comparator<String[]> comp) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (comp.compare(left.get(i), right.get(j)) <= 0) {
                list.set(k++, left.get(i++));
            } else {
                list.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) list.set(k++, left.get(i++));
        while (j < right.size()) list.set(k++, right.get(j++));
    }

    public static void quickSort(List<String[]> list, Comparator<String[]> comp, int low, int high) {
        if (low < high) {
            int pi = partition(list, comp, low, high);
            quickSort(list, comp, low, pi - 1);
            quickSort(list, comp, pi + 1, high);
        }
    }

    public static int partition(List<String[]> list, Comparator<String[]> comp, int low, int high) {
        String[] pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comp.compare(list.get(j), pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    public static void quickSortMediana(List<String[]> list, Comparator<String[]> comp, int low, int high) {
        if (low < high) {
            int pi = partitionMediana(list, comp, low, high);
            quickSortMediana(list, comp, low, pi - 1);
            quickSortMediana(list, comp, pi + 1, high);
        }
    }

    public static int partitionMediana(List<String[]> list, Comparator<String[]> comp, int low, int high) {
        int mid = (low + high) / 2;
        String[] a = list.get(low);
        String[] b = list.get(mid);
        String[] c = list.get(high);
        String[] pivot = medianOfThree(a, b, c, comp);
        int pivotIndex = list.indexOf(pivot);
        Collections.swap(list, pivotIndex, high);
        return partition(list, comp, low, high);
    }

    public static String[] medianOfThree(String[] a, String[] b, String[] c, Comparator<String[]> comp) {
        if (comp.compare(a, b) < 0) {
            if (comp.compare(b, c) < 0) return b;
            else if (comp.compare(a, c) < 0) return c;
            else return a;
        } else {
            if (comp.compare(a, c) < 0) return a;
            else if (comp.compare(b, c) < 0) return c;
            else return b;
        }
    }

    public static void countingSortLength(List<String[]> list) {
        int max = list.stream().mapToInt(e -> Integer.parseInt(e[2])).max().orElse(0);
        List<List<String[]>> buckets = new ArrayList<>();
        for (int i = 0; i <= max; i++) buckets.add(new ArrayList<>());
        for (String[] entry : list) {
            int len = Integer.parseInt(entry[2]);
            buckets.get(len).add(entry);
        }
        list.clear();
        for (List<String[]> bucket : buckets) {
            list.addAll(bucket);
        }
    }

    public static void heapSort(List<String[]> list, Comparator<String[]> comp) {
        int n = list.size();
        for (int i = n / 2 - 1; i >= 0; i--) heapify(list, n, i, comp);
        for (int i = n - 1; i > 0; i--) {
            Collections.swap(list, 0, i);
            heapify(list, i, 0, comp);
        }
    }

    public static void heapify(List<String[]> list, int n, int i, Comparator<String[]> comp) {
        int largest = i;
        int left = 2 * i + 1, right = 2 * i + 2;
        if (left < n && comp.compare(list.get(left), list.get(largest)) > 0) largest = left;
        if (right < n && comp.compare(list.get(right), list.get(largest)) > 0) largest = right;
        if (largest != i) {
            Collections.swap(list, i, largest);
            heapify(list, n, largest, comp);
        }
    }
}
