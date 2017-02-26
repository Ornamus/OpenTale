package ryan.shavell.main.resources;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

//TODO: change how this works so there is no synchronization calls being made on the main game logic thread (in/out buffer lists :) )

public class AudioHandler implements LineListener {

    private static List<AudioHandler> handlers = new ArrayList<>();
    private static Object HANDLERS_USE = new Object();

    private String audioFileName;
    private boolean loop = false, playCompleted = false;
    private Clip audioClip = null;

    //TODO: Function to stop all/certain tracks already playing

    private AudioHandler(String audioName, boolean loop) {
        this.audioFileName = audioName;
        this.loop = loop;

        play();
        //if (this.loop) System.out.println("A looping sound has been started!");
    }

    public void play() {
        try {
            playCompleted = false;

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(AudioHandler.class.getResource(audioFileName + ".wav"));
            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, format);

            //TODO: lower music more than other game audio

            audioClip = (Clip) AudioSystem.getLine(info);

            audioClip.addLineListener(this);

            audioClip.open(audioStream);

            FloatControl gainControl =
                    (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f); // Reduce volume by 20 decibels.

            audioClip.start();
        } catch (Exception e) {
            System.out.println("[ERROR] AudioHandler.play() exception!");
            e.printStackTrace();
        }
    }

    public static void tick() {
        for (AudioHandler a : new ArrayList<>(handlers)) {
            if (a.playCompleted) {
                a.audioClip.close();
                if (a.loop) {
                    a.play();
                } else {
                    synchronized (HANDLERS_USE) {
                        handlers.remove(a);
                    }
                }
            }
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
        synchronized (HANDLERS_USE) {
            handlers.add(new AudioHandler("music/" + audioFileName, loop));
        }
    }

    /**
     * Play a given audio effect.
     *
     * @param audioFileName Name of the audio file.
     */
    public static void playEffect(String audioFileName) {
        synchronized (HANDLERS_USE) {
            handlers.add(new AudioHandler("sound_effects/" + audioFileName, false));
        }
    }
}
