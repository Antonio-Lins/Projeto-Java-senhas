package com.example.sorters;

public class CountingSorter implements Sorter {

    @Override
    public void sort(String[][] data, int columnIndex, boolean descending) {
        if (data.length == 0) return;
        int n = data.length;

        // cria um array auxiliar para armazenar os valores convertidos em inteiros
        int[] keys = new int[n];
        for (int i = 0; i < n; i++) {
            try {
                keys[i] = Integer.parseInt(data[i][columnIndex].replaceAll("[^0-9]", ""));
            } catch (NumberFormatException e) {
                keys[i] = 0;
            }
        }

        // encontrar o maior valor para o range do counting sort
        int max = keys[0];
        for (int i = 1; i < n; i++) {
            if (keys[i] > max) {
                max = keys[i];
            }
        }

        // counting sort só funciona para números não-negativos
        int[] count = new int[max + 1];
        for (int i = 0; i < n; i++) {
            count[keys[i]]++;
        }

        if (!descending) {
            for (int i = 1; i <= max; i++) {
                count[i] += count[i - 1];
            }
        } else {
            for (int i = max - 1; i >= 0; i--) {
                count[i] += count[i + 1];
            }
        }

        // construir array ordenado
        String[][] sorted = new String[n][];
        for (int i = n - 1; i >= 0; i--) {
            int key = keys[i];
            int pos = count[key] - 1;
            sorted[pos] = data[i];
            count[key]--;
        }

        // copiar de volta para o array original
        for (int i = 0; i < n; i++) {
            data[i] = sorted[i];
        }
    }
}
