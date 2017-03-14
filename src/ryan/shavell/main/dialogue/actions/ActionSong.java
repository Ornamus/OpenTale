package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.resources.AudioHandler;

public class ActionSong extends Action {
    public ActionSong(String music) {
        super(()-> AudioHandler.playSong(music, true), ()->true);
    }
}