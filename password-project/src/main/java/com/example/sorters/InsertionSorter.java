package com.example.sorters;

public class InsertionSorter implements Sorter{
    public void sort(String[][] data, int column, boolean crescente) {
        for (int i = 1; i < data.length; i++) {
            String[] key = data[i];
            int j = i - 1;

            while (j >= 0 && !comparar(data[j][column], key[column], crescente)) {
                data[j + 1] = data[j];
                j = j - 1;
            }
            data[j + 1] = key;
        }
    }

    private static boolean comparar(String a, String b, boolean crescente) {
        try {
            double numA = Double.parseDouble(a);
            double numB = Double.parseDouble(b);
            return crescente ? numA <= numB : numA >= numB;
        } catch (NumberFormatException e) {
            return crescente ? a.compareTo(b) <= 0 : a.compareTo(b) >= 0;
        }
    }
}
