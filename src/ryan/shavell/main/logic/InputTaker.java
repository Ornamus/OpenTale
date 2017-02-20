package ryan.shavell.main.logic;

import java.awt.event.KeyEvent;

public interface InputTaker {

    void onKeyPress(KeyEvent e);
    void onKeyRelease(KeyEvent e);
}
