package ryan.shavell.main.resources;

import ryan.shavell.main.stuff.Log;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: don't allow the same looping track to be started if it already exists
//TODO: Function to stop all tracks already playing

public class AudioHandler implements LineListener {

    private static Map<String, Clip> clips = new HashMap<>();

    private static List<String> audioToStop = new ArrayList<>();

    private static List<AudioHandler> newAudio = new ArrayList<>();
    private static List<AudioHandler> handlers = new ArrayList<>();

    private String audioFileName;
    private boolean loop = false, playCompleted = false;
    private Clip audioClip = null;

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

            audioClip = getClip(audioFileName);
            audioClip.addLineListener(this);

            audioClip.start();
        } catch (Exception e) {
            Log.e("AudioHandler.play() exception!");
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
            //audioClip.close();
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
                String s;
                while (audioToStop.size() > 0 && (s = audioToStop.get(0)) != null) {
                    boolean noMatch = true;
                    for (AudioHandler a : new ArrayList<>(handlers)) {
                        if (a.audioFileName.equalsIgnoreCase(s)) {
                            a.audioClip.stop();
                            //a.audioClip.close();
                            a.loop = false;
                            a.playCompleted = true;
                            handlers.remove(a);
                            audioToStop.remove(s);
                            noMatch = false;
                            System.out.println("Found command to remove \"" + s + "\"");
                        }
                    }
                    if (noMatch) audioToStop.remove(s);
                }
                for (AudioHandler a : new ArrayList<>(handlers)) {
                    if (a.playCompleted) {
                        if (a.loop) {
                            a.play();
                        } else {
                            a.audioClip.removeLineListener(a);
                            //a.audioClip.
                            a.audioClip = null;
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

    public static void stopSong(String audioFileName) {
        if (audioFileName != null) {
            audioToStop.add("music/" + audioFileName);
            System.out.println("Gave command to remove \"" + audioFileName + "\"");
        }
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
