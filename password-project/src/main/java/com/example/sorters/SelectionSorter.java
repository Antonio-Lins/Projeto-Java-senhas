package com.example.sorters;

public class SelectionSorter implements Sorter{
    public void sort(String[][] data, int column, boolean crescente) {
        int n = data.length;

        for (int i = 0; i < n - 1; i++) {
            int indiceExtremo = i;

            for (int j = i + 1; j < n; j++) {
                if (comparar(data[j][column], data[indiceExtremo][column], crescente)) {
                    indiceExtremo = j;
                }
            }

            // troca
            String[] temp = data[i];
            data[i] = data[indiceExtremo];
            data[indiceExtremo] = temp;
        }
    }

    private static boolean comparar(String a, String b, boolean crescente) {
        try {
            double numA = Double.parseDouble(a);
            double numB = Double.parseDouble(b);
            return crescente ? numA < numB : numA > numB;
        } catch (NumberFormatException e) {
            return crescente ? a.compareTo(b) < 0 : a.compareTo(b) > 0;
        }
    }
}
