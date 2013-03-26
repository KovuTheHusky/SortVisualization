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

}
