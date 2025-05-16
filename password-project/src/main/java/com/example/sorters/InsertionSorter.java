package com.example.sorters;

import java.util.Comparator;
import java.util.List;

public class InsertionSorter implements Sorter {
    @Override
    public void sort(List<String[]> list, Comparator<String[]> comp) {
        for (int i = 1; i < list.size(); i++) {
            String[] key = list.get(i);
            int j = i - 1;
            while (j >= 0 && comp.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }
}
