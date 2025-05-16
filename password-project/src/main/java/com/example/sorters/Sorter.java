package com.example.sorters;

import java.util.Comparator;
import java.util.List;

public interface Sorter {
    void sort(List<String[]> list, Comparator<String[]> comparator);
}
