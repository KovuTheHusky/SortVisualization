package com.kovuthehusky.sortvisualization;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public final class AudioEngine {
    private static final int SAMPLE_RATE = 44000;
    private static final AudioFormat af = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);
    private int length = 10;
    private SourceDataLine line;
    private boolean muted = false;
    private float volume = 50F;

    public AudioEngine() {
        try {
            line = AudioSystem.getSourceDataLine(af);
            line.open(af, 4400);
            this.setVolume(volume);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        line.start();
    }

    public int getLength() {
        return length;
    }

    public float getVolume() {
        return volume;
    }

    public boolean isMuted() {
        return muted;
    }

    public void play(Number num, Number num2) {
        if (muted || SortVisualization.isMuted()) {
            try {
                Thread.sleep(length);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        byte[] arr = this.generateSineWavefreq((int) (num.getValue() * 1.2));
        byte[] arr2 = this.generateSineWavefreq((int) (num2.getValue() * 1.2));
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

    public void setLength(int length) {
        if (length < 10)
            length = 10;
        else if (length > 1000)
            length = 1000;
        this.length = length;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        volume = volume / 100F;
        float decibels = (float) (Math.log(volume == 0.0F ? 0.0001F : volume) / Math.log(10.0) * 10.0);
        if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(decibels);
        }
    }

    private byte[] generateSineWavefreq(int freq) {
        byte[] sin = new byte[(int) ((double) length / 1000 * SAMPLE_RATE)];
        double samplingInterval;
        samplingInterval = SAMPLE_RATE / freq;
        for (int i = 0; i < sin.length; i++) {
            double angle = 2.0 * Math.PI * i / samplingInterval;
            sin[i] = (byte) (Math.sin(angle) * 127);
        }
        return sin;
    }
}
