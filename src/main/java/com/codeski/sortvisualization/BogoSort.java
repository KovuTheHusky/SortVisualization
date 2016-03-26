package com.codeski.sortvisualization;

import java.util.Random;

public class BogoSort extends Sort {
    public BogoSort(Window window) {
        super(window);
    }

    @Override
    public void run() {
        Random r = new Random();
        while (!this.isSorted())
            for (int i = 0; i < array.length; ++i) {
                if (this.stop())
                    return;
                this.swap(i, r.nextInt(array.length));
            }
    }
}
