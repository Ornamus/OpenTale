package ryan.shavell.main.logic.battle.attacks.Asriel;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.logic.battle.Arena;
import ryan.shavell.main.logic.battle.attacks.Attack;
import ryan.shavell.main.logic.battle.attacks.Projectile;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.AudioHandler;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Utils;
import java.awt.*;

public class StarBlazing extends Attack {

    //TODO: Big star comes from the left and keeps going until it almost crosses the entire battlebox
    private boolean simple;

    public StarBlazing(boolean simple) {
        super(255, 140);
        this.simple = simple;
        if (simple) {
            setTimeLength(7);
        } else {
            setTimeLength(12);
        }
    }

    @Override
    public void start() {
        super.start();
        if (simple) {
            spawnStar(0, 30, 50);
            doDelayed(1.5, () -> {
                spawnStar(600, 40, -50);
                doDelayed(1, () -> {
                    spawnStar(100, -20, 40);
                    doDelayed(2, () -> {
                        Projectile p = new ProjectileStar(-30, 250, 90, "attacks/star_meh_big") {
                            @Override
                            public void draw(Graphics2D g) {
                                super.draw(g);
                                //g.setColor(Color.RED);
                                //g.draw(getHitbox());
                            }
                        };
                        p.setHitboxNiceness(10);
                        p.setMoveSpeed(3);
                        //p.moveAtAngle(90);
                        AudioHandler.playEffect("star_rain");
                        AudioHandler.playEffect("falling");
                        AudioHandler.playEffect("blades_summon");
                        spawnProjectile(p);
                    });
                });
            });
        } else {
            double turn = Arena.getTurn() * 1.0;
            double loopSpeed = 1.8;
            if (turn > 3) {
                loopSpeed -= (turn / 3);
                if (loopSpeed < .8) loopSpeed = .8;
            }
            doOnLoop(loopSpeed, 0, ()-> {
                Rectangle r = Arena.getBattleBox().getBounds();
                int x = Utils.randomNumber(-10, Main.WIDTH);
                int y = 10;
                spawnStar(x, y, Utils.getAngle(x, y, r.getCenterX(), r.getCenterY()));
            });
        }
    }

    private void spawnStar(int x, int y, double angle) {
        final Attack attack = this;
        Projectile p = new ProjectileStar(x, y, angle, "attacks/star_meh") {
            @Override
            public void tick() {
                super.tick();
                Rectangle bounds = Arena.getBattleBox().getBounds();
                if (getHitbox().intersects(bounds)) {
                    setShouldDelete(true);
                    AudioHandler.playEffect("explosion");
                    //List<Projectile> shards = new ArrayList();
                    for (int i=0; i<6; i++) {
                        double x = getX() + (getWidth() / 2);
                        double y = getY() + (getHeight() / 2);
                        SpriteSheet sheet = new SpriteSheet(7, 8, 3, 1, "attacks/star_shard");
                        Animation a = new Animation(4, sheet.get(0, 0), sheet.get(1, 0), sheet.get(2, 0), sheet.get(1, 0));

                        spawnShard(x, y, a);
                    }
                }
            }

            @Override
            public void draw(Graphics2D g) {
                super.draw(g);
                /*
                g.setColor(Color.RED);
                Shape s = getHitbox();
                g.draw(s);
                */
            }
        };
        p.setHitboxNiceness(6);
        p.setMoveSpeed(5);
        spawnProjectile(p);
        AudioHandler.playEffect("star_rain");
    }

    private void spawnShard(double x, double y, Animation a) {
        final Attack attack = this;
        Projectile shard = new Projectile(x, y, a) {
            private double xMomentum = Utils.randomNumber(0, 300) / 100.0;
            private double xDir = Utils.randomNumber(0, 1) == 0 ? -1 : 1;
            private double yMomentum = Utils.randomNumber(-250, 100) / 100.0;
            private int ticksPerChange = 0;
            @Override
            public void tick() {
                super.tick();
                if (ticksPerChange == 6) {
                    yMomentum += 1;
                    //if (yMomentum > 4) yMomentum = 4;
                    ticksPerChange = 0;
                } else {
                    ticksPerChange++;
                }
                setX(getX() + (xMomentum * xDir));
                setY(getY() + yMomentum);
            }
        };
        attack.spawnProjectile(shard);
    }
}
