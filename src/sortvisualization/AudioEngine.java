package sortvisualization;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public final class AudioEngine {

	private static final int SAMPLE_RATE = 44000;
	private static final AudioFormat af = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);

	private static SourceDataLine line;
	private static int length = 10;

	static {
		try {
			line = AudioSystem.getSourceDataLine(af);
			line.open(af, 4400);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		line.start();
	}

	private static byte[] generateSineWavefreq(int freq) {
		byte[] sin = new byte[(int)((double)length / 1000 * SAMPLE_RATE)];
		double samplingInterval;
		samplingInterval = (double)(SAMPLE_RATE / freq);
		for (int i = 0; i < sin.length; i++) {
			double angle = (2.0 * Math.PI * i) / samplingInterval;
			sin[i] = (byte)(Math.sin(angle) * 127);
		}
		return sin;
	}

	public static void play(Number num, Number num2) {
		byte[] arr = AudioEngine.generateSineWavefreq(num.getValue());
		byte[] arr2 = AudioEngine.generateSineWavefreq(num2.getValue());
		try {
			line.write(arr, 0, arr.length);
			line.write(arr2, 0, arr2.length);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
			try {
				Thread.sleep(AudioEngine.getLength());
			} catch (InterruptedException ex2) {
				ex2.printStackTrace();
			}
		}
		if (line.available() >= 4400)
			line.drain();
	}
	
	public static int getLength() {
		return AudioEngine.length;
	}

	public static void setLength(int length) {
		if (length < 10)
			length = 10;
		else if (length > 1000)
			length = 1000;
		AudioEngine.length = length;
	}
	
	public static void nop() {
		return;
	}

}
