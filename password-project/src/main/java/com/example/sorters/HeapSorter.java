package com.example.sorters;

import java.util.Comparator;
import java.util.List;
import java.util.Collections;

public class HeapSorter implements Sorter {
    @Override
    public void sort(List<String[]> list, Comparator<String[]> comp) {
        int n = list.size();
        for (int i = n / 2 - 1; i >= 0; i--) heapify(list, n, i, comp);
        for (int i = n - 1; i > 0; i--) {
            Collections.swap(list, 0, i);
            heapify(list, i, 0, comp);
        }
    }

    private void heapify(List<String[]> list, int n, int i, Comparator<String[]> comp) {
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
