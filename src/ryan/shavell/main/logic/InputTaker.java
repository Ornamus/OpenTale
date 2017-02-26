package ryan.shavell.main.logic;

import java.awt.event.KeyEvent;

/**
 * An interface for any Object that will need to receive key press and key release events.
 *
 * @author Ornamus
 * @version 2017.2.26
 */
public interface InputTaker {

    void onKeyPress(KeyEvent e);
    default void onKeyRelease(KeyEvent e) {}
}
