package ryan.shavell.main.core;

import ryan.shavell.main.render.Board;
import ryan.shavell.main.resources.AudioHandler;
import ryan.shavell.main.stuff.Input;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private Board board;
    private Input input;

    public static Font MENU, DIALOGUE, SQUISH_MENU, BATTLE_DIALOG, BATTLE_NUMBERS, SANS, PAPYRUS;

    public static final int WIDTH = 640, HEIGHT = 480;

    public Main() {
        try {
            DIALOGUE = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("DTM-Mono.otf").openStream()).deriveFont(Font.PLAIN, 27);
            MENU = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("DTM-Sans.otf").openStream()).deriveFont(Font.PLAIN, 27);
            SQUISH_MENU = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("Mars.ttf").openStream()).deriveFont(Font.PLAIN, 24); //TODO: almost perfect 52
            BATTLE_DIALOG = DIALOGUE.deriveFont(Font.PLAIN, 16);
            BATTLE_NUMBERS = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("hachicro.ttf").openStream()).deriveFont(Font.PLAIN, 30); //TODO: a little small, spacing is off either way though
            SANS = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("Comic Sans UT.ttf").openStream()).deriveFont(Font.PLAIN, 32);
            PAPYRUS = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("Papyrus UT.ttf").openStream()).deriveFont(Font.PLAIN, 32);
        } catch (Exception e) {
            e.printStackTrace();
        }
        board = new Board();
        add(board);
        input = new Input();
        addKeyListener(input);
        addMouseListener(input);
        setSize(WIDTH + 6, HEIGHT + 29);
        setResizable(false);
        setTitle("OPENTALE");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        AudioHandler.init();

        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
