package com.example.sorters;

public class QuickSorter implements Sorter {

    @Override
    public void sort(String[][] data, int columnIndex, boolean descending) {
        quickSort(data, 0, data.length - 1, columnIndex, descending);
    }

    private void quickSort(String[][] data, int low, int high, int columnIndex, boolean descending) {
        if (low < high) {
            int pi = partition(data, low, high, columnIndex, descending);
            quickSort(data, low, pi - 1, columnIndex, descending);
            quickSort(data, pi + 1, high, columnIndex, descending);
        }
    }

    private int partition(String[][] data, int low, int high, int columnIndex, boolean descending) {
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

    private void swap(String[][] data, int i, int j) {
        String[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }
}
