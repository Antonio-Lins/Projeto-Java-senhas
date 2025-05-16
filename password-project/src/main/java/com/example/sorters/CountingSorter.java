package com.example.sorters;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class CountingSorter implements Sorter {
    @Override
    public void sort(List<String[]> list, Comparator<String[]> comp) {
        int max = list.stream().mapToInt(e -> Integer.parseInt(e[2])).max().orElse(0);
        List<List<String[]>> buckets = new ArrayList<>();
        for (int i = 0; i <= max; i++) buckets.add(new ArrayList<>());
        for (String[] entry : list) {
            int len = Integer.parseInt(entry[2]);
            buckets.get(len).add(entry);
        }
        list.clear();
        for (List<String[]> bucket : buckets) {
            list.addAll(bucket);
        }
    }
}
