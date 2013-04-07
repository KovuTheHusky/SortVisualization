package sortvisualization;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public class AudioEngine {

	private static final int SAMPLE_RATE = 44000;
	
	private static SourceDataLine line;
	private static int soundLength = 10;

	public AudioEngine() {
		final AudioFormat af = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);
		try {
			line = AudioSystem.getSourceDataLine(af);
			line.open(af);
			line.start();
			FloatControl gainControl = (FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-20.0f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static byte[] generateSineWavefreq(int freq, int millis) {
		millis = soundLength;
		byte[] sin = new byte[(int)((double)millis / 1000 * SAMPLE_RATE)];
		double samplingInterval;
		samplingInterval = (double)(SAMPLE_RATE / freq);
		for (int i = 0; i < sin.length; i++) {
			double angle = (2.0 * Math.PI * i) / samplingInterval;
			sin[i] = (byte)(Math.sin(angle) * 127);
		}
		return sin;
	}

	public static void play(Number num, Number num2, int millis) {
		millis = soundLength;
		byte[] arr = AudioEngine.generateSineWavefreq(num.getValue(), millis);
		byte[] arr2 = AudioEngine.generateSineWavefreq(num2.getValue(), millis);
		line.write(arr, 0, arr.length);
		line.write(arr2, 0, arr2.length);
		line.drain();
	}
	
	public static void setSoundLength(int len) {
		soundLength = len;
	}

}
