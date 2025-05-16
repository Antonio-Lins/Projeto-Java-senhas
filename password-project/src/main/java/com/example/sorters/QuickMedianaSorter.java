package com.example.sorters;

public class QuickMedianaSorter implements Sorter {

    @Override
    public void sort(String[][] data, int columnIndex, boolean descending) {
        quickSortMediana(data, 0, data.length - 1, columnIndex, descending);
    }

    private void quickSortMediana(String[][] data, int low, int high, int columnIndex, boolean descending) {
        if (low < high) {
            int pi = partitionMediana(data, low, high, columnIndex, descending);
            quickSortMediana(data, low, pi - 1, columnIndex, descending);
            quickSortMediana(data, pi + 1, high, columnIndex, descending);
        }
    }

    private int partitionMediana(String[][] data, int low, int high, int columnIndex, boolean descending) {
        int mid = (low + high) / 2;

        // pegar três valores para calcular a mediana: low, mid, high
        String[] a = data[low];
        String[] b = data[mid];
        String[] c = data[high];

        String pivotValue = mediana(a[columnIndex], b[columnIndex], c[columnIndex]);
        int pivotIndex = findIndex(data, low, high, pivotValue, columnIndex);
        swap(data, pivotIndex, high);

        String[] pivot = data[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            boolean condition = data[j][columnIndex].compareTo(pivot[columnIndex]) < 0;
            if (descending) condition = !condition;
            if (condition) {
                i++;
                swap(data, i, j);
            }
        }

        swap(data, i + 1, high);
        return i + 1;
    }

    private String mediana(String a, String b, String c) {
        if ((a.compareTo(b) >= 0 && a.compareTo(c) <= 0) || (a.compareTo(c) >= 0 && a.compareTo(b) <= 0)) return a;
        if ((b.compareTo(a) >= 0 && b.compareTo(c) <= 0) || (b.compareTo(c) >= 0 && b.compareTo(a) <= 0)) return b;
        return c;
    }

    private int findIndex(String[][] data, int low, int high, String pivot, int columnIndex) {
        for (int i = low; i <= high; i++) {
            if (data[i][columnIndex].equals(pivot)) return i;
        }
        return high; // fallback
    }

    private void swap(String[][] data, int i, int j) {
        String[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }
}
