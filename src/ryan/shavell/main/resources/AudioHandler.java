package ryan.shavell.main.resources;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

public class AudioHandler implements Runnable, LineListener {

    private static List<AudioHandler> handlers = new ArrayList<>();

    private String audioFileName;
    private boolean loop, playCompleted;

    //TODO: Function to stop all/certain tracks already playing

    private AudioHandler(String audioFileName, boolean loop) {
        this.audioFileName = audioFileName;
        this.loop = loop;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(audioFileName + ".wav"));

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, format);

            //TODO: lower music more than other game audio


            //TODO: looping not working?
            boolean didAtAll = false;
            while (loop || !didAtAll) {
                Clip audioClip = (Clip) AudioSystem.getLine(info);


                audioClip.addLineListener(this);

                audioClip.open(audioStream);

                FloatControl gainControl =
                        (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f); // Reduce volume by 20 decibels.

                playCompleted = false;
                audioClip.start();

                while (!playCompleted) {
                    // wait for the playback completes
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                audioClip.close();
                didAtAll = true;
            }

        } catch (Exception ex) {
            System.out.println("[ERROR] AudioHandler exception!");
            ex.printStackTrace();
        }
    }

    /**
     * Listens to the START and STOP events of the audio line.
     */
    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();

        if (type == LineEvent.Type.START) {
            //System.out.println("Playback started.");

        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            //System.out.println("Playback completed.");
        }

    }

    /**
     * Play a given song file.
     *
     * @param audioFileName Name of the audio file.
     * @param loop If the song should loop.
     */
    public static void playSong(String audioFileName, boolean loop) {
        handlers.add(new AudioHandler("music/" + audioFileName, loop));
    }

    /**
     * Play a given audio effect.
     *
     * @param audioFileName Name of the audio file.
     */
    public static void playEffect(String audioFileName) {
        handlers.add(new AudioHandler("sound_effects/" + audioFileName, false));
    }
}
