package com.kovuthehusky.sortvisualization;

public class RecursiveMergeSort extends Sort {
    // TODO: Rewrite this sort to be cleaner and easier to understand.
    public RecursiveMergeSort(Window window) {
        super(window);
    }

    @Override
    public void run() {
        this.mergeSortRecursive(array, 0, array.length);
    }

    private void merge(int start, int middle, int end) {
        Number[] merge = new Number[end - start];
        int l = 0, r = 0, pos = 0;
        while (l < middle - start && r < end - middle) {
            if (this.stop())
                return;
            if (array[start + l].lt(array[middle + r]))
                merge[pos++] = array[start + l++];
            else
                merge[pos++] = array[middle + r++];
        }
        while (r < end - middle)
            merge[pos++] = array[middle + r++];
        while (l < middle - start)
            merge[pos++] = array[start + l++];
        for (Number element : merge) {
            array[start++] = element;
            array[start - 1].dirty();
        }
    }

    private void mergeSortRecursive(Number[] array, int start, int end) {
        if (end - start <= 1)
            return;
        int middle = start + (end - start) / 2;
        this.mergeSortRecursive(array, start, middle);
        this.mergeSortRecursive(array, middle, end);
        this.merge(start, middle, end);
    }
}
