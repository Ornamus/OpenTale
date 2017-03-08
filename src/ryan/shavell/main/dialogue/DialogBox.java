package ryan.shavell.main.dialogue;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.core.player.PlayerInfo;
import ryan.shavell.main.logic.InputTaker;
import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.resources.AudioHandler;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Displays text and options within a chat box. Uses ScrollText for all fancy text things.
 *
 * @author Ornamus
 * @version 2017.3.7
 */
public class DialogBox implements Drawable, InputTaker {

    //private static final int x = 33;
    private static final int dialogWidth = 575, dialogHeight = 140;

    private Point primaryTextPoint, optionOnePoint, optionTwoPoint, optionThreePoint, optionFourPoint;

    private ScrollText optionOne, optionTwo, optionThree, optionFour;
    private String[] options = null;
    private int optionSelected = -1;
    private int forSureOptionSelected = -1;

    private int x, y;
    private ScrollText text;
    private int scrollSpeed = ScrollText.SCROLL_NORMAL;

    private String lastText = null;

    private boolean isOptions, blocking, skippable, shouldMoveOn;
    private boolean renderBackground = true;

    public DialogBox(int y) {
        this(33, y);
    }

    public DialogBox(int x, int y) {
        this(x, y, x + 20, y + 46); //x + 22, y + 40
    }

    //TODO: when to use DIALOGUE vs MENU
    public DialogBox(int x, int y, int textX, int textY) {
        this.x = x;
        this.y = y;

        primaryTextPoint = new Point(textX, textY);

        text = new ScrollText(primaryTextPoint.x, primaryTextPoint.y);
        text.setWidthLimit(dialogWidth - x);

        System.out.println("Text: " + primaryTextPoint.getX());

        optionOnePoint = new Point(primaryTextPoint.x + 48, primaryTextPoint.y); //formerly 30
        optionTwoPoint = new Point(optionOnePoint.x, optionOnePoint.y + 32);
        optionThreePoint = new Point(optionOnePoint.x + 256, optionOnePoint.y); //formerly 250
        optionFourPoint = new Point(optionThreePoint.x, optionTwoPoint.y);

        optionOne = new ScrollText(optionOnePoint.x, optionOnePoint.y);
        optionTwo = new ScrollText(optionTwoPoint.x, optionTwoPoint.y);
        optionThree = new ScrollText(optionThreePoint.x, optionThreePoint.y);
        optionFour = new ScrollText(optionFourPoint.x, optionFourPoint.y);

        text.setVisuals(Color.WHITE, Main.DIALOGUE);

        optionOne.setVisuals(Color.WHITE, Main.DIALOGUE);
        optionTwo.setVisuals(Color.WHITE, Main.DIALOGUE);
        optionThree.setVisuals(Color.WHITE, Main.DIALOGUE);
        optionFour.setVisuals(Color.WHITE, Main.DIALOGUE);

        //setVisuals(Color.WHITE, Main.DIALOGUE);
    }

    /**
     * Sets what the text in this DialogBox looks like.
     *
     * @param c The Color of the text.
     * @param f The Font of the text.
     */
    public void setVisuals(Color c, Font f) {
        text.setVisuals(c, f);
        optionOne.setVisuals(c, f);
        optionTwo.setVisuals(c, f);
        optionThree.setVisuals(c, f);
        optionFour.setVisuals(c, f);
    }

    public void setColors(Color t, Color o1, Color o2, Color o3, Color o4) {
        text.setVisuals(t, text.getFont());
        optionOne.setVisuals(o1, optionOne.getFont());
        optionTwo.setVisuals(o2, optionTwo.getFont());
        optionThree.setVisuals(o3, optionThree.getFont());
        optionFour.setVisuals(o4, optionFour.getFont());
    }

    /**
     * Sets all relevant variables and Objects back to their defaults.
     */
    private void setDefaults() {
        isOptions = false;
        blocking = false;
        skippable = true;
        shouldMoveOn = false;

        options = null;
        optionSelected = -1;
        forSureOptionSelected = -1;

        text.setText("", ScrollText.SCROLL_INSTANT);
        optionOne.setText("", ScrollText.SCROLL_INSTANT);
        optionTwo.setText("", ScrollText.SCROLL_INSTANT);
        optionThree.setText("", ScrollText.SCROLL_INSTANT);
        optionFour.setText("", ScrollText.SCROLL_INSTANT);
    }

    /**
     * Sets what options are available for the player to select in this DialogBox.
     * The primary text does not render while options are being shown.
     *
     * @param options The options.
     */
    public void setOptions(String... options) {
        setDefaults();
        this.options = options;
        isOptions = true;

        if (options.length > 0) optionOne.setText("* " + options[0], ScrollText.SCROLL_INSTANT);
        if (options.length > 1) optionTwo.setText("* " + options[1], ScrollText.SCROLL_INSTANT);
        if (options.length > 2) optionThree.setText("* " + options[2], ScrollText.SCROLL_INSTANT);
        if (options.length > 3) optionFour.setText("* " + options[3], ScrollText.SCROLL_INSTANT);
        if (options.length > 4) {
            System.out.println("More than four options were passed to a DialogBox! The options:");
            for (String s : options) {
                System.out.println(s);
            }
        }
        optionSelected = 0;
    }

    /**
     * Sets the primary text that is displayed in this DialogBox. Options will not render while this
     * text is being shown.
     *
     * @param string The text.
     */
    public void setText(String string) {
        setDefaults();
        text.setText(string, scrollSpeed);
        lastText = string;
    }

    /**
     * Sets the primary text that is displayed in this DialogBox. Options will not render while this
     * text is being shown.
     *
     * @param string The text.
     * @param blocking If this text should block key input.
     */
    public void setText(String string, boolean blocking) {
        setText(string);
        this.blocking = blocking;
    }

    public void setText(String string, String sound) {
        text.setSound(sound);
        setText(string);
    }

    /**
     * Gets the current primary text being displayed.
     *
     * @return The current primary text being displayed. Will be null if options are currently being displayed.
     */
    public String getText() {
        if (isOptions) {
            return null;
        } else {
            return text.getText();
        }
    }

    public void setTextWidthLimit(int limit) {
        text.setWidthLimit(limit);
    }

    public void setScrollSpeed(int speed) {
        this.scrollSpeed = speed;
    }

    public String getLastText() {
        return lastText;
    }

    /**
     * Gets if this text is currently playing the text scrolling animation.
     *
     * @return If this text is currently playing the text scrolling animation.
     */
    public boolean isScrolling() {
        return text.isScrolling();
    }

    public boolean shouldBlockInput() {
        return blocking;
    }

    /**
     * Gets if this DialogBox is ready to "move on." This means the box is either done displaying it's text
     * or an option has been selected.
     *
     * @return If this DialogBox is ready to "move on."
     */
    public boolean shouldMoveOn() {
        String text = getText();
        return shouldMoveOn || (text != null && text.equals(""));
    }

    public int getSelectedOption() {
        return forSureOptionSelected;
    }

    public String[] getOptions() {
        return options;
    }

    public boolean isOptions() {
        return isOptions;
    }

    @Override
    public void onKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (isOptions) {
            //System.out.println("press");
            int oldOption = optionSelected;
            if (keyCode == KeyEvent.VK_RIGHT) {
                if (optionSelected <= 1) {
                    optionSelected += 2;
                }
            } else if (keyCode == KeyEvent.VK_LEFT) {
                if (optionSelected >= 2) {
                    optionSelected -= 2;
                }
            } else if (keyCode == KeyEvent.VK_UP) {
                if (optionSelected == 1 || optionSelected == 3) {
                    optionSelected--;
                }
            } else if (keyCode == KeyEvent.VK_DOWN) {
                if (optionSelected == 0 || optionSelected == 2) {
                    optionSelected++;
                }
            } else if (keyCode == KeyEvent.VK_Z) {
                forSureOptionSelected = optionSelected;
                shouldMoveOn = true;
                AudioHandler.playEffect("menu_select");
            }
            if (optionSelected >= options.length) {
                optionSelected = oldOption;
            }
            if (optionSelected != oldOption) {
               // AudioHandler.playEffect("menu_scroll"); //TODO: Apparently Undertale doesn't use the scroll sound in the options menu! :(
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_X && skippable) {
                text.forceEndScroll();
            } else if (e.getKeyCode() == KeyEvent.VK_Z && !isScrolling()) {
                shouldMoveOn = true;
            }
        }
    }

    @Override
    public void onKeyRelease(KeyEvent e) {

    }

    @Override
    public void tick() {
        text.tick();
        optionOne.tick();
        optionTwo.tick();
        optionThree.tick();
        optionFour.tick();
    }

    /**
     * Draws the background of this DialogBox. Typically just a black box with a white border.
     *
     * @param g The Graphics2D object this will be drawn on.
     */
    public void drawBackground(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, dialogWidth, dialogHeight);
        for (int i = 0; i < 5; i++) {
            g.setColor(Color.WHITE);
            g.drawRect(x + i, y + i, dialogWidth - (i * 2), dialogHeight - (i * 2));
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (renderBackground) drawBackground(g);
        text.draw(g);
        optionOne.draw(g);
        optionTwo.draw(g);
        optionThree.draw(g);
        optionFour.draw(g);
        if (isOptions && optionSelected != -1) {
            Point p;
            if (optionSelected == 0) p= optionOnePoint;
            else if (optionSelected == 1) p = optionTwoPoint;
            else if (optionSelected == 2) p = optionThreePoint;
            else if (optionSelected == 3) p = optionFourPoint;
            else {
                System.out.println("INVALID POINT SELECTED IN DIALOGBOX");
                p = null;
            }
            g.drawImage(PlayerInfo.soulType.getImage(), p.x - 35, p.y - 18, null); //formerly x - 28, y - 16
        }
    }

    @Override
    public boolean shouldDoubleSize() {
        return false;
    }

    public boolean shouldRenderBackground() {
        return renderBackground;
    }

    public void setRenderBackground(boolean b) {
        renderBackground = b;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return dialogWidth;
    }

    public ScrollText getMainText() {
        return text;
    }

    public int getHeight() {
        return dialogHeight;
    }
}
