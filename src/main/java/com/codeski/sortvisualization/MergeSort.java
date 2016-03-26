package com.codeski.sortvisualization;

public class MergeSort extends Sort {
    // TODO: Rewrite this sort to be cleaner and easier to understand.
    public MergeSort(Window window) {
        super(window);
    }

    @Override
    public void run() {
        if (array.length < 2)
            return;
        int step = 1;
        int startL, startR;
        while (step < array.length) {
            startL = 0;
            startR = step;
            while (startR + step <= array.length) {
                if (this.stop())
                    return;
                this.mergeArrays(array, startL, startL + step, startR, startR + step);
                startL = startR + step;
                startR = startL + step;
            }
            if (startR < array.length)
                this.mergeArrays(array, startL, startL + step, startR, array.length);
            step *= 2;
        }
    }

    private void mergeArrays(Number[] array, int startL, int stopL, int startR, int stopR) {
        Number[] right = new Number[stopR - startR + 1];
        Number[] left = new Number[stopL - startL + 1];
        for (int i = 0, k = startR; i < right.length - 1; ++i, ++k)
            right[i] = array[k];
        for (int i = 0, k = startL; i < left.length - 1; ++i, ++k)
            left[i] = array[k];
        right[right.length - 1] = new Number(Integer.MAX_VALUE, window.getAudioEngine());
        left[left.length - 1] = new Number(Integer.MAX_VALUE, window.getAudioEngine());
        for (int k = startL, m = 0, n = 0; k < stopR; ++k) {
            if (left[m].lte(right[n])) {
                array[k] = left[m];
                m++;
            } else {
                array[k] = right[n];
                n++;
            }
            array[k].dirty();
        }
    }
}
