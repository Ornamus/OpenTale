package ryan.shavell.main.logic.entity.battle;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.core.player.PlayerInfo;
import ryan.shavell.main.core.player.Weapon;
import ryan.shavell.main.dialogue.ChatBox;
import ryan.shavell.main.dialogue.actions.ActionDialog;
import ryan.shavell.main.dialogue.actions.ActionStartAttack;
import ryan.shavell.main.dialogue.actions.Action;
import ryan.shavell.main.dialogue.actions.ActionTriggerPreAttack;
import ryan.shavell.main.logic.InputTaker;
import ryan.shavell.main.logic.SoulType;
import ryan.shavell.main.dialogue.DialogBox;
import ryan.shavell.main.logic.entity.battle.attacks.*;
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

    //TODO: code cleanup, prettify

    public static Arena self;

    private SpriteSheet buttons;

    private BufferedImage hp;
    private BufferedImage fightGUI;
    private BufferedImage background = null;

    private SpriteSheet battlePointer;

    private static SoulType soulType;

    private static DialogBox dialogBox;
    private static ChatBox chatBubble;

    private static BattleBox battleBox;

    private static Mob mob;

    private final Point fightPoint = new Point(33, 433);
    private final Point actPoint = new Point(186, 433);
    private final Point itemPoint = new Point(346, 433);
    private final Point mercyPoint = new Point(501, 433);

    public int selected = 0;
    public int subMenu = -1;

    public boolean freezeMenu = false;

    private boolean doingAttack = false;
    private boolean didTarget = false;
    private boolean midAttack = false;
    private boolean damageAnim = false;
    private boolean dealtDamage = false;
    private int battleTargeterX;
    private int healthLost = 0;
    private long timeOfDamageDeal = -1;
    public List<Action> actions = new ArrayList<>();

    public boolean mobTurn = false;

    private String[] oldOptions = null;

    private String mobMusic = null;
    private boolean oldDidTarget = didTarget;

    private int damageShakeTargetX = 0;
    private int adjustAmount = 15;
    private Long timeSinceLastAdjust = null;

    public Arena(Mob mob) {
        self = this;
        this.mob = mob;

        if (mob.isBoss()) {
            background = ImageLoader.getImage("battle_background_boss");
        }

        buttons = new SpriteSheet(110, 42, 2, 4, "ss_battle_buttons");
        soulType = SoulType.NORMAL;

        dialogBox = new DialogBox(251);
        chatBubble = new ChatBox(80);

        battleBox = new BattleBox(Main.WIDTH / 2);

        hp = ImageLoader.getImage("hp");
        fightGUI = ImageLoader.getImage("fight_gui");
        battlePointer = new SpriteSheet(14, 128, 2, 1, "battle_target");

        dialogBox.setText("* Asriel takes a stand!");
        //dialogBox.setText("* You feel like you're going to have a bad time.");
    }

    public void resetAttackVariables() {
        doingAttack = false;
        didTarget = false;
        midAttack = false;
        damageAnim = false;
        dealtDamage = false;
        timeOfDamageDeal = -1;
        battleTargeterX = dialogBox.getX() + 5;
        damageShakeTargetX = 0;
        adjustAmount = 15;
        timeSinceLastAdjust = null;
    }

    public void optionSelect(int option, int whichSubMenu) {
        if (whichSubMenu == 0) { //FIGHT
            resetAttackVariables();
            doingAttack = true;
            freezeMenu = true;
            subMenu = -1;
            selected = -1;
            dialogBox.setText("");
        } else if (whichSubMenu == 1) {//ACT
            String optionString = dialogBox.getOptions()[option];
            if (optionString.equalsIgnoreCase("Check")) {
                actions.clear();
                actions.add(new ActionDialog(mob.getCheckInfo()));
                actions.add(new ActionTriggerPreAttack());
            } else {
                actions = mob.onACT(optionString);
                //System.out.println("Got " + actions.size() + " actions");
            }
            dialogBox.setText("");
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
            chatBubble.onKeyPress(e);
        }
        if (mobTurn) battleBox.onKeyPress(e);
        //chatBubble.onKeyPress(e);
    }

    @Override
    public void onKeyRelease(KeyEvent e) {
        dialogBox.onKeyRelease(e);
        chatBubble.onKeyRelease(e);
        if (mobTurn) battleBox.onKeyRelease(e);
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
            Action current = actions.get(0);
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

        boolean missed = false;
        if (doingAttack && !didTarget && battleTargeterX > dialogBox.getX() + dialogBox.getWidth()) {
            didTarget = true;
            missed = true;
            damageAnim = true;
            //System.out.println("HEEEEEEEEEEEEY");
        }

        Weapon w = PlayerInfo.weapon;
        if (doingAttack && !didTarget) {
            battleTargeterX += 7; //8
        } else if (doingAttack && didTarget && !oldDidTarget && !missed) { //Runs right when the player locked in
            w.reset();
            AudioHandler.playEffect(w.getSound());
            midAttack = true;
        } else if (doingAttack && damageAnim && !dealtDamage) {
            //TODO: damage is different based off of where the attack target was
            int damage = w.getDamage();
            if (missed) damage = 0;
            mob.setCurrentHealth(mob.getCurrentHealth() - damage);
            if (mob.getCurrentHealth() < 0) mob.setCurrentHealth(0);
            healthLost = damage;
            timeOfDamageDeal = System.currentTimeMillis();
            dealtDamage = true;
            if (!missed) AudioHandler.playEffect("mob_hit");
        } else if (doingAttack && dealtDamage && (System.currentTimeMillis() - timeOfDamageDeal) >= 1250) {
            actions.clear();
            actions = mob.onAttack();
            resetAttackVariables();
        }

        if (mobTurn && battleBox.isDone()) {
            mobTurn = false;
            actions.clear();
            actions = mob.onAfterAttack(battleBox.getAttack());
            /*
            freezeMenu = false;
            dialogBox.setText(mob.getNewTurnText());
            selected = 0;
            */
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
        if (mobTurn) battleBox.tick();
    }

    public void startMobTurn() {
        resetAttackVariables();
        selected = -1;
        freezeMenu = true;
        dialogBox.setText("");
        Attack a = mob.getNextAttack();
        battleBox.setAttack(a);
        battleBox.doResizeAnimation(575, 140, a.getWidth(), a.getHeight());
        mobTurn = true;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Board.self.getWidth(), Board.self.getHeight());

        if (background != null) {
            g.drawImage(background, 0, 0, null);
        }

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

        int centerX = Main.WIDTH / 2;
        int x = centerX - (width / 2);

        if (!dealtDamage) g.drawImage(mob.getImage(), x, mob.getY(), width, height, null);

        drawPlayerInfo(g);
        chatBubble.draw(g);

        if (mobTurn) {
            battleBox.draw(g);
        } else {
            dialogBox.draw(g);
        }

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
                    System.out.println("Animation done");
                }
            } else if (dealtDamage) {
                //TODO: Animate the health bar going down
                //TODO: bouncing damage numbers
                int renderX = x;
                if (healthLost > 0) {
                    if (timeSinceLastAdjust == null) {
                        damageShakeTargetX = x - adjustAmount;
                        timeSinceLastAdjust = System.currentTimeMillis();
                    }
                    if (timeSinceLastAdjust != null && System.currentTimeMillis() - timeSinceLastAdjust > 100){
                        adjustAmount -= 3;
                        if (adjustAmount <= 0) {
                            damageShakeTargetX = x;
                        } else {
                            if (damageShakeTargetX > x) {
                                damageShakeTargetX = x - adjustAmount;
                            } else {
                                damageShakeTargetX = x + adjustAmount;
                            }
                        }
                        timeSinceLastAdjust = System.currentTimeMillis();
                    }
                    renderX = damageShakeTargetX;
                }
                BufferedImage mobImage = healthLost > 0 ? mob.getHitImage() : mob.getImage();
                g.drawImage(mobImage, renderX, mob.getY(), width, height, null);

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
                    g.setColor(Color.WHITE);
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

        g.setColor(UnderColor.UNDER_HEALTHBAR_RED);
        Rectangle rect = new Rectangle(actPoint.x + 90, stats.y - 18, 25, 21);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);

        g.setColor(UnderColor.YELLOW);
        double percent = (PlayerInfo.currentHealth * 1.0) / (PlayerInfo.maxHealth * 1.0);
        int healthWidth = (int) Math.round(rect.width * percent);
        g.fillRect(rect.x, rect.y, healthWidth, rect.height);

        g.drawImage(hp, rect.x - 31, rect.y + 5, null); //TODO: tune perfectly (268, 408) (might be perfect now)

        g.setColor(Color.WHITE);
        g.drawString(PlayerInfo.currentHealth + " / " + PlayerInfo.maxHealth, rect.x + rect.width + 14, stats.y);
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

    public static BattleBox getBattleBox() {
        return battleBox;
    }

    public static void setSoulType(SoulType t) {
        if (soulType != t) {
            //TODO: play sound effect
        }
        soulType = t;
    }

    public static SoulType getSoulType() {
        return soulType;
    }

    public static Mob getMob() {
        return mob;
    }
}
