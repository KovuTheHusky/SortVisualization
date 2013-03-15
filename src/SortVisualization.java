import java.util.concurrent.Semaphore;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

import processing.core.*;

public class SortVisualization extends PApplet {
	
	private final int SLEEP = 1;
	
	private final int ARRAY_LENGTH = 10;
	private final int WIDTH = 1000;
	private final int HEIGHT = 1000;
	private final int MIN = 100;
	private final int MAX = 900;
	private final int BAR_WIDTH = WIDTH / ARRAY_LENGTH - 1;
	private final int SOUND_LENGTH = 10;
	
	private int[] array = new int[ARRAY_LENGTH];
	private boolean running = false;
	private int cur1 = -1, cur2 = -1, cur3 = -1, cur4 = -1, cur5 = -1;
	Semaphore semaphore = new Semaphore(1);

	private static final int sampleRate = 44000;
	private static final AudioFormat af = new AudioFormat(sampleRate, 16, 1, true, true);
	public static SourceDataLine line;
	
	long startTime;
	long endTime = 0;
	
	long secondCount = 0;
	double fps = 0;

	public void setup() {
		size(WIDTH, HEIGHT);
		//background(255);
		//stroke(0);
		//fill(0);
		for (int i = 0; i < array.length; ++i) {
			array[i] = (int)random(MIN, MAX);
			//rect(i * BAR_WIDTH, HEIGHT - array[i], BAR_WIDTH, array[i]);
		}
		final AudioFormat af = new AudioFormat(sampleRate, 16, 1, true, true);
        try {
            line = AudioSystem.getSourceDataLine(af);
            line.open(af);
            line.start();
            FloatControl gainControl = (FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f);
            //line.drain();
            //line.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void randomize() {
		for (int i = 0; i < array.length; ++i) {
			array[i] = (int)random(MIN, MAX);
			//rect(i * BAR_WIDTH, HEIGHT - array[i], BAR_WIDTH, array[i]);
		}
		running = false;
	}
	
	public static byte[] generateSineWavefreq(int frequencyOfSignal, int freq2, int seconds) {
//		frequencyOfSignal = frequencyOfSignal * 2;
//		freq2 = freq2 * 2;
		double subs = (double)seconds / 1000;
        byte[] sin = new byte[(int)(subs * sampleRate)];
        double samplingInterval;
        if (frequencyOfSignal > freq2)
        	samplingInterval = (double) (sampleRate / frequencyOfSignal);
        else
        	samplingInterval = (double) (sampleRate / freq2);
        //double samp2 = (double) (sampleRate / freq2);
        for (int i = 0; i < sin.length; i++) {
            double angle = (2.0 * Math.PI * i) / samplingInterval;
//            double angle2 = (2.0 * Math.PI * i) / samp2;
            sin[i] = (byte) (Math.sin(angle) * 127);
//            double other = Math.sin(angle2) * 127;
//            if (other > sin[i])
//            	sin[i] = (byte) (other);
        }
        return sin;
    }

	public void draw() {
		background(255);
		for (int i = 0; i < array.length; ++i) {
			if (i == cur1 || i == cur2) {
				//stroke(255, 0, 0);
				fill(0, 0, 255);
				
			} else if (i == cur3) {
				//stroke(0, 0, 255);
				fill(0, 0, 0);
			} else if (i == cur4 || i == cur5) {
				//stroke(0, 255, 0);
				if (array[cur4] >= array[cur5])
					fill(0, 255, 0);
				else
					fill(255, 0, 0);
			} else {
				//stroke(0);
				fill(0);
			}
			stroke(255);
			rect(i * BAR_WIDTH + i, HEIGHT - array[i], BAR_WIDTH, array[i]);
			
		}
		
		textSize(16);
		fill(0);
		
		if (running) {
			endTime = System.currentTimeMillis() - startTime;
		}
		text("time: " + ((double)endTime / 1000), 5, 16);
		long millisSinceLast = System.currentTimeMillis() - secondCount;
		secondCount = System.currentTimeMillis();
		if (secondCount % 10 == 0) {
			fps = 1000 / millisSinceLast;
		}
		text("fps: " + (int)fps, 5, 32);
		
		semaphore.drainPermits();
		semaphore.release();
	}
	
	public void mousePressed() {
//		if (!running)
//			thread("rMergeSort");
	}
		
	public void keyReleased() {
		if (running)
			return;
		running = true;
		if (key == 'i')
			thread("insertionSort");
		else if (key == 'm')
			thread("rMergeSort");
		else if (key == 'c')
			thread("isSorted");
		else if (key == 'n')
			randomize();
		else if (key == 'f')
			flip();
		else {
			running = false;
		}
	}
	
	private void swap(int a, int b) {
		int temp = array[a];
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
			cur4 = i;
			cur5 = i - 1;
			if (cur4 > 0) {
//				se.play(se.generateSineWavefreq(array[cur2],1));
//				se.play(se.generateSineWavefreq(array[cur1],1));
				play(array[cur4], array[cur5], SOUND_LENGTH);
				//play(array[cur1], 1);
				if (array[cur4] < array[cur5])
					play(1, 100, 1000);
			}
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (array[i] < array[i - 1]) {
				//cur4 = cur5 = -1;
				//running = false;
				//return false;
			}
		}
		cur4 = cur5 = -1;
		running = false;
		return true;
	}
	
	private void play(int freq, int freq2, int seconds) {
		byte[] array = this.generateSineWavefreq(freq, freq2, seconds);
		int length = sampleRate * array.length / 1000;
        line.write(array, 0, array.length);
        line.drain();
	}
	
	public void insertionSort() {
		startTime = System.currentTimeMillis();
		if (array.length <= 1)
			return;
		for (int i = 0; i < array.length; ++i) {
			cur3 = i;
			int min = i;
			for (int j = i + 1; j < array.length; ++j) {
				cur1 = j;
				cur2 = min;
				if (cur1 > 0) {
//					se.play(se.generateSineWavefreq(array[cur2],1));
//					se.play(se.generateSineWavefreq(array[cur1],1));
					play(array[cur2], array[cur1], SOUND_LENGTH);
					//play(array[cur1], 1);
				}
				try {
					semaphore.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (array[j] < array[min])
					min = j;
			}
			if (min != i)
				swap(i, min);
		}
		cur1 = cur2 = cur3 = -1;
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
	 
	 private void rMergeSort(int[] array, int start, int end)
	 {
	     if (end - start <= 1) return;
	     int middle = start + (end - start) / 2;
	 
	     rMergeSort(array, start, middle);
	     rMergeSort(array, middle, end);
	     merge(start, middle, end);
	 }
	
	public void merge(int start, int middle, int end) {
		int[] merge = new int[end - start];
	    int l = 0, r = 0, i = 0;
	    while (l < middle - start && r < end - middle) {
	    	cur1 = start + l;
			cur2 = middle + r;
			if (cur1 > -1)
				play(array[cur2], array[cur1], SOUND_LENGTH);
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	if (array[start + l] < array[middle + r])
	    		merge[i++] = array[start + l++];
	    	else
	    		merge[i++] = array[middle + r++];
	    }
	 
	    while (r < end - middle) merge[i++] = array[middle + r++];
	 
	    while (l < middle - start) merge[i++] = array[start + l++];
	 
	    //Array.Copy(merge, 0, array, start, merge.length);
	    for (int i1 = 0; i1 < merge.length; ++i1) {
	    	array[start++] = merge[i1];
	    }
	}
	
}
