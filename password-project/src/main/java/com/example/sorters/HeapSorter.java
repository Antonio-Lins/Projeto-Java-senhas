package com.example.sorters;

public class HeapSorter implements Sorter {

    @Override
    public void sort(String[][] data, int columnIndex, boolean descending) {
        int n = data.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(data, n, i, columnIndex, descending);
        }

        for (int i = n - 1; i >= 0; i--) {
            swap(data, 0, i);
            heapify(data, i, 0, columnIndex, descending);
        }
    }

    private void heapify(String[][] data, int n, int i, int columnIndex, boolean descending) {
        int largestOrSmallest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && compare(data[left][columnIndex], data[largestOrSmallest][columnIndex], descending) > 0) {
            largestOrSmallest = left;
        }

        if (right < n && compare(data[right][columnIndex], data[largestOrSmallest][columnIndex], descending) > 0) {
            largestOrSmallest = right;
        }

        if (largestOrSmallest != i) {
            swap(data, i, largestOrSmallest);
            heapify(data, n, largestOrSmallest, columnIndex, descending);
        }
    }

    private int compare(String a, String b, boolean descending) {
        int result;
        try {
            result = Integer.compare(Integer.parseInt(a), Integer.parseInt(b));
        } catch (NumberFormatException e) {
            result = a.compareTo(b);
        }
        return descending ? -result : result;
    }

    private void swap(String[][] data, int i, int j) {
        String[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }
}
