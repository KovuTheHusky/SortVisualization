package sortvisualization;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public final class AudioEngine {

	private static final int SAMPLE_RATE = 44000;
	private static final AudioFormat af = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);

	private SourceDataLine line;
	private int length = 10;
	private boolean muted = false;

	public AudioEngine() {
		try {
			line = AudioSystem.getSourceDataLine(af);
			line.open(af, 4400);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		line.start();
	}

	private byte[] generateSineWavefreq(int freq) {
		byte[] sin = new byte[(int)((double)length / 1000 * SAMPLE_RATE)];
		double samplingInterval;
		samplingInterval = (double)(SAMPLE_RATE / freq);
		for (int i = 0; i < sin.length; i++) {
			double angle = (2.0 * Math.PI * i) / samplingInterval;
			sin[i] = (byte)(Math.sin(angle) * 127);
		}
		return sin;
	}

	public void play(Number num, Number num2) {
		if (this.muted || SortVisualization.isMuted()) {
			try {
				Thread.sleep(length);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		byte[] arr = this.generateSineWavefreq((int)(num.getValue() * 1.2));
		byte[] arr2 = this.generateSineWavefreq((int)(num2.getValue() * 1.2));
		try {
			line.write(arr, 0, arr.length);
			line.write(arr2, 0, arr2.length);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
			try {
				Thread.sleep(length);
			} catch (InterruptedException ex2) {
				ex2.printStackTrace();
			}
		}
		if (line.available() >= 4400)
			line.drain();
	}

	public int getLength() {
		return this.length;
	}

	public void setLength(int length) {
		if (length < 10)
			length = 10;
		else if (length > 1000)
			length = 1000;
		this.length = length;
	}
	
	public boolean isMuted() {
		return this.muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

}
