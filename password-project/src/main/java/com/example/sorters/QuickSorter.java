package com.example.sorters;

import java.util.Comparator;
import java.util.List;
import java.util.Collections;

public class QuickSorter implements Sorter {
    @Override
    public void sort(List<String[]> list, Comparator<String[]> comp) {
        quickSort(list, comp, 0, list.size() - 1);
    }

    private void quickSort(List<String[]> list, Comparator<String[]> comp, int low, int high) {
        if (low < high) {
            int pi = partition(list, comp, low, high);
            quickSort(list, comp, low, pi - 1);
            quickSort(list, comp, pi + 1, high);
        }
    }

    int partition(List<String[]> list, Comparator<String[]> comp, int low, int high) {
        String[] pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comp.compare(list.get(j), pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
}
