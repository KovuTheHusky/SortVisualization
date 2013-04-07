package sortvisualization;

import java.util.ArrayList;
import processing.core.*;

@SuppressWarnings("serial")
public class SortVisualization extends PApplet {

	private static final int ARRAY_LENGTH = 200;
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 1000;
	private static final int MIN = 100;
	private static final int MAX = 900;
	private static final int BAR_WIDTH = WIDTH / ARRAY_LENGTH - 1;

	private Number[] array = new Number[ARRAY_LENGTH];
	private boolean running = false;

	private ArrayList<Integer> highlighted = new ArrayList<Integer>();
	private ArrayList<Integer> toHighlight = new ArrayList<Integer>();

	private double fps = 0;
	private long lastFpsOut = 0;

	@Override
	public void setup() {
		size(WIDTH, HEIGHT);
		background(255);
		stroke(255);
		fill(0);
		textSize(16);
		textAlign(TOP, LEFT);
		newArray();
		AudioEngine.setLength(10);
	}
	
	@Override
	public void draw() {
		++fps;
		long time = System.currentTimeMillis();
		if (time - 1000 > lastFpsOut) {
			fill(255);
			rect(5, 0, WIDTH, 59);
			fill(0);
			text("FPS: " + (int)fps, 5, 5, WIDTH, 23);
			text("Sorting algorithms: (s)election  (i)nsertion  (m)erge  recursive m(e)rge  (b)ogo", 5, 23, WIDTH, 41);
			text("Miscellaneous: (n)ew  (f)lip  (c)heck  (1-9) pause length", 5, 41, WIDTH, 59);
			fps = 0;
			lastFpsOut = time;
		}

		if (!running) {
			if (!highlighted.isEmpty()) {
				for (int i : highlighted) {
					fill(0);
					rect(i * BAR_WIDTH + i, HEIGHT - array[i].getValue(), BAR_WIDTH, array[i].getValue());
				}
				highlighted.clear();
			}
		}

		toHighlight.clear();
		for (int i = 0; i < array.length; ++i) {
			if (array[i].isHighlighted()) {
				toHighlight.add(i);
			}

			if (array[i].isDirty()) {
				fill(255);
				rect(i * BAR_WIDTH + i, HEIGHT - MAX, BAR_WIDTH, MAX);
				fill(0);
				rect(i * BAR_WIDTH + i, HEIGHT - array[i].getValue(), BAR_WIDTH, array[i].getValue());
			}
		}

		if (!toHighlight.isEmpty()) {
			if (!highlighted.isEmpty()) {
				for (int i = highlighted.size() - 1; i >= 0; --i) {
					if (!toHighlight.contains(highlighted.get(i))) {
						fill(0);
						rect(highlighted.get(i) * BAR_WIDTH + highlighted.get(i), HEIGHT - array[highlighted.get(i)].getValue(), BAR_WIDTH, array[highlighted.get(i)].getValue());
						highlighted.remove(i);
					}
				}
			}

			if (!toHighlight.equals(highlighted)) {
				for (int i : toHighlight) {
					if (!highlighted.contains(i)) {
						fill(0, 0, 255);
						rect(i * BAR_WIDTH + i, HEIGHT - array[i].getValue(), BAR_WIDTH, array[i].getValue());
						highlighted.add(i);
					}
				}
			}

		}
	}

	@Override
	public void keyReleased() {
		switch (key) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				AudioEngine.setLength(Integer.parseInt(key + "") * 10);
		}
		
		if (running)
			return;
		running = true;
		switch (key) {
			case 's':
				thread("selectionSort");
				break;
			case 'i':
				thread("insertionSort");
				break;
			case 'e':
				thread("mergeSortRecursive");
				break;
			case 'b':
				thread("bogoSort");
				break;
			case 'm':
				thread("mergeSort");
				break;
			case 'c':
				thread("isSorted");
				break;
			case 'n':
				newArray();
				break;
			case 'f':
				flipArray();
				break;
			default:
				running = false;
		}
	}
	
	private void newArray() {
		for (int i = 0; i < array.length; ++i)
			array[i] = new Number((int)random(MIN, MAX));
		running = false;
	}
	
	private void flipArray() {
		for (int i = 0; i < array.length / 2; ++i)
			swap(i, array.length - i - 1);
		running = false;
	}
	
	public boolean isSorted() {
		if (array.length <= 1)
			return !(running = false);
		for (int i = 1; i < array.length; ++i)
			if (array[i].lt(array[i - 1]))
				return (running = false);
		return !(running = false);
	}
	
	private boolean isSorted(boolean keepRunning) {
		if (!keepRunning)
			return isSorted();
		if (array.length <= 1)
			return true;
		for (int i = 1; i < array.length; ++i)
			if (array[i].lt(array[i - 1]))
				return false;
		return true;
	}

	private void swap(int a, int b) {
		array[a].dirty();
		array[b].dirty();
		Number temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}

	public void bogoSort() {
		while (!isSorted(true))
			for (int i = 0; i < array.length; ++i)
				swap(i, (int)random(0, array.length));
		isSorted();
	}

	public void selectionSort() {
		if (array.length <= 1)
			return;
		for (int i = 0; i < array.length; ++i) {
			int min = i;
			for (int j = i + 1; j < array.length; ++j) {
				if (array[j].lt(array[min]))
					min = j;
			}
			if (min != i)
				swap(i, min);
		}
		isSorted();
	}

	public void insertionSort() {
		if (array.length <= 1)
			return;
		for (int i = 0; i < array.length; ++i) {
			Number valueToInsert = array[i];
			int holePos = i;
			while (holePos > 0 && array[holePos].lt(array[holePos - 1])) {
				swap(holePos, holePos - 1);
				--holePos;
			}
			array[holePos] = valueToInsert;
			array[holePos].dirty();
		}
		isSorted();
	}

	public void mergeSort() {
		Number[] array = this.array;
		if (array.length < 2)
			return;
		int step = 1;
		int startL, startR;

		while (step < array.length) {
			startL = 0;
			startR = step;
			while (startR + step <= array.length) {
				mergeArrays(array, startL, startL + step, startR, startR + step);
				startL = startR + step;
				startR = startL + step;
			}
			if (startR < array.length)
				mergeArrays(array, startL, startL + step, startR, array.length);
			step *= 2;
		}
		isSorted();
	}

	private void mergeArrays(Number[] array, int startL, int stopL, int startR, int stopR) {
		Number[] right = new Number[stopR - startR + 1];
		Number[] left = new Number[stopL - startL + 1];

		for (int i = 0, k = startR; i < (right.length - 1); ++i, ++k)
			right[i] = array[k];
		for (int i = 0, k = startL; i < (left.length - 1); ++i, ++k)
			left[i] = array[k];

		right[right.length - 1] = new Number(Integer.MAX_VALUE);
		left[left.length - 1] = new Number(Integer.MAX_VALUE);

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

	public void mergeSortRecursive() {
		mergeSortRecursive(array, 0, array.length);
		isSorted();
	}

	private void mergeSortRecursive(Number[] array, int start, int end) {
		if (end - start <= 1)
			return;
		int middle = start + (end - start) / 2;
		mergeSortRecursive(array, start, middle);
		mergeSortRecursive(array, middle, end);
		merge(start, middle, end);
	}

	private void merge(int start, int middle, int end) {
		Number[] merge = new Number[end - start];
		int l = 0, r = 0, pos = 0;
		while (l < middle - start && r < end - middle) {
			if (array[start + l].lt(array[middle + r]))
				merge[pos++] = array[start + l++];
			else
				merge[pos++] = array[middle + r++];
		}
		while (r < end - middle)
			merge[pos++] = array[middle + r++];
		while (l < middle - start)
			merge[pos++] = array[start + l++];
		for (int i = 0; i < merge.length; ++i) {
			array[start++] = merge[i];
			array[start - 1].dirty();
		}
	}

}
