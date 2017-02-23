package ryan.shavell.main.logic.entity.battle;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.core.player.PlayerInfo;
import ryan.shavell.main.core.player.Weapon;
import ryan.shavell.main.dialogue.ChatBox;
import ryan.shavell.main.dialogue.actions.ActionDialog;
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
import ryan.shavell.main.resources.UnderColor;

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
    private BufferedImage fightGUI;

    private SpriteSheet battlePointer;

    private static DialogBox dialogBox;
    private static ChatBox chatBubble;

    private static Mob mob;

    private final Point fightPoint = new Point(33, 433);
    private final Point actPoint = new Point(186, 433);
    private final Point itemPoint = new Point(346, 433);
    private final Point mercyPoint = new Point(501, 433);

    private int selected = 0;
    private int subMenu = -1;

    private boolean freezeMenu = false;

    private boolean doingAttack = false;
    private boolean didTarget = false;
    private boolean midAttack = false;
    private boolean damageAnim = false;
    private boolean dealtDamage = false;
    private int battleTargeterX;
    private int healthLost = 0;
    private long timeOfDamageDeal = -1;

    private List<DialogAction> actions = new ArrayList<>();

    private String[] oldOptions = null;

    private String mobMusic = null;
    private boolean oldDidTarget = didTarget;

    public Arena(Mob mob) {
        this.mob = mob;
        buttons = new SpriteSheet(110, 42, 2, 4, "battle_buttons");
        soulType = SoulType.NORMAL;

        dialogBox = new DialogBox(251);
        chatBubble = new ChatBox(80);

        hp = ImageLoader.getImage("hp");
        fightGUI = ImageLoader.getImage("fight_gui");
        battlePointer = new SpriteSheet(14, 128, 2, 1, "battle_target");

        dialogBox.setText("* ASRIEL takes a stand!");
    }

    public void optionSelect(int option, int whichSubMenu) {
        if (whichSubMenu == 0) { //FIGHT
            freezeMenu = true;
            doingAttack = true;
            didTarget = false;
            damageAnim = false;
            dealtDamage = false;
            timeOfDamageDeal = -1;
            battleTargeterX = dialogBox.getX() + 5;
            subMenu = -1;
            selected = -1;
            dialogBox.setText("");
        } else if (whichSubMenu == 1) {//ACT
            String optionString = dialogBox.getOptions()[option];
            if (optionString.equalsIgnoreCase("Check")) {
                actions.clear();
                actions.add(new ActionDialog(mob.getCheckInfo()));
            } else {
                actions = mob.onACT(optionString);
                dialogBox.setText("");
                //System.out.println("Got " + actions.size() + " actions");
            }
            subMenu = -1;
            selected = -1;
            freezeMenu = true;
        } else if (whichSubMenu == 2) { //ITEM
            if (option == 3) {
                dialogBox.setText("* It's an old meme, sir, but it checks out.");
                subMenu = -1;
            }
            //TODO: pull item info from item info class/list
        } else if (whichSubMenu == 3) { //MERCY
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

        if (!freezeMenu && !doingAttack) {
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
            if (actions.isEmpty()) {
                if (selected != oldSelected) {
                    AudioHandler.playEffect("menu_scroll");
                }
            } else {
                selected = -1;
            }
        } else if (doingAttack && !didTarget) {
            if (keyCode == KeyEvent.VK_Z) {
                didTarget = true;
            }
        }

        if (subMenu == oldSubMenu) {
            dialogBox.onKeyPress(e);
        }
        chatBubble.onKeyPress(e);
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
                options = new String[mob.getACT().length + 1];
                options[0] = "Check";
                int index = 1;
                for (String s : mob.getACT()) {
                    options[index] = s;
                    index++;
                }
            } else if (subMenu == 2) {
                options = new String[]{"CrustyPotato", "Carrot", "Fineapple", "HairSampler"};
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
        if (subMenu == -1 && actions.size() > 0) {
            DialogAction current = actions.get(0);
            //System.out.println("PROCESSING ACTION");
            if (!current.hasRun()) {
                current.run();
                System.out.println("Ran action");
            }
            if (current.isDone()) {
                System.out.println("Action complete");
                actions.remove(current);
            }
        }

        Weapon w = PlayerInfo.weapon;
        if (doingAttack && !didTarget) {
            battleTargeterX += 7; //8
        } else if (doingAttack && didTarget && !oldDidTarget) { //Runs right when the player locked in
            w.reset();
            AudioHandler.playEffect(w.getSound());
            midAttack = true;
        } else if (doingAttack && damageAnim && !dealtDamage) {
            //TODO: damage is different based off of where the attack target was
            int damage = w.getDamage();
            mob.setCurrentHealth(mob.getCurrentHealth() - damage);
            if (mob.getCurrentHealth() < 0) mob.setCurrentHealth(0);
            healthLost = damage;
            timeOfDamageDeal = System.currentTimeMillis();
            dealtDamage = true;
            AudioHandler.playEffect("mob_hit");
        } else if (doingAttack && dealtDamage && (System.currentTimeMillis() - timeOfDamageDeal) >= 1250) {
            doingAttack = false;

            //TODO: start mob attack instead of all of this
            freezeMenu = false;
            selected = 0;
            dialogBox.setText("* ASRIEL seems shaken.");
        }
        oldDidTarget = didTarget;

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

        dialogBox.tick();
        chatBubble.tick();
    }

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

        if (selectedPoint != null && subMenu == -1 && !freezeMenu) {
            g.drawImage(soulType.getImage(), selectedPoint.x + 8, selectedPoint.y + 14, null);
        }

        BufferedImage image = mob.getImage();
        int width = image.getWidth();
        int height = image.getHeight();
        if (mob.shouldDoubleSize()) {
            width *= 2;
            height *= 2;
        }

        int centerX = (Board.self.getWidth() / 2);
        int x = centerX - (width / 2);

        g.drawImage(mob.getImage(), x, mob.getY(), width, height, null);

        drawPlayerInfo(g);
        dialogBox.draw(g);
        chatBubble.draw(g);

        if (subMenu == 1) {
            /*
            g.drawString("* " + mob.getName(),  optionOnePoint.x, optionOnePoint.y);

            int hBarX = optionOnePoint.x + 165, hBarY = optionOnePoint.y - 17, hBarWidth = 101, hBarHeight = 17;

            g.setColor(UnderColor.UNDER_HEALTHBAR_RED);
            g.fillRect(hBarX, hBarY, hBarWidth, hBarHeight);
            g.setColor(UnderColor.GREEN);

            //TODO: use getHealthPercent() in mob
            double percent = (mob.getCurrentHealth() * 1.0) / (mob.getMaxHealth() * 1.0);
            int partialWidth = (int) Math.round(hBarWidth * percent);

            g.fillRect(hBarX, hBarY, partialWidth, hBarHeight);
            */
        }

        if (doingAttack) {
            g.drawImage(fightGUI, dialogBox.getX() + 15, dialogBox.getY() + 14, null);
            g.drawImage(battlePointer.getImage(didTarget ? 1 : 0, 0), battleTargeterX, dialogBox.getY() + 6, null);
            if (midAttack && !damageAnim) {
                //TODO: animation only runs properly the first attack, breaks after that. DEBUG
                if (!PlayerInfo.weapon.isAnimationDone()) {
                    PlayerInfo.weapon.drawEffect(centerX, mob.getY(), g);
                } else {
                    damageAnim = true;
                }
            } else if (dealtDamage) {
                //TODO: ANIMATE THE HEALTH BAR GOING DOWN UGHUGHUGUHDHGH
                //TODO: Mob sprite changes and shakes
                //TODO: bouncing damage numbers
                int hBarWidth = 103, hBarHeight = 15;
                int hBarX = centerX - (hBarWidth / 2), hBarY = mob.getY() - 25;

                g.setColor(Color.black);
                g.drawRect(hBarX, hBarY, hBarWidth, hBarHeight);

                g.setColor(UnderColor.GRAY);
                g.fillRect(hBarX + 1, hBarY + 1, hBarWidth - 2, hBarHeight - 2);

                int fillAmount = (int) Math.round((hBarWidth - 2.0) * mob.getHealthPercent());
                g.setColor(UnderColor.GREEN);
                g.fillRect(hBarX + 1, hBarY + 1, fillAmount, hBarHeight - 2);

                g.setColor(UnderColor.RED);
                g.setFont(Main.BATTLE_NUMBERS);

                String damage;
                if (healthLost > 0) {
                    damage = "" + healthLost;
                } else {
                    damage = "MISS";
                }
                g.drawString(damage, centerX - (g.getFontMetrics().stringWidth(damage) / 2), hBarY - 15);

                //TODO: deal with this
                if (mob.isBoss()) {

                } else {

                }
            }
        }
    }

    public void drawPlayerInfo(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(Main.SQUISH_MENU);

        //TODO: all of this is roughly correct, but perfect it

        Point stats = new Point(33, 420);
        g.drawString(PlayerInfo.name + "  LV " + PlayerInfo.level, stats.x, stats.y);
        //g.drawString("SHIFTY  LV 1", stats.x, stats.y); //TODO: pull name and level variables from player info once that exists

        g.setColor(Color.YELLOW);
        Rectangle rect = new Rectangle(actPoint.x + 90, stats.y - 18, 25, 21);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);

        g.drawImage(hp, rect.x - 31, rect.y + 5, null); //TODO: tune perfectly (268, 408) (might be perfect now)

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

    public static DialogBox getDialogBox() {
        return dialogBox;
    }

    public static ChatBox getChatBubble() {
        return chatBubble;
    }

    public static Mob getMob() {
        return mob;
    }
}
