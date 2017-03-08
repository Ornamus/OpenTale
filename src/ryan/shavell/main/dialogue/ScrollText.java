package ryan.shavell.main.dialogue;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.resources.AudioHandler;
import ryan.shavell.main.stuff.Log;
import ryan.shavell.main.stuff.Utils;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

//TODO: DOES ONLY PAPYRUS HAVE SHAKY TEXT?

public class ScrollText implements Drawable {

    public static final int SCROLL_INSTANT = -1;
    public static final int SCROLL_SLOW = 2;
    public static final int SCROLL_NORMAL = 1;
    public static final int SCROLL_FAST = 0;

    private String text = "";
    private int x, y;

    private List<TextMetadata> allMetadata = new ArrayList<>();

    private String typeSound = "generic_text";

    private int speed = SCROLL_NORMAL;
    private int widthLimit = -1;
    private Color color = Color.WHITE;
    private Font font = Main.DIALOGUE;

    private int waitedTicks = 0;
    private int currentCharacter = -1;

    private boolean calculatedBreaks = false;
    private List<String> breaks = new ArrayList<>();

    private Character[] delayCharacters = {',', '.', '?', '!'};
    private Character[] noVoiceCharacters = {' ', '-', '\'', ',', '*'};

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

    public Font getFont() {
        return font;
    }

    public boolean isScrolling() {
        return currentCharacter != -1;
    }

    public void setVisuals(Color c, Font f) {
        color = c;
        font = f;
    }

    public void setSound(String sound) {
        typeSound = sound;
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
        allMetadata.clear();
        calculatedBreaks = false;

        return this;
    }

    private String test = "I think you are going to have a [color(150,0,0)BAD TIME].";

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
                } else {                  
                    if (currentCharacter - 1 >= 0 && currentCharacter - 1 < text.length()) {
                        char c = text.charAt(currentCharacter - 1);
                        for (Character c2 : delayCharacters) {
                            if (c == c2) {
                                waitedTicks -= 3;
                                break;
                            }
                        }
                    }
                }
            } else {
                waitedTicks++;
            }
        }
    }

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
        while (text.contains("[") && text.contains("]")) {
            int start = text.indexOf('[');
            int end = text.indexOf(']');
            String metadata = text.substring(start + 1, end);

            //while ((metadata = text.substring(start + 1, end)).length() > 0) {
            int argIndex = metadata.indexOf("(");
            int argEndIndex = metadata.indexOf(")");
            String function = metadata.substring(0, argIndex);
            String args = metadata.substring(argIndex + 1, argEndIndex);
            String message = metadata.substring(argEndIndex + 1, metadata.length());
            //Log.d("\nMetadata: " + metadata + "\nFunction: " + function + "\nargs: " + args + "\nMessage: " + message);

            text = text.replace("[" + metadata + "]", message);

            TextMetadata textData = new TextMetadata(start, start + message.length(), message);

            if (function.equalsIgnoreCase("color")) {
                String[] parts = args.split(",");
                Color c = new Color(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                textData.color = c;
                //Log.d("Should color \"" + textData.text + "\" " + c.toString());
            }
            //}
            allMetadata.add(textData);

        }
        if (!calculatedBreaks) {
            //if (!text.equals("")) System.out.println("Calculating breaks for \"" + text + "\".");
            String textCopy = text;
            breaks = new ArrayList<>();
            while ((widthLimit != -1 && (g.getFontMetrics().stringWidth(textCopy) > widthLimit)) || textCopy.contains("/n")) {
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
            int wholeStringIndex = 0;
            for (String s : breaksCopy) {
                String original = s;
                while (s.startsWith(" ")) s = s.replaceFirst(" ", "");
                if (index > 0) {
                    if (!s.startsWith("*") && breakStartsWithStar)
                        while (!s.startsWith("  ") && !s.startsWith("*")) s = " " + s;
                }

                int diff = (s.length() - original.length());
                for (TextMetadata m : allMetadata) {
                    if (m.start <= wholeStringIndex + s.length() && !m.frontShifted) {
                        m.start += diff;
                        Log.d("Shifted front by " + diff + " on break " + index);
                        m.frontShifted = true;
                    }
                    if (m.end <= wholeStringIndex + s.length() && !m.endShifted) {
                        m.end += diff;
                        Log.d("Shifted end by " + -diff + " on break " + index);
                        m.endShifted = true;
                    }
                }
                breaks.add(s);
                index++;
                wholeStringIndex += s.length();
            }
            //System.out.println("Done shifting");
            //System.out.println("text length: " + text.length() + ", break sum: " + wholeStringIndex);

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

        Rectangle2D rect = g.getFontMetrics().getStringBounds("boop", g);
        int offset = (int) Math.round(rect.getHeight()) + 7;
        if (speed == SCROLL_INSTANT || currentCharacter == -1 || text.equals("")) {
            int index = 0;
            int sumLength = 0;
            for (String s : breaks) {
                drawString(g, s, x, y + (index * offset), sumLength);

                index++;
                sumLength += s.length();
            }
        } else {
            int index = 0;
            int sumLength = 0;
            for (String s : breaks) {
                sumLength += s.length();

                //TODO: tune line spacing and handle properly

                int breakStartIndex = sumLength - s.length();

                if (currentCharacter >= sumLength) { //whole break is being rendered
                    drawString(g, s, x, y + (index * offset), breakStartIndex);
                } else if (currentCharacter > breakStartIndex) { //part of the break is being rendered
                    drawString(g, s.substring(0, currentCharacter - breakStartIndex), x, y + (index * offset), breakStartIndex);
                }
                index++;
            }

            if (waitedTicks == 0) {
                boolean voice = true;
                char c = text.charAt(currentCharacter);
                for (Character c2 : noVoiceCharacters) {
                    if (c == c2) {
                        voice = false;
                        break;
                    }
                }
                if (voice) {
                    AudioHandler.playEffect(typeSound);
                }
            }
        }
    }

    private final boolean doingFancyText = true;

    public void drawString(Graphics2D g, String string, int x, int y, int startIndex) {
        if (doingFancyText) {
            FontMetrics metrics = g.getFontMetrics();
            int spaceWidth = metrics.stringWidth("ab") - (metrics.stringWidth("a") + metrics.stringWidth("b"));
            int currX = x;
            for (int i=0; i< string.length(); i++) {
                String curr = string.charAt(i) + "";

                Color c = color;
                for (TextMetadata m : allMetadata) {
                    if (m.start <= startIndex + i && m.end > startIndex + i) {
                        if (m.color != null) c = m.color;
                    }
                }
                g.setColor(c);

                int xShift = 0;
                int yShift = 0;

                if (Utils.randomNumber(0, 1000) == 0) xShift = Utils.randomNumber(0, 1) == 0 ? 1 : -1;
                if (Utils.randomNumber(0, 1000) == 0) yShift = Utils.randomNumber(0, 1) == 0 ? 1 : -1;

                g.drawString(curr + "", currX + xShift, y + yShift);
                currX += metrics.stringWidth(curr) + spaceWidth;
            }
        } else {
            g.drawString(string, x, y);
        }
    }

    private class TextMetadata {

        public int start, end;
        public final String text;
        public Color color = null;
        public boolean frontShifted = false, endShifted = false;

        TextMetadata(int start, int end, String text) {
            this.start = start;
            this.end = end;
            this.text = text;
        }
    }
}