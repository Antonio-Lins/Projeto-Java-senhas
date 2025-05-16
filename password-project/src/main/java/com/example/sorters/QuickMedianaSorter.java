package com.example.sorters;

import java.util.Comparator;
import java.util.List;
import java.util.Collections;

public class QuickMedianaSorter implements Sorter {
    @Override
    public void sort(List<String[]> list, Comparator<String[]> comp) {
        quickSortMediana(list, comp, 0, list.size() - 1);
    }

    private void quickSortMediana(List<String[]> list, Comparator<String[]> comp, int low, int high) {
        if (low < high) {
            int pi = partitionMediana(list, comp, low, high);
            quickSortMediana(list, comp, low, pi - 1);
            quickSortMediana(list, comp, pi + 1, high);
        }
    }

    private int partitionMediana(List<String[]> list, Comparator<String[]> comp, int low, int high) {
        int mid = (low + high) / 2;
        String[] a = list.get(low);
        String[] b = list.get(mid);
        String[] c = list.get(high);
        String[] pivot = medianOfThree(a, b, c, comp);
        int pivotIndex = list.indexOf(pivot);
        Collections.swap(list, pivotIndex, high);
        return new QuickSorter().partition(list, comp, low, high);
    }

    private String[] medianOfThree(String[] a, String[] b, String[] c, Comparator<String[]> comp) {
        if (comp.compare(a, b) < 0) {
            if (comp.compare(b, c) < 0) return b;
            else if (comp.compare(a, c) < 0) return c;
            else return a;
        } else {
            if (comp.compare(a, c) < 0) return a;
            else if (comp.compare(b, c) < 0) return c;
            else return b;
        }
    }
}
