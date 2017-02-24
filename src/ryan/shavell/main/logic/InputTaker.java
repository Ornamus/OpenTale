package ryan.shavell.main.logic;

import java.awt.event.KeyEvent;

public interface InputTaker {

    void onKeyPress(KeyEvent e);
    default void onKeyRelease(KeyEvent e) {}
}
