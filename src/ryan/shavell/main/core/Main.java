package ryan.shavell.main.core;

import ryan.shavell.main.render.Board;
import ryan.shavell.main.stuff.Input;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private Board board;
    private Input input;

    public static Font MENU, DIALOGUE, SQUISH_MENU, BATTLE_DIALOG;

    public Main() {
        try {
            //Sans is menus, Mono is dialogue
            DIALOGUE = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("DTM-Mono.otf")).deriveFont(Font.PLAIN, 27);
            MENU = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("DTM-Sans.otf")).deriveFont(Font.PLAIN, 27);
            SQUISH_MENU = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("Mars.ttf")).deriveFont(Font.PLAIN, 24); //TODO: almost perfect 52
            BATTLE_DIALOG = DIALOGUE.deriveFont(Font.PLAIN, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        board = new Board();
        add(board);
        input = new Input();
        addKeyListener(input);
        addMouseListener(input);
        setSize(640 + 6, 480 + 29);
        setResizable(false);
        setTitle("OPENTALE");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //AudioHandler.play("Home", true);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
