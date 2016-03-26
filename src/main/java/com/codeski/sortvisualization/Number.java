package com.codeski.sortvisualization;

public class Number implements Comparable<Number> {
    private final AudioEngine ae;
    private boolean dirty;
    private boolean highlighted;
    private final int value;

    public Number(int value, AudioEngine ae) {
        this.value = value;
        highlighted = false;
        dirty = true;
        this.ae = ae;
    }

    @Override
    public int compareTo(Number that) {
        this.highlighted();
        that.highlighted();
        ae.play(this, that);
        return value - that.value;
    }

    public int compareToInt(int num) {
        Number that = new Number(num, ae);
        return this.compareTo(that);
    }

    public void dirty() {
        dirty = true;
    }

    public boolean equals(int that) {
        return this.compareToInt(that) == 0;
    }

    public boolean equals(Number that) {
        return this.compareTo(that) == 0;
    }

    public int getValue() {
        return value;
    }

    public boolean gt(int that) {
        return this.compareToInt(that) > 0;
    }

    public boolean gt(Number that) {
        return this.compareTo(that) > 0;
    }

    public boolean gte(int that) {
        return this.compareToInt(that) >= 0;
    }

    public boolean gte(Number that) {
        return this.compareTo(that) >= 0;
    }

    public void highlighted() {
        highlighted = true;
    }

    public boolean isDirty() {
        boolean ret = dirty;
        dirty = false;
        return ret;
    }

    public boolean isHighlighted() {
        boolean ret = highlighted;
        highlighted = false;
        return ret;
    }

    public boolean lt(int that) {
        return this.compareToInt(that) < 0;
    }

    public boolean lt(Number that) {
        return this.compareTo(that) < 0;
    }

    public boolean lte(int that) {
        return this.compareToInt(that) <= 0;
    }

    public boolean lte(Number that) {
        return this.compareTo(that) <= 0;
    }

    @Override
    public String toString() {
        return value + "";
    }
}
