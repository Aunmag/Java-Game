package managers;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Created by Aunmag on 2016.11.17.
 */

public class SoundManager {

    private Clip clip;
    private FloatControl floatControl;

    public SoundManager(String path) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(path);
            InputStream inputStreamBuffered = new BufferedInputStream(inputStream);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStreamBuffered);
            AudioFormat format = audioInputStream.getFormat();
            AudioFormat formatDecoded = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    format.getSampleRate(),
                    16,
                    format.getChannels(),
                    format.getChannels() * 2,
                    format.getSampleRate(),
                    false
            );
            AudioInputStream audioInputStreamDecoded = AudioSystem.getAudioInputStream(formatDecoded, audioInputStream);
            clip = AudioSystem.getClip();
            clip.open(audioInputStreamDecoded);
            floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
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
        if (clip != null && clip.isRunning()) {
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
        floatControl.setValue(volume);
    }

}
