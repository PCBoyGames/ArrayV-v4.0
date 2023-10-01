package utils;

import main.ArrayVisualizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

//
//  SineContinuousjava (a part of PitchLab)
//  (Version 0.6)
//
//  Created by Gavin Shriver on 4/17/09.
//  This is a class written as part of PitchLab for a research project involving
//  human pitch perception.  This class can generate a continuous sine wave at
//  a specified frequency. The frequency can be changed on the fly as nessasary.
/**
 ** @see <a href=
 *      "https://github.com/drshriveer/sinewave/blob/master/sinewave/SineContinuous.java">
 *      The original sauce</a>
 **/
public class ArraySound implements Runnable {
    public class Phasor { // By Florian Mrugalla, The Audio Programmer Discord server
        private float phase, inc;

        public void setInc(float hz, float sampleRate) {
            inc = hz / sampleRate;
        }

        public float process() {
            phase += inc;
            if (phase >= 1.0f) {
                --phase;
            }
            return ArrayVisualizer.getInstance()
                    .getArray()[(int) (phase * ArrayVisualizer.getInstance().getCurrentLength())] * 0.01f;
        }
    }

    private static int sampleRate = 48000; // 48 kbit/s sampling rate
    private static int sampleSizeInBits = 16; // bits
    private int sampleSizeInBytes;
    private static int lineBufferSize = 5000; // bytes

    // --- Math Constructs
    private double frequency = 400.0;
    private static double amplitude = 12.0;

    private byte[] buffer;
    private boolean playing = false;

    private AudioFormat af = new AudioFormat(sampleRate, sampleSizeInBits, 1, true, true);
    private DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);

    private SourceDataLine line;
    private Phasor phs;
    private Thread player = null;

    // ******************************************************************************
    // END VARIABLES
    // BEGIN CONSTRUCTORS
    // ******************************************************************************
    public ArraySound() {
        setSampleSizeInBytes();
        buffer = new byte[getSampleSizeInBytes()];

        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(af, lineBufferSize);
        } catch (Exception e) {
            System.out.println(e);
        }
        phs = new Phasor();
    }

    // ******************************************************************************
    // END CONSTRUCTORS
    // BEGIN SOUND METHODS
    // ******************************************************************************

    public void play(double frequency) {
        setFrequency(frequency);
        playing = true;
        player = new Thread(this, "Player");
        player.start();
    }

    public void play() {
        setFrequency(200);
        playing = true;
        player = new Thread(this, "Player");
        player.start();
    }

    public void run() {
        try {
            line.start(); // begins the stream... starts playing it
            while (playing) {
                for (int i = 0; i <= (int) (((double) sampleRate / frequency)); i++) {
                    int wave = calculateWaveSample(i);
                    byte msb = (byte) (wave >>> 8);
                    byte lsb = (byte) wave;
                    buffer[0] = msb;
                    buffer[1] = lsb;
                    line.write(buffer, 0, buffer.length);

                    if (!playing)
                        break;
                }

                if (!playing)
                    break;
            } // end while
        } catch (Exception e) {
            e.printStackTrace();
        } // end catch
        line.stop();
        line.flush();
    }// end run

    public void stop() {
        setPlaying(false);
    }

    public void end() {
        line.close();
    }

    private int calculateWaveSample(int i) {
        return (int) ((amplitude)
                * ((32768.0 / ArrayVisualizer.getInstance().getCurrentLength()) * phs.process()));
    }

    // ******************************************************************************
    // END SOUND METHODS
    // BEGIN GET/SET METHODS
    // ******************************************************************************

    public synchronized void setFrequency(double frequency) {
        this.frequency = frequency;
        phs.setInc((float) frequency, (float) sampleRate);
    }

    public synchronized double getFrequency() {
        return this.frequency;
    }

    public synchronized void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean getPlaying() {
        return this.playing;
    }

    // ------------------------------------------------------
    // --- ADVANCED VARIABLES
    // ------------------------------------------------------
    public static synchronized void setSampleRate(int sampleRate) {
        ArraySound.sampleRate = sampleRate;
    }

    public static synchronized int getSampleRate() {
        return ArraySound.sampleRate;
    }

    public static synchronized void setSampleSizeInBits(int sampleSizeInBits) {
        ArraySound.sampleSizeInBits = sampleSizeInBits;
    }

    public static synchronized int getSampleSizeInBits() {
        return ArraySound.sampleSizeInBits;
    }

    public static synchronized void setLineBufferSize(int lineBufferSize) {
        ArraySound.lineBufferSize = lineBufferSize;
    }

    public static synchronized int getLineBufferSize() {
        return ArraySound.lineBufferSize;
    }

    public static synchronized void setAmplitude(double amplitude) {
        ArraySound.amplitude = amplitude;
        System.out.println("amp changed to:" + amplitude);
    }

    public static synchronized double getAmplitude() {
        return ArraySound.amplitude;
    }

    private int getSampleSizeInBytes() {
        return this.sampleSizeInBytes;
    }

    private void setSampleSizeInBytes() {
        this.sampleSizeInBytes = (int) (getSampleSizeInBits() / 8);

    }

    // ------------------------------------------------------
    // --- DEFAULTS
    // ------------------------------------------------------
    public static void setDefaults() {
        ArraySound.setLineBufferSize(5000);
        ArraySound.setSampleRate(48000);
        ArraySound.setSampleSizeInBits(16);
        ArraySound.setAmplitude(100.0);

    }
}
