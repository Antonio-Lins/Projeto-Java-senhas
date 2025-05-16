package com.example.sorters;

public class MergeSorter implements Sorter{
    public void sort(String[][] data, int column, boolean crescente) {
        mergeSort(data, 0, data.length - 1, column, crescente);
    }

    private static void mergeSort(String[][] data, int left, int right, int column, boolean crescente) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(data, left, middle, column, crescente);
            mergeSort(data, middle + 1, right, column, crescente);
            merge(data, left, middle, right, column, crescente);
        }
    }

    private static void merge(String[][] data, int left, int middle, int right, int column, boolean crescente) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        String[][] L = new String[n1][data[0].length];
        String[][] R = new String[n2][data[0].length];

        for (int i = 0; i < n1; i++)
            L[i] = data[left + i];
        for (int j = 0; j < n2; j++)
            R[j] = data[middle + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (comparar(L[i][column], R[j][column], crescente)) {
                data[k] = L[i];
                i++;
            } else {
                data[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            data[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            data[k] = R[j];
            j++;
            k++;
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
