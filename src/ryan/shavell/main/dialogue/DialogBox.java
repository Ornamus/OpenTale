package ryan.shavell.main.dialogue;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.logic.InputTaker;
import ryan.shavell.main.logic.SoulType;
import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.resources.AudioHandler;
import java.awt.*;
import java.awt.event.KeyEvent;

public class DialogBox implements Drawable, InputTaker {

    private static final int x = 33;
    private static final int dialogWidth = 575, dialogHeight = 140;

    private Point primaryTextPoint, optionOnePoint, optionTwoPoint, optionThreePoint, optionFourPoint;

    private ScrollText optionOne, optionTwo, optionThree, optionFour;
    private String[] options = null;
    private int optionSelected = -1;
    private int forSureOptionSelected = -1;

    private int y;
    private ScrollText text;

    private String lastText = null;

    private boolean isOptions, blocking, skippable, shouldMoveOn;

    //TODO: some sort of "lock movement" criteria that would let Arena know not to allow menu movement while this spells text
    //TODO: tune a little better? (maybe not)
    public DialogBox(int y) {
        this.y = y;

        primaryTextPoint = new Point(x + 22, y + 40);

        text = new ScrollText(primaryTextPoint.x, primaryTextPoint.y);
        text.setVisuals(Color.WHITE, Main.DIALOGUE);
        text.setWidthLimit(dialogWidth - x);

        optionOnePoint = new Point(primaryTextPoint.x + 30, primaryTextPoint.y);
        optionTwoPoint = new Point(optionOnePoint.x, optionOnePoint.y + 34); //formerly y + 40
        optionThreePoint = new Point(optionOnePoint.x + 250, optionOnePoint.y);
        optionFourPoint = new Point(optionThreePoint.x, optionTwoPoint.y);

        optionOne = new ScrollText(optionOnePoint.x, optionOnePoint.y);
        optionTwo = new ScrollText(optionTwoPoint.x, optionTwoPoint.y);
        optionThree = new ScrollText(optionThreePoint.x, optionThreePoint.y);
        optionFour = new ScrollText(optionFourPoint.x, optionFourPoint.y);

        optionOne.setVisuals(Color.WHITE, Main.MENU);
        optionTwo.setVisuals(Color.WHITE, Main.MENU);
        optionThree.setVisuals(Color.WHITE, Main.MENU);
        optionFour.setVisuals(Color.WHITE, Main.MENU);
    }

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

    public void setText(String string) {
        setDefaults();
        text.setText(string, ScrollText.SCROLL_NORMAL);
        lastText = string;
    }

    public void setText(String string, boolean blocking) {
        setText(string);
        this.blocking = blocking;
    }

    public String getText() {
        if (isOptions) {
            return null;
        } else {
            return text.getText();
        }
    }

    public String getLastText() {
        return lastText;
    }

    public boolean isScrolling() {
        return text.isScrolling();
    }

    public boolean shouldBlockInput() {
        return blocking;
    }

    public boolean shouldMoveOn() {
        return shouldMoveOn;
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
                AudioHandler.playEffect("menu_scroll");
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_X && skippable) {
                text.forceEndScroll();
            } else if (e.getKeyCode() == KeyEvent.VK_Z && !isScrolling()) { //TODO: Having shouldMoveOn work like this may be really unhelpfull
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

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, dialogWidth, dialogHeight);
        for (int i = 0; i < 5; i++) {
            g.setColor(Color.WHITE);
            g.drawRect(x + i, y + i, dialogWidth - (i * 2), dialogHeight - (i * 2));
        }
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
            g.drawImage(SoulType.NORMAL.getImage(), p.x - 28, p.y - 16, null);
        }
    }

    @Override
    public boolean shouldDoubleSize() {
        return false;
    }
}
