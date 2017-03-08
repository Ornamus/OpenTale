package ryan.shavell.main.logic.entity.overworld;

import ryan.shavell.main.core.Game;
import ryan.shavell.main.core.Main;
import ryan.shavell.main.dialogue.DialogBox;
import ryan.shavell.main.dialogue.actions.*;
import ryan.shavell.main.logic.InputTaker;
import ryan.shavell.main.logic.SoulType;
import ryan.shavell.main.logic.entity.battle.Arena;
import ryan.shavell.main.logic.entity.battle.Mob;
import ryan.shavell.main.logic.entity.battle.StoryshiftAsriel;
import ryan.shavell.main.render.BasicRenderedThing;
import ryan.shavell.main.render.Board;
import ryan.shavell.main.render.Drawable;
import ryan.shavell.main.resources.AudioHandler;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Utils;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Overworld implements Drawable, InputTaker {

    public static Overworld self;

    private OverworldPlayer player;
    private List<OverworldEntity> entities = new ArrayList<>();
    private static DialogBox dialogBox = new DialogBox(320);

    public List<Action> actions = new ArrayList<>();

    public Overworld() {
        player = new OverworldPlayer(200, 150);
        entities.add(new BasicRenderedThing(0, 0, "home"));
        entities.add(new OverworldEntity(100, 100, new SpriteSheet(16, 28, 4, 1, "asriel_overworld").get(0,0)));
        entities.add(player);

        Game.setState(Game.State.OVERWORLD);

        self = this;
    }

    @Override
    public void tick() {
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
            if (isUsingDialog())  dialogBox.draw(g);
        } else {
            encounterAnimation(g);
        }
    }

    boolean did = false;

    @Override
    public void onKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_Z && !did) {

            String voice = "asriel_text";
            actions.add(new ActionDialog("* Hey there, newcomer! What's up?", voice));

            List<Action> no = new ArrayList<>();
            List<Action> yes = new ArrayList<>();

            no.add(new ActionDialog("* Alright! Cool! Talk to you later, then.", voice));
            no.add(new ActionCrash());

            yes.add(new ActionDialog("* Alright, yeah, sure!", voice));
            yes.add(new ActionDialog("* Just don't forget this is just a demo, okay?", voice));
            yes.add(new ActionDialog("* Ahem...", voice));

            yes.add(new ActionDialog("* So you finally came.", voice));
            yes.add(new ActionDialog("* At last. Time to show what we are really made of.", voice));
            yes.add(new ActionDialog("* God against mortal.", voice));
            yes.add(new ActionDialog("* Monster against human.", voice));
            yes.add(new ActionDialog("* That look in your eyes...", voice));
            yes.add(new ActionDialog("* That's the same look Chara gives when we play.", voice));
            yes.add(new ActionDialog("* Heh heh heh.", voice));
            yes.add(new ActionDialog("* You just won't give up no matter what, will you?", voice));
            yes.add(new ActionDialog("* Good, good. We'll have a great time.", voice));
            yes.add(new ActionDialog("* But enough stalling.", voice));
            yes.add(new ActionDialog("* Prepare yourself human!", voice));
            yes.add(new ActionDialog("* Let me show you my true power!", voice));
            yes.add(new ActionDialog("* Here's [color(187,102,187)Asriel Dreemurr]!", voice));
            yes.add(new ActionDialog("* The future Captain of the Royal Guard!", voice));
            yes.add(new ActionDialog("* And protector of all monsters!!", voice));

            yes.add(new ActionStartEncounter(new StoryshiftAsriel()));

            actions.add(new ActionDialogOption(new List[]{yes, no}, "Lets fight.", "Nothing."));

            did = true;
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
        return dialogBox.getText() == null || (dialogBox.getText() != null && !dialogBox.getText().equals(""));
    }

    public static boolean blockPlayerMovement() {
        return self.actions.size() > 0;
    }

    public static DialogBox getDialogBox() {
        return dialogBox;
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
            angle = -angle;
            angle -= 90;

            //System.out.println("ANGLE: " + angle);

            double rad = Math.toRadians(angle);

            //currX += Math.sin(rad) * 4.0;
            //currY += Math.cos(rad) * 4.0;

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
            //TODO: render on player's center
            startX = player.getX() * 2;
            startY = player.getY() * 2;
            currX = startX;
            currY = startY;
            g.drawImage(SoulType.NORMAL.getImage(), (int) Math.round(currX), (int) Math.round(currY), null);
            heartTicks --;
            if (heartTicks < 0) heartTicks = 0;
        }
    }
}


