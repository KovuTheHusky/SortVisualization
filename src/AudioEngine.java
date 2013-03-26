import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;


public class AudioEngine {
	
	private static final int sampleRate = 44000;
	public static SourceDataLine line;

	public AudioEngine() {
		final AudioFormat af = new AudioFormat(sampleRate, 16, 1, true, true);
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
	
	private static byte[] generateSineWavefreq(int frequencyOfSignal, int freq2, int seconds) {
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
	
	public static void play(Number f, Number f2, int seconds) {
		int freq = f.getValue();
		int freq2 = f2.getValue();
		//int length = sampleRate * array.length / 1000;
		byte[] array = AudioEngine.generateSineWavefreq(0, freq2, seconds);
		byte[] array2 = AudioEngine.generateSineWavefreq(freq, 0, seconds);
        line.write(array, 0, array.length);
        line.write(array2, 0, array2.length);
        line.drain();
	}
	
	public static void play(int frequency, int seconds) {
		int freq = frequency;
		//int length = sampleRate * array.length / 1000;
		byte[] array = AudioEngine.generateSineWavefreq(freq, 0, seconds);
        line.write(array, 0, array.length);
        line.drain();
	}

}
