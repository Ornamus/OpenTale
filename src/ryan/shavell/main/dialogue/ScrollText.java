package ryan.shavell.main.dialogue;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.resources.AudioHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ScrollText implements Drawable {

    public static final int SCROLL_INSTANT = -1;
    public static final int SCROLL_SLOW = 2;
    public static final int SCROLL_NORMAL = 1;
    public static final int SCROLL_FAST = 0;

    private String text = "";
    private int x, y;

    private int speed = SCROLL_NORMAL;
    private int widthLimit = -1;
    private Color color = Color.WHITE;
    private Font font = Main.DIALOGUE;

    private int waitedTicks = 0;
    private int currentCharacter = -1;

    private boolean calculatedBreaks = false;
    private List<String> breaks = new ArrayList<>();

    public ScrollText(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getScrollSpeed() {
        return speed;
    }

    public String getText() {
        return text;
    }

    public boolean isScrolling() {
        return currentCharacter != -1;
    }

    public void setVisuals(Color c, Font f) {
        color = c;
        font = f;
    }

    public void forceEndScroll() {
        currentCharacter = -1;
    }

    public ScrollText setText(String newText, int scrollSpeed) {
        text = newText;
        speed = scrollSpeed;

        currentCharacter = 0;
        waitedTicks = 0;

        breaks.clear();
        calculatedBreaks = false;

        return this;
    }

    public ScrollText setWidthLimit(int lim) {
        widthLimit = lim;
        return this;
    }

    public void replay() {
        setText(text, speed);
    }

    @Override
    public void tick() {
        if (currentCharacter != -1) {
            if (waitedTicks == speed) {
                waitedTicks = 0;
                currentCharacter++;
                if (currentCharacter == (text.length())) {
                    currentCharacter = -1;
                }
            } else {
                waitedTicks++;
            }
        }
    }

    //TODO: Don't indent breaks if there isn't a *.
    //TODO: Make double spacing not push text past widthLimit
    private int oldWidthLimit = widthLimit;
    private Font oldFont = font;
    private String oldText = text;

    @Override
    public void draw(Graphics2D g) {
        boolean recalculate = oldWidthLimit != widthLimit || !oldFont.equals(font) || !oldText.equals(text);
        oldWidthLimit = widthLimit;
        oldFont = font;
        oldText = text;
        if (recalculate) calculatedBreaks = false;
        g.setFont(font);
        if (!calculatedBreaks) {
            //if (!text.equals("")) System.out.println("Calculating breaks for \"" + text + "\".");
            String textCopy = text;
            breaks = new ArrayList<>();
            while ((widthLimit != -1 && (g.getFontMetrics().stringWidth(textCopy) > widthLimit)) || textCopy.contains("\n")) {
                int indexOfBreakable = -1;
                boolean newLine = false;
                for (int i=0; i<textCopy.length(); i++) {
                    char c = textCopy.charAt(i);
                    //System.out.println("" + c);
                    newLine = (c == '/' && textCopy.charAt(i+1) == 'n');
                    //if (newLine) System.out.println("newline break!");
                    if ((c == ' ' || c == '-' || newLine) && (g.getFontMetrics().stringWidth(textCopy.substring(0, i)) < widthLimit) || newLine) {
                        indexOfBreakable = i;
                        if (newLine) break;
                    }
                }
                if (indexOfBreakable != -1) {
                    String newBreak = textCopy.substring(0, indexOfBreakable); //TODO: tweak second arg
                    //if (newLine) newBreak = newBreak.replace("/n", "");
                    //if (breaks.size() > 0) {
                        //while (!newBreak.startsWith("  ") && !newBreak.startsWith("*")) newBreak = " " + newBreak;
                    //}
                    breaks.add(newBreak);
                    textCopy = textCopy.substring(indexOfBreakable, textCopy.length()); //TODO: tweak first arg to match the other arg up there ^^
                    if (newLine) textCopy = textCopy.replaceFirst("/n", "");
                }
            }
            breaks.add(textCopy);

            //TODO: do this while breaks is being calculated so that extra spaces don't push text out of bounds
            List<String> breaksCopy = new ArrayList<>(breaks);
            breaks.clear();
            boolean breakStartsWithStar = false;
            for (String s : breaksCopy) {
                while (s.startsWith(" ")) s = s.replaceFirst(" ", "");
                if (s.startsWith("*")) {
                    breakStartsWithStar = true;
                    break;
                }
            }
            int index = 0;
            for (String s : breaksCopy) {
                while (s.startsWith(" ")) s = s.replaceFirst(" ", "");
                if (index > 0) {
                    if (!s.startsWith("*") && breakStartsWithStar)
                        while (!s.startsWith("  ") && !s.startsWith("*")) s = " " + s;
                }
                breaks.add(s);
                index++;
            }

            /*
            System.out.println("Breaks: ");
            for (String s : breaks) {
                System.out.println(s);
            }
            */

            calculatedBreaks = true;
        }
        //Render
        g.setColor(color);
        if (speed == SCROLL_INSTANT || currentCharacter == -1) {
            int index = 0;
            for (String s : breaks) {
                g.drawString(s, x, y + (index * 34));
                index++;
            }
        } else {
            int index = 0;
            int sumLength = 0;
            for (String s : breaks) {
                sumLength += s.length();

                //TODO: tune line spacing and handle properly

                if (currentCharacter >= sumLength) {
                    g.drawString(s, x, y + (index * 34));
                    //System.out.println("perfect");
                } else if (currentCharacter > (sumLength - s.length())) {
                    //System.out.println("inbetween");

                    g.drawString(s.substring(0, currentCharacter - (sumLength - s.length())), x, y + (index * 34));
                }
                index++;
            }

            if (waitedTicks == 0 && text.charAt(currentCharacter) != ' ') {
                AudioHandler.playEffect("generic_text");
            }
        }
    }
}