package ryan.shavell.main.resources;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AudioHandler implements LineListener {

    private static Map<String, Clip> clips = new HashMap<>();

    private static List<AudioHandler> newAudio = new ArrayList<>();
    private static List<AudioHandler> handlers = new ArrayList<>();

    private String audioFileName;
    private boolean loop = false, playCompleted = false;
    private Clip audioClip = null;

    //TODO: Function to stop all/certain tracks already playing

    private AudioHandler(String audioName, boolean loop) {
        this.audioFileName = audioName;
        this.loop = loop;
        /*
        Thread t = new Thread(()-> {
            play();
            hasPlayed = true;
        });
        t.start();
        */
    }

    public void play() {
        try {
            playCompleted = false;

            Clip audioClip = getClip(audioFileName);
            audioClip.addLineListener(this);

            audioClip.start();
        } catch (Exception e) {
            System.out.println("[ERROR] AudioHandler.play() exception!");
            e.printStackTrace();
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
            audioClip.close();
            //System.out.println("Playback completed.");
        }

    }
    private static Thread t;

    public static void init() {
        t = new Thread(()-> {

            while (true) {
                AudioHandler h;
                while (newAudio.size() > 0 && (h = newAudio.get(0)) != null) {
                    h.play();
                    if (h.loop) System.out.println("A looping sound has loaded");
                    newAudio.remove(h);
                    handlers.add(h);
                }
                for (AudioHandler a : new ArrayList<>(handlers)) {
                    if (a.playCompleted) {
                        if (a.loop) {
                            a.play();
                        } else {
                            //a.audioClip.removeLineListener(a);
                            handlers.remove(a);
                        }
                    }
                }
                try {
                    Thread.sleep(25);
                } catch (Exception e) {}
            }
        });
        t.start();
    }

    /**
     * Play a given song file.
     *
     * @param audioFileName Name of the audio file.
     * @param loop If the song should loop.
     */
    public static void playSong(String audioFileName, boolean loop) {
        newAudio.add(new AudioHandler("music/" + audioFileName, loop));
    }

    /**
     * Play a given audio effect.
     *
     * @param audioFileName Name of the audio file.
     */
    public static void playEffect(String audioFileName) {
        newAudio.add(new AudioHandler("sound_effects/" + audioFileName, false));
    }

    public static Clip getClip(String originalAudioFile) {
        String audioFile = originalAudioFile;
        while (true) {
            boolean clipExists = false;
            List<String> keysetCopy = new ArrayList<>(clips.keySet());
            for (String s : keysetCopy) {
                if (s.equalsIgnoreCase(audioFile)) {
                    clipExists = true;
                    Clip c = clips.get(s);
                    if (c.isRunning()) {
                        audioFile = audioFile + "#";
                        break;
                    } else {
                        //c.close();
                        c.setFramePosition(0);
                        return c;
                    }
                }
            }
            if (!clipExists) break;
        }
        try {
            //System.out.println("Making a new clip");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(AudioHandler.class.getResource(originalAudioFile + ".wav"));
            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, format);

            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f); // Reduce volume by 20 decibels.

            clips.put(audioFile, clip);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
