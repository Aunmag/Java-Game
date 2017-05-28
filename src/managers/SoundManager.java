package managers;

import client.Client;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

// Created by Aunmag on 17.11.2016.

public class SoundManager {

    // Sound quality settings:
    private static final int rate = 44100; // 44100, 22050, 11025, 8000
    private static final int buffer = 1024; // 1024, 512, 256, 128
    private static final int bitDepth = 16; // 32, 24, 16, 8

    private Clip clip;
    private FloatControl floatControl;

    public SoundManager(String path) {

        try {
            InputStream inputStream = getClass().getResourceAsStream(path);
            InputStream inputStreamBuffered = new BufferedInputStream(inputStream);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStreamBuffered);
            AudioFormat format = audioInputStream.getFormat();
            AudioFormat formatDecoded = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), false);
            AudioInputStream audioInputStreamDecoded = AudioSystem.getAudioInputStream(formatDecoded, audioInputStream);
            clip = AudioSystem.getClip();
            clip.open(audioInputStreamDecoded);
            floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void makeSound(float x2, float y2) {

        if (clip == null) {
            return;
        }

        stop();

        clip.setFramePosition(0);

        float x1 = Client.getPlayer().getX();
        float y1 = Client.getPlayer().getY();
        float distance = (float) (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));

        setVolume(-distance / 25);

        while (!clip.isRunning()) {
            clip.start();
        }

    }

    public void play() {

        if (clip == null) {
            return;
        }

        stop();

        clip.setFramePosition(0);

        while (!clip.isRunning()) {
            clip.start();
        }

    }

    public void loop() {

        if (clip == null) {
            return;
        }

        clip.loop(Clip.LOOP_CONTINUOUSLY);

        while (!clip.isRunning()) {
            clip.start();
        }

    }

    public void stop() {

        if (clip == null) {
            return;
        }

        if (clip.isRunning()) {
            clip.stop();
        }

    }

    public void close() {

        stop();
        clip.drain();
        clip.close();

    }

    @Override public void finalize() {

        close();

    }

    // Setters:

    public void setVolume(float volume) {

        floatControl.setValue((float) volume);

    }


}
