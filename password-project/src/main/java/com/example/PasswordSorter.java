package com.example;

import com.example.sorters.*;

import java.io.*;
import java.util.*;

public class PasswordSorter {

    private static final String INPUT_FILE = "passwords_formated_data.csv";
    private static final String OUTPUT_FOLDER = "outputs";

    private static final String[] ALGORITHMS = {
        "SelectionSort", "InsertionSort", "MergeSort", "QuickSort", "QuickSortMediana",
        "CountingSort", "HeapSort"
    };

    private static final Sorter[] SORTERS = {
        new SelectionSorter(), new InsertionSorter(), new MergeSorter(),
        new QuickSorter(), new QuickMedianaSorter(), new CountingSorter(), new HeapSorter()
    };

    private static final String[] CRITERIA = {"length", "data_month", "data"};
    private static final int LENGTH_INDEX = 1;
    private static final int DATE_INDEX = 2;

    public static void main(String[] args) throws IOException {
        String[][] originalData = readCsv(INPUT_FILE);
        new File(OUTPUT_FOLDER).mkdir();

        for (int i = 0; i < ALGORITHMS.length; i++) {
            Sorter sorter = SORTERS[i];
            String algorithm = ALGORITHMS[i];

            for (String criterion : CRITERIA) {
                for (String caso : new String[]{"melhorCaso", "medioCaso", "piorCaso"}) {
                    String[][] dataCopy = copyData(originalData);

                    prepareCase(dataCopy, criterion, caso);
                    int column = getColumnIndex(criterion);
                    boolean descending = criterion.equals("length");

                    sorter.sort(dataCopy, column, descending);

                    String fileName = String.format("%s/passwords_%s_%s_%s.csv",
                            OUTPUT_FOLDER, criterion, algorithm, caso);
                    writeCsv(dataCopy, fileName);
                }
            }
        }
        System.out.println("✅ todos os 54 arquivos foram gerados com sucesso!");
    }

    private static void prepareCase(String[][] data, String criterion, String caso) {
        int column = getColumnIndex(criterion);
        switch (caso) {
            case "melhorCaso":
                Arrays.sort(data, Comparator.comparing(a -> a[column]));
                break;
            case "piorCaso":
                Arrays.sort(data, Comparator.comparing(a -> a[column], Comparator.reverseOrder()));
                break;
            case "medioCaso":
                Collections.shuffle(Arrays.asList(data));
                break;
        }
    }

    private static int getColumnIndex(String criterion) {
        return switch (criterion) {
            case "length" -> LENGTH_INDEX;
            case "data_month" -> 3; // assumindo coluna 3 = mês (extraída antes)
            case "data" -> DATE_INDEX;
            default -> -1;
        };
    }

    private static String[][] readCsv(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    String date = parts[2];
                    String month = date.length() >= 7 ? date.substring(5, 7) : "00";
                    String[] extended = Arrays.copyOf(parts, parts.length + 1);
                    extended[3] = month;
                    rows.add(extended);
                }
            }
        }
        return rows.toArray(new String[0][0]);
    }

    private static void writeCsv(String[][] data, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                writer.write(String.join(";", row[0], row[1], row[2]));
                writer.newLine();
            }
        }
    }

    private static String[][] copyData(String[][] data) {
        String[][] copy = new String[data.length][];
        for (int i = 0; i < data.length; i++) {
            copy[i] = Arrays.copyOf(data[i], data[i].length);
        }
        return copy;
    }
}
