package ryan.shavell.main.dialogue.actions;

import ryan.shavell.main.resources.AudioHandler;

public class ActionSoundEffect extends Action {
    public ActionSoundEffect(String soundEffect) {
        super(()-> AudioHandler.playEffect(soundEffect), ()->true);
    }
}
