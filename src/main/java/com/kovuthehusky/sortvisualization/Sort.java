package com.kovuthehusky.sortvisualization;

public abstract class Sort implements Runnable {
    protected Number[] array;
    protected Window window;

    public Sort(Window window) {
        this.window = window;
        array = window.getArray();
    }

    @Override
    public abstract void run();

    protected boolean isSorted() {
        if (array.length <= 1)
            return true;
        for (int i = 1; i < array.length; ++i)
            if (array[i].lt(array[i - 1]))
                return false;
        return true;
    }

    protected boolean stop() {
        return window.isStopping();
    }

    protected void swap(int a, int b) {
        array[a].dirty();
        array[b].dirty();
        Number temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }
}
