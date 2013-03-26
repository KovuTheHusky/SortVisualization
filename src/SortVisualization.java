import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import processing.core.*;

@SuppressWarnings("serial")
public class SortVisualization extends PApplet {
	
	private boolean playBoth = true;
		
	private final int ARRAY_LENGTH = 50;
	private final int WIDTH = 1000;
	private final int HEIGHT = 1000;
	private final int MIN = 100;
	private final int MAX = 900;
	private final int BAR_WIDTH = WIDTH / ARRAY_LENGTH - 1;
	private final int SOUND_LENGTH = 10;
	
	private Number[] array = new Number[ARRAY_LENGTH];
	private boolean running = false;
	private int cur1 = -1, cur2 = -1, cur4 = -1, cur5 = -1;
	
	private ArrayList<Integer> highlighted = new ArrayList<Integer>();
	private ArrayList<Integer> toHighlight = new ArrayList<Integer>();
	
	Semaphore semaphore = new Semaphore(1);
	AudioEngine ae = new AudioEngine();
	
	long startTime;
	long endTime = 0;
	
	long secondCount = 0;
	double fps = 0;
	long lastFpsOut = 0;
	
	private boolean firstFrame = true;

	public void setup() {
		size(WIDTH, HEIGHT);
		stroke(255);
		fill(0);
		textSize(16);
		textAlign(TOP, LEFT);
		for (int i = 0; i < array.length; ++i)
			array[i] = new Number(i, (int)random(MIN, MAX), semaphore);
	}
	
	public void randomize() {
		for (int i = 0; i < array.length; ++i)
			array[i] = new Number(i, (int)random(MIN, MAX), semaphore);
		running = false;
	}
	
	public byte[] generateSineWavefreq(int frequencyOfSignal, int freq2, int seconds) {
		double subs = (double)seconds / 1000;
        byte[] sin = new byte[(int)(subs * sampleRate)];
        double samplingInterval;
        if (frequencyOfSignal > freq2)
        	samplingInterval = (double) (sampleRate / frequencyOfSignal);
        else
        	samplingInterval = (double) (sampleRate / freq2);
        for (int i = 0; i < sin.length; i++) {
            double angle = (2.0 * Math.PI * i) / samplingInterval;
            sin[i] = (byte) (Math.sin(angle) * 127);
        }
        return sin;
    }

	public void draw() {
		
		if (firstFrame) {
			background(255);
			text("time: ", 5, 0, WIDTH, 18);
			text("fps: ", 5, 18, WIDTH, 36);
			text("sorts: (s)election (i)nsertion (m)erge (b)ogo (w)rong", 5, 36, WIDTH, 54);
			text("misc: (n)ew (f)lip (c)heck", 5, 54, WIDTH, 72);
			firstFrame = !firstFrame;
		}
		
		++fps;
		long currentTime = System.currentTimeMillis();
		
		if (running) {
			fill(255);
			rect(5, 0, WIDTH, 18);
			fill(0);
			endTime = currentTime - startTime;
			text("time: " + ((double)endTime / 1000), 5, 0, WIDTH, 18);
		}
		
		if (currentTime - 1000 > lastFpsOut) {
			fill(255);
			rect(5, 18, WIDTH, 18);
			fill(0);
			text("fps: " + (int)fps, 5, 18, WIDTH, 36);
			fps = 0;
			lastFpsOut = currentTime;
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
					} else {
					}
				}
				//highlighted.clear();
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
			
			semaphore.drainPermits();
			semaphore.release();
		}
		
		if (highlighted.size() > 2) {
			println("ERROR: We should never have more than two bars highlighted.");
			println(highlighted);
			//noLoop();
			//return;
		}
		
		//
		
	}
	
	public void mousePressed() {
		return;
	}
			
	public void keyReleased() {
		if (running)
			return;
		running = true;
		switch(key) {
			case 's':
				thread("selectionSort");
				break;
			case 'i':
				thread("insertionSort");
				break;
			case 'm':
				thread("rMergeSort");
				break;
			case 'b':
				thread("bogoSort");
				break;
			case 'w':
				thread("mergeSort");
				break;
			case 'q':
				thread("quickSort");
				break;
			case 'c':
				thread("isSorted");
				break;
			case 'n':
				randomize();
				break;
			case 'f':
				flip();
				break;
			default:
				running = false;
		}
	}
	
	private void swap(int a, int b) {
		array[a].dirty();
		array[b].dirty();
		Number temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
	
	public void flip() {
		for (int i = 0; i < array.length / 2; ++i)
			swap(i, array.length - i - 1);
		running = false;
	}
	
	public boolean isSorted() {
		if (array.length <= 1) {
			running = false;
			return true;
		}
		for (int i = 1; i < array.length; ++i) {
			play(array[i], array[i - 1], SOUND_LENGTH);
			if (array[i].lt(array[i - 1]))
				play(new Number(-1, 1, semaphore), new Number(-1, MIN, semaphore), SOUND_LENGTH * 10);
		}
		running = false;
		return true;
	}
	
	public boolean isSortedNoPause() {
		for (int i = 1; i < array.length; ++i) {
			cur4 = i;
			cur5 = i - 1;
			if (cur4 > 0) {
				play(array[cur4], array[cur5], SOUND_LENGTH);
				if (array[cur4].lt(array[cur5]))
					play(new Number(-1, 1, semaphore), new Number(-1, MIN, semaphore), SOUND_LENGTH * 10);
			}
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (array[i].lt(array[i - 1])) {
				cur4 = cur5 = -1;
				return false;
			}
		}
		return true;
	}
	
	private void play(Number f, Number f2, int seconds) {
		int freq = f.getValue();
		int freq2 = f2.getValue();
		//int length = sampleRate * array.length / 1000;
		if (playBoth) {
			byte[] array = this.generateSineWavefreq(0, freq2, seconds);
			byte[] array2 = this.generateSineWavefreq(freq, 0, seconds);
	        line.write(array, 0, array.length);
	        line.write(array2, 0, array2.length);
		} else {
			byte[] array = this.generateSineWavefreq(freq, freq2, seconds);
			line.write(array, 0, array.length);
		}
        line.drain();
	}
	
	public void bogoSort() {
		startTime = System.currentTimeMillis();
		while (!isSortedNoPause()) {
			for (int i = 0; i < array.length; ++i) {
				swap(i, (int)random(0, array.length));
				try {
					semaphore.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		isSorted();
	}
	
	public void selectionSort() {
		startTime = System.currentTimeMillis();
		if (array.length <= 1)
			return;
		for (int i = 0; i < array.length; ++i) {
			int min = i;
			for (int j = i + 1; j < array.length; ++j) {
				if (j > 0)
					play(array[min], array[j], SOUND_LENGTH);
				if (array[j].lt(array[min]))
					min = j;
			}
			if (min != i)
				swap(i, min);
		}
		isSorted();
	}
	
	public void insertionSort() {
		startTime = System.currentTimeMillis();
		if (array.length <= 1)
			return;
		for (int i = 0; i < array.length; ++i) {
			Number valueToInsert = array[i];
			int holePos = i;
			while (holePos > 0 && valueToInsert.lt(array[holePos - 1])) {
				if (holePos - 1 > 0)
					play(valueToInsert, array[holePos - 1], SOUND_LENGTH);
				swap(holePos, holePos - 1);
				--holePos;
			}
			array[holePos] = valueToInsert;
			array[holePos].dirty();
		}
		isSorted();
	}
	
	public void mergeSort() {
		startTime = System.currentTimeMillis();
	    for (int i = 1; i <= array.length / 2 + 1; i *= 2) {
	        for (int j = i; j < array.length; j += 2 * i) {
	            merge(j - i, j, Math.min(j + i, array.length));
	        }
	    }
	    
	    cur1 = cur2 = -1;
	    isSorted();
	}
	
	public void rMergeSort()
	 {
		startTime = System.currentTimeMillis();
	    rMergeSort(array, 0, array.length);
	    cur1 = cur2 = -1;
	    isSorted();
	 }
	 
	 private void rMergeSort(Number[] array, int start, int end)
	 {
	    if (end - start <= 1) return;
	    int middle = start + (end - start) / 2;
		rMergeSort(array, start, middle);
		rMergeSort(array, middle, end);
		merge(start, middle, end);
	 }
	
	public void merge(int start, int middle, int end) {
		Number[] merge = new Number[end - start];
	    int l = 0, r = 0, i = 0;
	    while (l < middle - start && r < end - middle) {
	    	cur1 = start + l;
			cur2 = middle + r;
			if (cur1 > -1)
				play(array[cur2], array[cur1], SOUND_LENGTH);
//			try {
//				semaphore.acquire();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
	    	if (array[start + l].lt(array[middle + r]))
	    		merge[i++] = array[start + l++];
	    	else
	    		merge[i++] = array[middle + r++];
	    }
	 
	    while (r < end - middle) merge[i++] = array[middle + r++];
	 
	    while (l < middle - start) merge[i++] = array[start + l++];
	 
	    for (int i1 = 0; i1 < merge.length; ++i1) {
	    	array[start++] = merge[i1];
	    	array[start - 1].dirty();
	    }
	}
	
	public void quickSort() {
		Number[] sorted = quickSort(array);
		array = sorted;
	}
	
	public Number[] quickSort(Number[] arr) {
		if (arr.length < 2)
			return arr;
		int pivot = (int)random(0, arr.length - 1);
		ArrayList<Number> less = new ArrayList<Number>();
		ArrayList<Number> more = new ArrayList<Number>();
		for (int i = 0; i < arr.length; ++i) {
			
			if (i == pivot)
				continue;
			
			cur1 = i;
			cur2 = pivot;
			if (cur1 > -1)
				play(array[cur2], array[cur1], SOUND_LENGTH);
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (arr[i].lte(arr[pivot]))
				less.add(arr[i]);
			else
				more.add(arr[i]);
		}
		Number[] lessA = new Number[less.size()];
		for (int i = 0; i < less.size(); ++i) {
			lessA[i] = less.get(i);
		}
		Number[] moreA = new Number[more.size()];
		for (int i = 0; i < more.size(); ++i) {
			moreA[i] = more.get(i);
		}
		
		Number[] toInsert = concatenate(lessA, arr[pivot], moreA);
		int pos = 0;
		for (int i = 0; i < toInsert.length; i++) {
			array[pos++] = toInsert[i];
		}
		
		return concatenate(quickSort(lessA), arr[pivot], quickSort(moreA));
	}
	
	public Number[] concatenate(Number[] a, Number b, Number[] c) {
		int len = a.length + 1 + c.length;
		Number[] r = new Number[len];
		int pos = 0;
		for (int i = 0; i < a.length; ++i)
			r[pos++] = a[i];
		r[pos++] = b;
		for (int i = 0; i < c.length; ++i)
			r[pos++] = c[i];
		return r;
	}
	
}
