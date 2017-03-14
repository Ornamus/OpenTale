package ryan.shavell.main.logic.battle.attacks;

import ryan.shavell.main.core.Game;
import ryan.shavell.main.dialogue.actions.Action;
import ryan.shavell.main.dialogue.actions.ActionImageChange;
import ryan.shavell.main.dialogue.actions.ActionTalk;
import ryan.shavell.main.dialogue.actions.ActionWait;
import ryan.shavell.main.logic.battle.Arena;
import ryan.shavell.main.logic.battle.encounters.Boogie;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.AudioHandler;
import ryan.shavell.main.resources.SpriteSheet;

import java.util.Collections;
import java.util.List;

public class BoogieAttack extends Attack {

    final String voice = "boogie_text";
    static Projectile bullet = null;

    public BoogieAttack() {
        setTimeLength(-1);
    }

    @Override
    public void start() {
        super.start();
        final Attack attack = this;
        List<Action> actions = Game.getActionBuffer();
        Collections.addAll(actions,
                new Action(()->Arena.self.hideMenu = true),
                new ActionTalk("See that red thing down there? That's your SOUL. Your essence.", voice),
                new ActionTalk("It's not much now but if you gain LV it will be slightly more impressive.", voice),
                new ActionTalk("LV? Oh it's uh./nUh.../n.../n...LOVE?", voice),
                new ActionTalk("Do you maybe.../nWant some LOVE? I mean, if you really want some.", voice),
                new ActionTalk("Okay.../nHere it comes...", voice),
                new ActionImageChange(((Boogie)Arena.getMob()).sheet.get(1, 0)),
                new Action(()-> {
                    Projectile p = new Projectile(255, 175, "boogie/breath") { //250, 175
                        long spawn = System.currentTimeMillis();
                        double momentum = 1.5;
                        float trans = 1f;

                        @Override
                        public void tick() {
                            super.tick();
                            long timeElapsed = System.currentTimeMillis() - spawn;
                            setMoveSpeed(momentum);
                            momentum -= .05;
                            if (momentum < 0) {
                                momentum = 0;
                                trans -= .05;
                                if (trans < 0) trans = 0;
                                setTransparency(trans);
                            }
                            if (timeElapsed > 1750) {
                                setShouldDelete(true);
                                Collections.addAll(Game.getActionBuffer(), new ActionImageChange(((Boogie)Arena.getMob()).sheet.get(0, 0)),
                                        new Action(()-> {
                                            Animation a = new Animation(4, new SpriteSheet(12, 12, 2, 1, "attacks/seed").getImageArray());
                                            bullet = new Projectile(240, 130, a) {
                                                @Override
                                                public void onCollision() {
                                                    setShouldDelete(true);
                                                    Game.getActionBuffer().clear();
                                                    Collections.addAll(Game.getActionBuffer(),
                                                            new Action(()-> AudioHandler.stopSong("Boogie")),
                                                            new ActionImageChange(((Boogie)Arena.getMob()).sheet.get(2, 0)),
                                                            new ActionWait(0.75),
                                                            new ActionTalk("Huh./nThat did more than I expected.", voice),
                                                            new ActionImageChange(((Boogie)Arena.getMob()).sheet.get(0, 0)),
                                                            new ActionTalk("Okay.", voice),
                                                            new ActionTalk("I don't really need anything else from you so...", voice),
                                                            new ActionTalk("You can go now.", voice));
                                                }
                                            };
                                            bullet.setDamage(19);
                                            attack.spawnProjectile(bullet);
                                        }, ()->true),
                                        new ActionTalk("So this here is called.", voice),
                                        new ActionTalk("A bullet.", voice),
                                        new ActionTalk("It's just one, so it won't hurt that much.", voice),
                                        new Action(()-> bullet.moveAtAngle(18)),
                                        new ActionTalk("Dodge it if you want. Or not.", voice));
                            }
                        }
                    };
                    p.moveAtAngle(-45);
                    //p.setMoveSpeed(.5);
                    spawnProjectile(p);
                }, ()->true));
    }
}
