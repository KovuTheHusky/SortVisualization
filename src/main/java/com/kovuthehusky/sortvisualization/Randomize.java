package com.kovuthehusky.sortvisualization;

import java.util.Random;

public class Randomize implements Runnable {
    private final Number[] array;
    private final Window window;

    public Randomize(Window window) {
        this.window = window;
        array = window.getArray();
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < array.length; ++i)
            array[i] = new Number(random.nextInt(window.getMaximum() - 1) + 1, window.getAudioEngine());
    }
}
