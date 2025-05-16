package com.example.sorters;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class MergeSorter implements Sorter {
    @Override
    public void sort(List<String[]> list, Comparator<String[]> comp) {
        if (list.size() <= 1) return;
        int mid = list.size() / 2;
        List<String[]> left = new ArrayList<>(list.subList(0, mid));
        List<String[]> right = new ArrayList<>(list.subList(mid, list.size()));
        sort(left, comp);
        sort(right, comp);
        merge(list, left, right, comp);
    }

    private void merge(List<String[]> list, List<String[]> left, List<String[]> right, Comparator<String[]> comp) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (comp.compare(left.get(i), right.get(j)) <= 0) {
                list.set(k++, left.get(i++));
            } else {
                list.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) list.set(k++, left.get(i++));
        while (j < right.size()) list.set(k++, right.get(j++));
    }
}
