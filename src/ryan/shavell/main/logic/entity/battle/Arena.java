package ryan.shavell.main.logic.entity.battle;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.dialogue.actions.ActionTalk;
import ryan.shavell.main.dialogue.actions.DialogAction;
import ryan.shavell.main.logic.InputTaker;
import ryan.shavell.main.logic.SoulType;
import ryan.shavell.main.dialogue.DialogBox;
import ryan.shavell.main.render.Board;
import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.resources.AudioHandler;
import ryan.shavell.main.resources.ImageLoader;
import ryan.shavell.main.resources.SpriteSheet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Arena implements InputTaker, Drawable {

    //TODO: fighting, health stuff, code cleanup

    private SpriteSheet buttons;
    private SoulType soulType;

    private BufferedImage hp;

    private DialogBox dialogBox;

    //private ScrollText primaryText;

    private Mob mob;

    private int battleBox_height = 0;
    private int battleBox_width = 0;

    private final Point fightPoint = new Point(33, 433);
    private final Point actPoint = new Point(186, 433);
    private final Point itemPoint = new Point(346, 433);
    private final Point mercyPoint = new Point(501, 433);

    private int selected = 0;
    private int subMenu = -1;

    private boolean playerTurn = true;
    private boolean mobHandlingDialog = false;

    private List<DialogAction> mobProvidedActions = new ArrayList<>();

    private String[] oldOptions = null;

    public Arena(Mob mob) {
        this.mob = mob;
        buttons = new SpriteSheet(110, 42, 2, 4, "battle_buttons");
        soulType = SoulType.NORMAL;

        dialogBox = new DialogBox(251);

        hp = ImageLoader.getImage("hp");

        dialogBox.setText("* ASRIEL takes a stand!");
    }

    public void optionSelect(int option, int whichSubMenu) {
        //TODO: remove this after implementing properly
        /*
        dialogBox.setText("Selected option " + option + " \"" + dialogBox.getOptions()[option] + "\" in menu " + whichSubMenu + ".", true);
        subMenu = -1;
        selected = -1;
        */

        if (subMenu == 0) { //FIGHT
            //TODO: make the fight box GUI thing appear
        } else if (subMenu == 1) {//ACT
            mobHandlingDialog = true;
            mobProvidedActions = mob.onACT(dialogBox.getOptions()[option]);
            System.out.println("Got " + mobProvidedActions.size() + " actions");
            subMenu = -1;
            selected = -1;
        } else if (subMenu == 2) { //ITEM
            //TODO: pull item info from item info class/list
        } else if (subMenu == 3) { //MERCY
            if (option == 0) { //Spare
                //TODO: spare
            } else { //Flee
                dialogBox.setText("I'm outta here...", true);
                subMenu = -1;
                selected = -1;
            }
        }
    }

    @Override
    public void onKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int oldSelected = selected;
        int oldSubMenu = subMenu;

        if (subMenu == -1 && !dialogBox.shouldBlockInput()) {
            if (keyCode == KeyEvent.VK_RIGHT) {
                if (subMenu == -1) {
                    if (selected != 3) {
                        selected++;
                    }
                }
            } else if (keyCode == KeyEvent.VK_LEFT) {
                if (subMenu == -1) {
                    if (selected != 0) {
                        selected--;
                    }
                }
            } else if (keyCode == KeyEvent.VK_Z) {
                if (subMenu == -1) {
                    subMenu = selected;
                    AudioHandler.playEffect("menu_select");
                }
            }
        }
        if (keyCode == KeyEvent.VK_X) {
            if (subMenu != -1) {
                subMenu = -1;
                dialogBox.setText(dialogBox.getLastText());
            }
        }
        if (selected != oldSelected) {
            AudioHandler.playEffect("menu_scroll");
        }

        if (subMenu == oldSubMenu) {
            dialogBox.onKeyPress(e);
        }
    }

    @Override
    public void onKeyRelease(KeyEvent e) {
        dialogBox.onKeyRelease(e);
    }

    int type = 0;
    int ticks = 0;

    @Override
    public void tick() {
        if (subMenu != -1) {
            String[] options = {};
            if (subMenu == 0) {
                options = new String[]{mob.getName()};
            } else if (subMenu == 1) {
                options = mob.getACT();
            } else if (subMenu == 2) {
                options = new String[]{"CrustyPotato", "Carrot", "Fineapple"};
            } else if (subMenu == 3) {
                options = new String[]{"Spare", "Flee"};
            }
            if (!Arrays.equals(options, oldOptions) || !dialogBox.isOptions()) {
                dialogBox.setOptions(options);
            }
            oldOptions = options;

            if (dialogBox.shouldMoveOn() && dialogBox.isOptions()) {
                optionSelect(dialogBox.getSelectedOption(), subMenu);
            }
        }
        //TODO: make this line not a clustertruck
        if (subMenu == -1 && /*dialogBox.shouldBlockInput() &&*/ mobHandlingDialog && mobProvidedActions.size() > 0) {
            //TODO: scroll to next dialog piece, provided by mob most of the time?
            DialogAction current = mobProvidedActions.get(0);
            if (current instanceof ActionTalk) {
                //System.out.println("PROCESSING ACTION");
                if (!current.hasRun()) {
                    current.run(dialogBox);
                    System.out.println("Ran action");
                }
                if (current.isDone(dialogBox)) {
                    System.out.println("Action complete");
                    mobProvidedActions.remove(current);
                }
            }
            //dialogBox.setText("* ASRIEL attacks you.", true);
        }
        dialogBox.tick();
    }

    String mobMusic = null;

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Board.self.getWidth(), Board.self.getHeight());

        g.drawImage(getFight(), fightPoint.x, fightPoint.y, null);
        g.drawImage(getAct(), actPoint.x, actPoint.y, null);
        g.drawImage(getItem(), itemPoint.x, itemPoint.y, null);
        g.drawImage(getMercy(), mercyPoint.x, mercyPoint.y, null);

        Point selectedPoint = null;
        if (selected == 0) selectedPoint = fightPoint;
        else if (selected == 1) selectedPoint = actPoint;
        else if (selected == 2) selectedPoint = itemPoint;
        else if (selected == 3) selectedPoint = mercyPoint;
        //else System.out.println("INVALID SELECTED MENU ITEM \"" + selected + "\"!!!");

        if (selectedPoint != null && subMenu == -1) {
            g.drawImage(soulType.getImage(), selectedPoint.x + 8, selectedPoint.y + 14, null);
        }

        BufferedImage image = mob.getImage();
        int width = image.getWidth();
        int height = image.getHeight();
        if (mob.shouldDoubleSize()) {
            width *= 2;
            height *= 2;
        }

        int x = (Board.self.getWidth() / 2) - (width / 2);
        g.drawImage(mob.getImage(), x, mob.getY(), width, height, null);

        drawPlayerInfo(g);

        dialogBox.draw(g);

        //TODO: move health bar code to either DialogBox or a correct spot here
        /*
                g.drawString("* " + mob.getName(),  optionOnePoint.x, optionOnePoint.y);

                int hBarX = optionOnePoint.x + 165, hBarY = optionOnePoint.y - 17, hBarWidth = 101, hBarHeight = 17;

                g.setColor(UnderColor.RED);
                g.fillRect(hBarX, hBarY, hBarWidth, hBarHeight);
                g.setColor(UnderColor.GREEN);

                //TODO: add getHealthPercent() to mob
                double percent = (mob.getCurrentHealth() * 1.0) / (mob.getMaxHealth() * 1.0);
                int partialWidth = (int) Math.round(hBarWidth * percent);
                //System.out.println(percent + "%, width: " + partialWidth);
                g.fillRect(hBarX, hBarY, partialWidth, hBarHeight);

                //TODO: health bar 62 pixels away from the name if the name is "Dummy"
        */

        String music = mob.getMusic();
        if (music != null) {
            if (!music.equals(mobMusic)) {
                AudioHandler.playSong(music, true);
                //TODO: stop existing music if it wasn't null
            }
            mobMusic = music;
        } else {
            mobMusic = null;
            //TODO: if it wasn't null before, stop the existing music (if there is any)
        }
    }

    public void drawPlayerInfo(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(Main.SQUISH_MENU);

        //TODO: all of this is roughly correct, but perfect it and do it right

        Point stats = new Point(33, 420);
        g.drawString("RYAN  LV 1", stats.x, stats.y); //TODO: pull name and level variables from player info once that exists

        g.setColor(Color.YELLOW);
        Rectangle rect = new Rectangle(actPoint.x + 90, stats.y - 18, 25, 21);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);

        g.drawImage(hp, rect.x - 31, rect.y + 5, null); //TODO: tune perfectly (268, 408)

        g.setColor(Color.WHITE);
        g.drawString("20 / 20", rect.x + rect.width + 14, stats.y);
    }

    public BufferedImage getFight() {
        return buttons.getImage(0, selected == 0 ? 1 : 0);
    }

    public BufferedImage getAct() {
        return buttons.getImage(1, selected == 1 ? 1 : 0);
    }

    public BufferedImage getItem() {
        return buttons.getImage(0, selected == 2 ? 3 : 2);
    }

    public BufferedImage getMercy() {
        return buttons.getImage(1, selected == 3 ? 3 : 2);
    }

    @Override
    public boolean shouldDoubleSize() {
        return false;
    }
}
