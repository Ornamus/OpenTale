package ryan.shavell.main.logic.overworld;

import ryan.shavell.main.core.Game;
import ryan.shavell.main.core.Main;
import ryan.shavell.main.dialogue.DialogBox;
import ryan.shavell.main.dialogue.actions.*;
import ryan.shavell.main.logic.InputTaker;
import ryan.shavell.main.logic.SoulType;
import ryan.shavell.main.logic.battle.Arena;
import ryan.shavell.main.logic.battle.Mob;
import ryan.shavell.main.logic.overworld.specifics.OverworldAsriel;
import ryan.shavell.main.logic.overworld.specifics.OverworldBoogie;
import ryan.shavell.main.render.Board;
import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.AudioHandler;
import ryan.shavell.main.resources.ImageLoader;
import ryan.shavell.main.stuff.Log;
import ryan.shavell.main.stuff.Utils;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Overworld implements Drawable, InputTaker {

    public static Overworld self;

    private static Map map = null;
    private OverworldPlayer player;
    private List<OverworldEntity> entities = new ArrayList<>();
    private static DialogBox dialogBox = new DialogBox(320);

    public List<Action> actions = new ArrayList<>();

    public Overworld() {
        map = new Map("ruins");

        player = new OverworldPlayer(160, 185); //200, 150
        //entities.add(new BasicRenderedThing(0, 0, "home"));
        //entities.add(new OverworldEntity(100, 100, new SpriteSheet(16, 28, 4, 1, "asriel_overworld").get(0,0)));
        //entities.add(new OverworldAsriel(100, 100));
        entities.add(new OverworldAsriel(150, 115));
        entities.add(player);

        //dialogBox.setPortrait(new Animation(1, ImageLoader.getImage("as_face")), 0);

        Game.setState(Game.State.OVERWORLD);

        self = this;
    }

    @Override
    public void tick() {
        if (map != null) map.tick();
        entities.forEach(OverworldEntity::tick);

        if (isUsingDialog())  dialogBox.tick();

        if (actions.size() > 0) {
            Action current = actions.get(0);
            if (!current.hasRun()) {
                current.run();
            }
            if (current.isDone()) {
                actions.remove(current);
            }
        }
    }

    double scale = 1;

    boolean atY = false, atX = false;

    @Override
    public void draw(Graphics2D g) {
        if (!doingEncounterAnim) {
            if (map != null) {
                scale = 2;
                g.scale(2.0, 2.0);
                map.draw(g);
            }
            for (OverworldEntity e : entities) {
                if (e.shouldDoubleSize() && scale == 1) {
                    g.scale(2.0, 2.0);
                    scale = 2;
                } else if (!e.shouldDoubleSize() && scale == 2) {
                    g.scale(0.5, 0.5);
                    scale = 1;
                }
                e.draw(g);
            }
            if (scale == 2) {
                g.scale(0.5, 0.5);
                scale = 1;
            }
            if (isUsingDialog()) dialogBox.draw(g);
        } else {
            encounterAnimation(g);
        }
    }

    @Override
    public void onKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_Z && !blockPlayerMovement() && !doingEncounterAnim) {
            int dir = player.getDirection();
            Rectangle interactionBox = null;
            Rectangle hitbox = player.getCollisionBox();

            int x = hitbox.x;
            int y = hitbox.y;
            int x2 = Utils.round(hitbox.x + hitbox.getWidth());
            int y2 = Utils.round(hitbox.y + hitbox.getHeight());

            //TODO: investigate abnormally tall left interaction box
            if (dir == 0) {
                interactionBox = new Rectangle(x, y - 5, x2, y);
            } else if (dir == 1) {
                interactionBox = new Rectangle(x - 5, y, x, y2);
            } else if (dir == 2) {
                interactionBox = new Rectangle(x2 + 5, y, x2, y2);
            } else if (dir == 3) {
                interactionBox = new Rectangle(x, y + 5, x2, y);
            }
            if (interactionBox != null) {
                for (OverworldEntity ent : entities) {
                    if (!(ent instanceof OverworldPlayer) && interactionBox.intersects(ent.getCollisionBox())) {
                        List<Action> interact = ent.onInteract();
                        if (!interact.isEmpty()) {
                            //Log.d("Interacted with " + ent.toString());
                            actions.clear();
                            actions = interact;
                            break;
                        }
                    }
                }
            }
        }

        if (isUsingDialog()) dialogBox.onKeyPress(e);

        for (OverworldEntity ent : entities) {
            if (ent instanceof InputTaker) {
                ((InputTaker)ent).onKeyPress(e);
            }
        }
    }

    @Override
    public void onKeyRelease(KeyEvent e) {
        for (OverworldEntity ent : entities) {
            if (ent instanceof InputTaker) {
                ((InputTaker)ent).onKeyRelease(e);
            }
        }
    }

    @Override
    public boolean shouldDoubleSize (){
        return false;
    }

    private boolean isUsingDialog() {
        return dialogBox.isOptions() || (dialogBox.getText() != null && !dialogBox.getText().equals(""));
    }

    public static boolean blockPlayerMovement() {
        return self.actions.size() > 0;
    }

    public static DialogBox getDialogBox() {
        return dialogBox;
    }

    public static List<Rectangle> getAllCollisions() {
        List<Rectangle> rects = new ArrayList<>();
        for (Rectangle2D.Double d : map.getCollisions()) {
            rects.add(d.getBounds());
        }
        for (OverworldEntity e : self.entities) {
            if (!(e instanceof OverworldPlayer)) {
                rects.add(e.getCollisionBox());
            }
        }
        return rects;
    }

    public static Map getMap() {
        return map;
    }

    //All variables and functions related to the encounter start animation

    double startX, startY, currX, currY, endX = 33 + 8, endY = 433 + 14;
    int clicks = 0;

    boolean doingEncounterAnim = false;
    boolean doingHeartMove = false;
    Long encounterStart = null;
    Mob encounterMob = null;
    int heartTicks = 3;

    public void startEncounter(Mob m) {
        encounterMob = m;
        encounterStart = System.currentTimeMillis();
        doingEncounterAnim = true;
    }

    public void encounterAnimation(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
        if (System.currentTimeMillis() - encounterStart > (150 * clicks)) {
            if (clicks < 3) {
                AudioHandler.playEffect("click");
                heartTicks = 3;
                clicks++;
            } else if (clicks == 3) {
                AudioHandler.playEffect("battle_start");
                clicks++;
                doingHeartMove = true;
            }
        }
        if (doingHeartMove) {
            double angle = Utils.getAngle(startX, startY, endX, endY);

            double rad = Math.toRadians(angle);

            double distance = Utils.distance(new Point(Utils.round(startX), Utils.round(startY)), new Point(Utils.round(endX), Utils.round(endY)));
            double speed = distance / 25;

            double newX = currX + (Math.sin(rad) * speed);
            double newY = currY + (Math.cos(rad) * speed);

            if (atX) newX = currX;
            if (atY) newY = currY;

            if (atX && atY) {
                Board.add(new Arena(encounterMob));
                Board.remove(this);
            }

            if (Utils.getSign(endX - currX) != Utils.getSign(endX - newX)) {
                newX = endX;
                atX = true;
            }
            if (Utils.getSign(endY - currY) != Utils.getSign(endY - newY)) {
                newY = endY;
                atY = true;
            }

            currX = newX;
            currY = newY;

            g.drawImage(SoulType.NORMAL.getImage(), (int) Math.round(currX), (int) Math.round(currY), null);
        } else if (heartTicks > 0) {
            //BufferedImage soul = PlayerInfo.soulType.getImage();
            startX = ((player.getX() * 2) + (player.getWidth() / 2));// - (soul.getWidth() / 2);
            startY = ((player.getY() * 2) + (player.getHeight() / 2)) + 10;// - (soul.getWidth() / 2);
            currX = startX;
            currY = startY;
            g.drawImage(SoulType.NORMAL.getImage(), (int) Math.round(currX), (int) Math.round(currY), null);
            heartTicks --;
            if (heartTicks < 0) heartTicks = 0;
        }
    }
}


