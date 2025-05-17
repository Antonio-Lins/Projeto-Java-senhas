package com.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PasswordSorter {
    public static void main(String[] args) throws IOException {
        String inputPath = "passwords_formated_data.csv";
        String[][] dados = readCSV(inputPath);

        String[] algoritmos = { "insertion", "selection", "merge", "quick", "quickMediana", "counting", "heap" };
        String[] casos = { "melhorCaso", "medioCaso", "piorCaso" };
        String[] criterios = { "length", "month", "data" };

        for (String criterio : criterios) {
            for (String algoritmo : algoritmos) {
                if (!isAlgoritmoValido(algoritmo, criterio)) continue;
                for (String caso : casos) {
                    String[][] copia = Arrays.copyOf(dados, dados.length);
                    sortData(copia, algoritmo, criterio);
                    String nome = String.format("passwords_%s_%s_%s.csv", criterio, algoritmo, caso);
                    writeCSV(nome, copia);
                }
            }
        }
        System.out.println("arquivos gerados com sucesso!");
    }

    public static String[][] readCSV(String filePath) throws IOException {
        List<String> allLines = Files.readAllLines(Paths.get(filePath));
        String[][] linhas = new String[allLines.size()][];
        int idx = 0;
        for (String linha : allLines) {
            if (linha.trim().isEmpty() || linha.startsWith("\"\"")) continue; // pula cabeçalho e vazias
            String[] partes = linha.split("\",\"");
            if (partes.length < 5) continue;
            for (int i = 0; i < partes.length; i++) {
                partes[i] = partes[i].replace("\"", ""); // remove aspas
            }
            linhas[idx++] = partes;
        }
        return Arrays.copyOf(linhas, idx);
    }

    public static void writeCSV(String nomeArquivo, String[][] dados) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(nomeArquivo))) {
            for (String[] linha : dados) {
                writer.write(String.join(";", linha));
                writer.newLine();
            }
        }
    }

    public static void sortData(String[][] dados, String algoritmo, String criterio) {
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
            case "quick" -> quickSort(dados, comparator, 0, dados.length - 1);
            case "quickMediana" -> quickSortMediana(dados, comparator, 0, dados.length - 1);
            case "counting" -> {
                if (criterio.equals("length"))
                    countingSortLength(dados);
            }
            case "heap" -> heapSort(dados, comparator);
        }

        // length em ordem decrescente
        if (criterio.equals("length")) {
            Arrays.sort(dados, Comparator.comparingInt(o -> -Integer.parseInt(o[2])));
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

    // === ALGORITMOS ===
    public static void insertionSort(String[][] array, Comparator<String[]> comp) {
        for (int i = 1; i < array.length; i++) {
            String[] key = array[i];
            int j = i - 1;
            while (j >= 0 && comp.compare(array[j], key) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    public static void selectionSort(String[][] array, Comparator<String[]> comp) {
        for (int i = 0; i < array.length - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < array.length; j++) {
                if (comp.compare(array[j], array[minIdx]) < 0) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                String[] temp = array[i];
                array[i] = array[minIdx];
                array[minIdx] = temp;
            }
        }
    }

    public static void mergeSort(String[][] array, Comparator<String[]> comp) {
        if (array.length <= 1) return;
        int mid = array.length / 2;
        String[][] left = Arrays.copyOfRange(array, 0, mid);
        String[][] right = Arrays.copyOfRange(array, mid, array.length);
        mergeSort(left, comp);
        mergeSort(right, comp);
        merge(array, left, right, comp);
    }

    public static void merge(String[][] array, String[][] left, String[][] right, Comparator<String[]> comp) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (comp.compare(left[i], right[j]) <= 0) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }
        while (i < left.length) array[k++] = left[i++];
        while (j < right.length) array[k++] = right[j++];
    }

    public static void quickSort(String[][] array, Comparator<String[]> comp, int low, int high) {
        if (low < high) {
            int pi = partition(array, comp, low, high);
            quickSort(array, comp, low, pi - 1);
            quickSort(array, comp, pi + 1, high);
        }
    }

    public static int partition(String[][] array, Comparator<String[]> comp, int low, int high) {
        String[] pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comp.compare(array[j], pivot) <= 0) {
                i++;
                String[] temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        String[] temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }

    public static void quickSortMediana(String[][] array, Comparator<String[]> comp, int low, int high) {
        if (low < high) {
            int pi = partitionMediana(array, comp, low, high);
            quickSortMediana(array, comp, low, pi - 1);
            quickSortMediana(array, comp, pi + 1, high);
        }
    }

    public static int partitionMediana(String[][] array, Comparator<String[]> comp, int low, int high) {
        int mid = (low + high) / 2;
        String[] a = array[low];
        String[] b = array[mid];
        String[] c = array[high];
        String[] pivot = medianOfThree(a, b, c, comp);
        int pivotIndex = Arrays.asList(array).indexOf(pivot);
        String[] temp = array[pivotIndex];
        array[pivotIndex] = array[high];
        array[high] = temp;
        return partition(array, comp, low, high);
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

    public static void countingSortLength(String[][] array) {
        int max = Arrays.stream(array).mapToInt(e -> Integer.parseInt(e[2])).max().orElse(0);
        List<String[]>[] buckets = new List[max + 1];
        for (int i = 0; i <=
max; i++) buckets[i] = new ArrayList<>();
for (String[] e : array) {
int length = Integer.parseInt(e[2]);
buckets[length].add(e);
}
int idx = 0;
for (List<String[]> bucket : buckets) {
for (String[] e : bucket) array[idx++] = e;
}
}
public static void heapSort(String[][] array, Comparator<String[]> comp) {
    int n = array.length;
    for (int i = n / 2 - 1; i >= 0; i--) heapify(array, n, i, comp);
    for (int i = n - 1; i > 0; i--) {
        String[] temp = array[0];
        array[0] = array[i];
        array[i] = temp;
        heapify(array, i, 0, comp);
    }
}

public static void heapify(String[][] array, int n, int i, Comparator<String[]> comp) {
    int largest = i;
    int l = 2 * i + 1, r = 2 * i + 2;
    if (l < n && comp.compare(array[l], array[largest]) > 0) largest = l;
    if (r < n && comp.compare(array[r], array[largest]) > 0) largest = r;
    if (largest != i) {
        String[] swap = array[i];
        array[i] = array[largest];
        array[largest] = swap;
        heapify(array, n, largest, comp);
    }
}
}
