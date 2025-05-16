package com.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PasswordSorter {

    public static void main(String[] args) throws Exception {
        String inputFile = "passwords_formated_data.csv";
        List<String[]> originalData = readCSV(inputFile);
        String[] header = originalData.remove(0);

        String[] algoritmos = {"insertion", "selection", "merge", "quick", "quickMediana", "heap", "counting"};
        String[] criterios = {"length", "month", "date"};
        String[] casos = {"melhor", "medio", "pior"};

        for (String algoritmo : algoritmos) {
            for (String criterio : criterios) {
                if (algoritmo.equals("counting") && !criterio.equals("length")) continue;

                for (String caso : casos) {
                    List<String[]> data = generateCase(new ArrayList<>(originalData), caso, criterio);
                    String[][] array = data.toArray(new String[0][0]);
                    sortData(array, criterio, algoritmo);
                    String output = String.format("passwords_%s_%sSort_%sCaso.csv", criterio, algoritmo, caso);
                    writeCSV(output, header, array);
                    System.out.println("✅ arquivo gerado: " + output);
                }
            }
        }
    }

    private static List<String[]> readCSV(String filename) throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            List<String[]> all = reader.readAll();
            List<String[]> filtered = new ArrayList<>();
            for (String[] row : all) {
                if (row.length >= 4 && row[2] != null && !row[2].trim().isEmpty()) {
                    filtered.add(row);
                }
            }
            return filtered;
        }
    }

    private static void writeCSV(String filename, String[] header, String[][] data) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filename))) {
            writer.writeNext(header);
            for (String[] row : data) {
                writer.writeNext(row);
            }
        }
    }

    private static List<String[]> generateCase(List<String[]> data, String caso, String criterio) {
        Comparator<String[]> comparator = getComparator(criterio);
        switch (caso) {
            case "melhor" -> data.sort(comparator);
            case "pior" -> data.sort(comparator.reversed());
            case "medio" -> Collections.shuffle(data);
        }
        return data;
    }

    private static void sortData(String[][] array, String criterio, String algoritmo) {
        Comparator<String[]> comp = getComparator(criterio);
        switch (algoritmo) {
            case "insertion" -> insertionSort(array, comp);
            case "selection" -> selectionSort(array, comp);
            case "merge" -> mergeSort(array, comp, 0, array.length - 1);
            case "quick" -> quickSort(array, comp, 0, array.length - 1);
            case "quickMediana" -> quickSortMediana(array, comp, 0, array.length - 1);
            case "heap" -> heapSort(array, comp);
            case "counting" -> countingSort(array); // só funciona com length
        }
    }

    private static Comparator<String[]> getComparator(String criterio) {
        return switch (criterio) {
            case "length" -> Comparator.comparingInt(r -> Integer.parseInt(r[2].trim()));
            case "month" -> Comparator.comparingInt(r -> extractMonth(r[3]));
            case "date" -> Comparator.comparing(r -> parseDate(r[3]));
            default -> throw new IllegalArgumentException("critério inválido");
        };
    }

    private static int extractMonth(String data) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(data).getMonth() + 1;
        } catch (ParseException e) {
            return 0;
        }
    }

    private static Date parseDate(String data) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(data);
        } catch (ParseException e) {
            return new Date(0);
        }
    }

    // ---------- algoritmos com arrays ----------

    private static void insertionSort(String[][] array, Comparator<String[]> comp) {
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

    private static void selectionSort(String[][] array, Comparator<String[]> comp) {
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (comp.compare(array[j], array[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            String[] temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
        }
    }

    private static void mergeSort(String[][] array, Comparator<String[]> comp, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(array, comp, left, mid);
            mergeSort(array, comp, mid + 1, right);
            merge(array, comp, left, mid, right);
        }
    }

    private static void merge(String[][] array, Comparator<String[]> comp, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        String[][] leftArray = new String[n1][];
        String[][] rightArray = new String[n2][];

        for (int i = 0; i < n1; ++i) leftArray[i] = array[left + i];
        for (int j = 0; j < n2; ++j) rightArray[j] = array[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (comp.compare(leftArray[i], rightArray[j]) <= 0) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
            }
        }
        while (i < n1) array[k++] = leftArray[i++];
        while (j < n2) array[k++] = rightArray[j++];
    }

    private static void quickSort(String[][] array, Comparator<String[]> comp, int low, int high) {
        if (low < high) {
            int pi = partition(array, comp, low, high);
            quickSort(array, comp, low, pi - 1);
            quickSort(array, comp, pi + 1, high);
        }
    }

    private static void quickSortMediana(String[][] array, Comparator<String[]> comp, int low, int high) {
        if (low < high) {
            int pi = partitionMediana(array, comp, low, high);
            quickSortMediana(array, comp, low, pi - 1);
            quickSortMediana(array, comp, pi + 1, high);
        }
    }

    private static int partition(String[][] array, Comparator<String[]> comp, int low, int high) {
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

    private static int partitionMediana(String[][] array, Comparator<String[]> comp, int low, int high) {
        int mid = (low + high) / 2;
        String[] a = array[low], b = array[mid], c = array[high];
        String[] pivot;

        if (comp.compare(a, b) < 0) {
            if (comp.compare(b, c) < 0) pivot = b;
            else if (comp.compare(a, c) < 0) pivot = c;
            else pivot = a;
        } else {
            if (comp.compare(a, c) < 0) pivot = a;
            else if (comp.compare(b, c) < 0) pivot = c;
            else pivot = b;
        }

        int pivotIndex = high;
        for (int i = low; i <= high; i++) {
            if (array[i] == pivot) {
                pivotIndex = i;
                break;
            }
        }

        String[] temp = array[pivotIndex];
        array[pivotIndex] = array[high];
        array[high] = temp;

        return partition(array, comp, low, high);
    }

    private static void heapSort(String[][] array, Comparator<String[]> comp) {
        int n = array.length;
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(array, n, i, comp);
        for (int i = n - 1; i >= 0; i--) {
            String[] temp = array[0];
            array[0] = array[i];
            array[i] = temp;
            heapify(array, i, 0, comp);
        }
    }

    private static void heapify(String[][] array, int n, int i, Comparator<String[]> comp) {
        int largest = i;
        int l = 2 * i + 1, r = 2 * i + 2;

        if (l < n && comp.compare(array[l], array[largest]) > 0)
            largest = l;
        if (r < n && comp.compare(array[r], array[largest]) > 0)
            largest = r;

        if (largest != i) {
            String[] swap = array[i];
            array[i] = array[largest];
            array[largest] = swap;
            heapify(array, n, largest, comp);
        }
    }

    private static void countingSort(String[][] array) {
        int max = 0;
        for (String[] row : array) {
            int len = Integer.parseInt(row[2].trim());
            if (len > max) max = len;
        }

        List<String[]>[] count = new List[max + 1];
        for (int i = 0; i <= max; i++) count[i] = new ArrayList<>();

        for (String[] row : array) {
            int len = Integer.parseInt(row[2].trim());
            count[len].add(row);
        }

        int idx = 0;
        for (int i = 0; i <= max; i++) {
            for (String[] row : count[i]) {
                array[idx++] = row;
            }
        }
    }
}
