package ryan.shavell.main.logic.entity.battle;

import ryan.shavell.main.core.Game;
import ryan.shavell.main.core.Main;
import ryan.shavell.main.core.player.Item;
import ryan.shavell.main.core.player.PlayerInfo;
import ryan.shavell.main.core.player.Weapon;
import ryan.shavell.main.dialogue.ChatBox;
import ryan.shavell.main.dialogue.actions.*;
import ryan.shavell.main.logic.InputTaker;
import ryan.shavell.main.logic.SoulType;
import ryan.shavell.main.dialogue.DialogBox;
import ryan.shavell.main.logic.entity.battle.attacks.*;
import ryan.shavell.main.logic.entity.overworld.Overworld;
import ryan.shavell.main.render.Board;
import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.resources.*;
import ryan.shavell.main.stuff.Log;
import ryan.shavell.main.stuff.Utils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Arena implements InputTaker, Drawable {

    //TODO: code cleanup, prettify (look into breaking some logic/variable heavy animations and the like into other classes?)

    public static Arena self;

    private SpriteSheet buttons;

    private BufferedImage hp;
    private BufferedImage fightGUI;
    private BufferedImage background = null;

    private SpriteSheet battlePointer;
    private Animation pointedSelected;

    //private static SoulType soulType;

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
    private static int turn = 0;

    private String[] oldOptions = null;

    private String mobMusic = null;
    private boolean oldDidTarget = didTarget;

    private int damageShakeTargetX = 0;
    private int adjustAmount = 15;
    private Long timeSinceLastAdjust = null;

    public Arena(Mob mob) {
        self = this;
        this.mob = mob;

        Game.setState(Game.State.ENCOUNTER);

        if (mob.isBoss()) {
            background = ImageLoader.getImage("battle_background_boss");
        }

        buttons = new SpriteSheet(110, 42, 2, 4, "ss_battle_buttons");
        PlayerInfo.soulType = SoulType.NORMAL;

        dialogBox = new DialogBox(251);
        chatBubble = new ChatBox(80);

        battleBox = new BattleBox(Main.WIDTH / 2);

        hp = ImageLoader.getImage("hp");
        fightGUI = ImageLoader.getImage("fight_gui");
        battlePointer = new SpriteSheet(14, 128, 2, 1, "battle_target");
        pointedSelected = new Animation(3, battlePointer.get(0, 0), battlePointer.get(1, 0));

        //TODO: make Mob provide the starting text
        //dialogBox.setText("* ......?");
        dialogBox.setText("* Asriel takes a stand!");

        //TODO; move this somewhere outside of arena
        PlayerInfo.currentHealth = PlayerInfo.maxHealth; //TODO: don't max out player health here, max it when resetting/SAVEing
        PlayerInfo.items.add(new Item("CrustyPotato", "Crusty Potato", 10));
        PlayerInfo.items.add(new Item("Carrot", 15));
        PlayerInfo.items.add(new Item("Fineapple", 25));
        PlayerInfo.items.add(new Item("HairSampler", "Hair Sampler", 999));
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
        firstHPRecalculate = true;
        timeSinceLastAdjust = null;
    }

    public void optionSelect(int option, int whichSubMenu) {
        if (whichSubMenu == 0) { //FIGHT
            resetAttackVariables();
            doingAttack = true;

            mob.setLastAction("FIGHT");

            freezeMenu = true;
            subMenu = -1;
            selected = -1;
            dialogBox.setText("");
        } else if (whichSubMenu == 1) {//ACT
            String optionString = dialogBox.getOptions()[option];
            actions.clear();
            if (optionString.equalsIgnoreCase("Check")) {
                actions.add(new ActionDialog(mob.getCheckInfo()));
                mob.setLastAction("CHECK");
            } else {
                actions = mob.onACT(optionString);
                mob.setLastAction(optionString);
            }
            actions.add(new ActionTriggerPreAttack());
            dialogBox.setText("");
            subMenu = -1;
            selected = -1;
            freezeMenu = true;
        } else if (whichSubMenu == 2) { //ITEM
            Item i = PlayerInfo.items.get(option);
            PlayerInfo.currentHealth += i.getHealth();
            boolean max = PlayerInfo.currentHealth >= PlayerInfo.maxHealth;
            if (max) {
                PlayerInfo.currentHealth = PlayerInfo.maxHealth;
            }
            PlayerInfo.items.remove(i);
            mob.setLastAction("ITEM");

            actions.clear();

            actions.add(new ActionSoundEffect("heal"));
            actions.add(new ActionDialog("* You eat the " + i.getName() + "./n* " + (max ? "Your HP was maxed out!" : "You gained " + i.getHealth() + " HP.")));
            actions.add(new ActionTriggerPreAttack());

            dialogBox.setText("");
            subMenu = -1;
            selected = -1;
            freezeMenu = true;
        } else if (whichSubMenu == 3) { //MERCY
            if (option == 0) { //Spare
                mob.setLastAction("SPARE");
                actions.clear();
                if (mob.isSpareable()) {
                    actions.add(new ActionDialog("* You spared " + mob.getName() + "!"));
                    //TODO: new ActionStopSound(mobMusic);
                    actions.add(new ActionDialog("* You win!/n* You earned 0 EXP and 0 gold!"));
                    actions.add(new ActionCrash()); //TODO: go back to overworld
                } else {
                    actions = mob.onSPARE();
                    actions.add(new ActionTriggerPreAttack());
                }
                dialogBox.setText("");
                subMenu = -1;
                selected = -1;
                freezeMenu = true;
            } else { //Flee
                /*
                actions.clear();
                actions.add(new ActionDialog("* I'm outta here..."));
                actions.add(new ActionCrash());
                //dialogBox.setText("I'm outta here...", true);
                subMenu = -1;
                selected = -1;
                */
            }
        }
    }

    @Override
    public void onKeyPress(KeyEvent e) {
        if (!doingDeathAnimation) {
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
    }

    @Override
    public void onKeyRelease(KeyEvent e) {
        if (!doingDeathAnimation) {
            dialogBox.onKeyRelease(e);
            chatBubble.onKeyRelease(e);
            if (mobTurn) battleBox.onKeyRelease(e);
        }
    }

    int type = 0;
    int ticks = 0;

    @Override
    public void tick() {
        if (!doingDeathAnimation) {
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
                    //TODO: support item pages :(
                    options = new String[PlayerInfo.items.size()];
                    int index = 0;
                    for (Item i : PlayerInfo.items) {
                        options[index] = i.getMenuName();
                        index++;
                    }
                } else if (subMenu == 3) {
                    options = new String[]{"Spare", "Flee"};
                }
                if (!Arrays.equals(options, oldOptions) || !dialogBox.isOptions()) {
                    dialogBox.setOptions(options);
                    if ((subMenu == 0 || subMenu == 3) && mob.isSpareable()) {
                        //TODO: Better way of coloring options?
                        Log.d("SPARE IS YELLOW");
                        dialogBox.setColors(Color.WHITE, Color.YELLOW, Color.WHITE, Color.WHITE, Color.WHITE);
                    } else {
                        dialogBox.setColors(Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
                    }
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
                    //System.out.println("Ran action");
                }
                if (current.isDone()) {
                    //System.out.println("Action complete");
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
                pointedSelected.reset();
                midAttack = true;
            } else if (doingAttack && damageAnim && !dealtDamage) {
                //TODO: damage is different based off of where the attack target was
                int damage = w.getDamage();
                if (missed) damage = 0;
                else if (mob.isSpareable()) damage = 99999;
                mob.setCurrentHealth(mob.getCurrentHealth() - damage);
                if (mob.getCurrentHealth() < 0) mob.setCurrentHealth(0);
                healthLost = damage;
                timeOfDamageDeal = System.currentTimeMillis();
                dealtDamage = true;
                if (!missed) AudioHandler.playEffect("mob_hit");
            } else if (doingAttack && dealtDamage && (System.currentTimeMillis() - timeOfDamageDeal) >= 1250) {
                actions.clear();
                if (mob.getCurrentHealth() == 0) {
                    //TODO: mob death
                } else {
                    actions = mob.onAttack();
                    actions.add(new ActionTriggerPreAttack());
                }
                resetAttackVariables();
            }

            if (mobTurn && battleBox.isDone()) {
                mobTurn = false;
                actions.clear();
                actions = mob.onAfterAttack(battleBox.getAttack());
                actions.add(new ActionPlayerTurn());
            }

            oldDidTarget = didTarget;

            String music = mob.getMusic();
            if (music != null) {
                if (!music.equals(mobMusic)) {
                    AudioHandler.stopSong(mobMusic);
                    AudioHandler.playSong(music, true);
                }
                mobMusic = music;
            } else {
                if (mobMusic != null) {
                    AudioHandler.stopSong(mobMusic);
                }
                mobMusic = null;
            }

            dialogBox.tick();
            chatBubble.tick();
            if (mobTurn) battleBox.tick();
        }
    }

    public void startMobTurn() {
        resetAttackVariables();
        selected = -1;
        freezeMenu = true;
        dialogBox.setText("");
        turn++;
        Attack a = mob.getNextAttack();
        battleBox.setAttack(a);
        battleBox.doResizeAnimation(575, 140, a.getWidth(), a.getHeight());
        mobTurn = true;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Board.self.getWidth(), Board.self.getHeight());
        if (!doingDeathAnimation) {
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
                g.drawImage(PlayerInfo.soulType.getImage(), selectedPoint.x + 8, selectedPoint.y + 14, null);
            }

        /*
        BufferedImage image = mob.get();
        int width = image.getWidth();
        //int height = image.getHeight();
        if (mob.shouldDoubleSize()) {
            width *= 2;
            height *= 2;
        }

        int centerX = Main.WIDTH / 2;
        int x = centerX - (width / 2);

        if (!dealtDamage) g.drawImage(mob.get(), x, mob.getYOffset(), width, height, null);
        */
            int centerX = Main.WIDTH / 2;
            int x = mob.getX();
            int mobY = battleBox.getBounds().y - mob.getYOffset();
            if (mobY > 251 - mob.getYOffset()) mobY = 251 - mob.getYOffset();

            if (!dealtDamage) {
                mob.draw(g, x, mobY, false);
            }

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

            g.setFont(Main.MENU);
            g.setColor(Color.WHITE);
            //g.drawString("Turn = " + turn, 20, 20);

            if (doingAttack) {
                g.drawImage(fightGUI, dialogBox.getX() + 15, dialogBox.getY() + 14, null);
                BufferedImage draw = !didTarget ? battlePointer.get(0, 0) : pointedSelected.getImage();
                g.drawImage(draw, battleTargeterX, dialogBox.getY() + 6, null); //TODO: animate
                if (midAttack && !damageAnim) {
                    if (!PlayerInfo.weapon.isAnimationDone()) {
                        PlayerInfo.weapon.drawEffect(centerX, battleBox.getBounds().y - mob.getYOffset(), g);
                    } else {
                        damageAnim = true;
                        //System.out.println("Animation done");
                    }
                } else if (dealtDamage) {
                    //TODO: bouncing damage numbers
                    int renderX = x;
                    if (healthLost > 0) {
                        if (timeSinceLastAdjust == null) {
                            damageShakeTargetX = x - adjustAmount;
                            timeSinceLastAdjust = System.currentTimeMillis();
                        }
                        if (timeSinceLastAdjust != null && System.currentTimeMillis() - timeSinceLastAdjust > 100) {
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

                    //BufferedImage mobImage = healthLost > 0 ? mob.getHitImage() : mob.get();
                    mob.draw(g, renderX, mobY, healthLost > 0);
                    //g.drawImage(mobImage, renderX, mob.getYOffset(), width, height, null);

                    int hBarWidth = 103, hBarHeight = 15;

                    if (mob.isBoss()) {
                        hBarWidth *= 1.50;
                    }

                    int hBarX = centerX - (hBarWidth / 2), hBarY = (battleBox.getBounds().y - mob.getYOffset()) - 25;

                    g.setColor(Color.black);
                    g.drawRect(hBarX, hBarY, hBarWidth, hBarHeight);

                    g.setColor(UnderColor.GRAY);
                    g.fillRect(hBarX + 1, hBarY + 1, hBarWidth - 2, hBarHeight - 2);

                    if (currentHPBarWidth == -1) currentHPBarWidth = hBarWidth - 2;
                    if (firstHPRecalculate) {
                        lastHPWidth = currentHPBarWidth;
                        firstHPRecalculate = false;
                        //System.out.println("RECALCULATE");
                    }

                    double fillAmount = (hBarWidth - 2.0) * mob.getHealthPercent();
                    double diff = lastHPWidth - fillAmount;
                    double rateOfchange = diff / 20.0; //TODO: turn this number into a variable for easy tuning

                    currentHPBarWidth -= rateOfchange;
                    if (currentHPBarWidth < fillAmount) currentHPBarWidth = fillAmount;

                    int renderSize = (int) Math.round(currentHPBarWidth);

                    g.setColor(UnderColor.GREEN);
                    g.fillRect(hBarX + 1, hBarY + 1, renderSize, hBarHeight - 2);

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
                }
            }
        } else {
            drawDeathAnimation(g);
        }
    }

    private boolean doingDeathAnimation = false;

    private Long deathAnimStart = null;
    private int deathX, deathY;
    private int animStep = 0;
    private BufferedImage soulSplit, gameOver;
    private SpriteSheet soulShardSheet;

    public static void startDeathAnimation(int dX, int dY) {
        Arena.self.deathX = dX;
        Arena.self.deathY = dY;
        AudioHandler.stopSong(Arena.self.mobMusic);
        Arena.self.soulSplit = ImageLoader.getImage("death/soul_break");
        Arena.self.gameOver = ImageLoader.getImage("death/game_over");
        Arena.self.soulShardSheet = new SpriteSheet(7, 8, 3, 1, "death/soul_shard");
        Arena.self.deathAnimStart = System.currentTimeMillis();
        Arena.self.doingDeathAnimation = true;
    }

    private List<Projectile> soulShards = new ArrayList<>();

    public void drawDeathAnimation(Graphics2D g) {
        long time = System.currentTimeMillis() - deathAnimStart;
        double secsTillReset = 16 - ((time - (3550 + 1250)) / 1000);
        if (secsTillReset <= 10) {
            if (secsTillReset <= -1) {
                AudioHandler.stopSong("GameOver");
                Board.add(new Overworld());
                Board.remove(this);
            } else {
                g.setFont(Main.DIALOGUE);
                g.setColor(Color.WHITE);
                g.drawString("Reset in: " + Math.round(secsTillReset), (Main.WIDTH / 2) - 100, 350);
            }
        }
        if (time > 3550) {
            float trans = ((time - 3550f) / 1250f);
            if (trans > 1f) trans = 1f;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, trans));
            g.drawImage(gameOver, (Main.WIDTH / 2) - (gameOver.getWidth() / 2), 30, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            if (animStep == 3) {
                AudioHandler.playSong("GameOver", false);
                animStep = 4;
            }
        }
        soulShards.forEach(Projectile::tick);
        for (Projectile p : soulShards) p.draw(g);
        if (time > 1800) {
            if (animStep == 2) {
                for (int i=0; i<6; i++) {
                    System.out.println("spawn shard");
                    Animation a = new Animation(4, soulShardSheet.get(0, 0), soulShardSheet.get(1, 0), soulShardSheet.get(2, 0), soulShardSheet.get(1, 0));
                    Projectile shard = new Projectile(deathX, deathY, a) {
                        private double xMomentum = Utils.randomNumber(3, 5);
                        private double xDir = Utils.randomNumber(0, 1) == 0 ? -1 : 1;
                        private double yMomentum = Utils.randomNumber(-5, 1);
                        private int ticksPerChange = 0;
                        @Override
                        public void tick() {
                            if (ticksPerChange == 8) {
                                yMomentum += 1;
                                if (yMomentum > 5) yMomentum = 5;
                                ticksPerChange = 0;
                            } else {
                                ticksPerChange++;
                            }
                            setX(getX() + (xMomentum * xDir));
                            setY(getY() + yMomentum);
                        }
                    };
                    soulShards.add(shard);
                }
                AudioHandler.playEffect("soul_shatter");
                animStep = 3;
            }
        } else if (time > 500) {
            g.drawImage(soulSplit, deathX - 2, deathY, null);
            if (animStep == 1) {
                AudioHandler.playEffect("soul_split");
                animStep = 2;
            }
        } else {
            g.drawImage(SoulType.NORMAL.getImage(), deathX, deathY, null);
            animStep = 1;
        }
    }

    private boolean firstHPRecalculate = true;
    private double lastHPWidth = -1;
    private double currentHPBarWidth = -1;

    public void drawPlayerInfo(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(Main.SQUISH_MENU);

        //TODO: all of this is roughly correct, but perfect it

        Point stats = new Point(33, 420);
        g.drawString(PlayerInfo.name + "  LV " + PlayerInfo.level, stats.x, stats.y);

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
        return buttons.get(0, selected == 0 ? 1 : 0);
    }

    public BufferedImage getAct() {
        return buttons.get(1, selected == 1 ? 1 : 0);
    }

    public BufferedImage getItem() {
        return buttons.get(0, selected == 2 ? 3 : 2);
    }

    public BufferedImage getMercy() {
        return buttons.get(1, selected == 3 ? 3 : 2);
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

    public static int getTurn() {
        return turn;
    }

    public static void setSoulType(SoulType t) {
        if (PlayerInfo.soulType != t) {
            //TODO: play sound effect
        }
        PlayerInfo.soulType = t;
    }

    public static Mob getMob() {
        return mob;
    }
}
