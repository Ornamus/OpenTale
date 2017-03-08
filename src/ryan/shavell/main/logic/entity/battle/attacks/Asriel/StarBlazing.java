package ryan.shavell.main.logic.entity.battle.attacks.Asriel;

import ryan.shavell.main.logic.entity.battle.Arena;
import ryan.shavell.main.logic.entity.battle.attacks.Attack;
import ryan.shavell.main.logic.entity.battle.attacks.Projectile;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Utils;
import java.awt.*;

public class StarBlazing extends Attack {

    public StarBlazing() {
        super(255, 140);
        setTimeLength(25);
    }

    @Override
    public void start() {
        super.start();
        final Attack attack = this;
        Projectile p = new ProjectileStar(100, 50, 45, "attacks/star_meh") {
            @Override
            public void tick() {
                super.tick();
                Rectangle bounds = Arena.getBattleBox().getBounds();
                if (bounds.intersects(getHitbox())) {
                    setShouldDelete(true);
                    //List<Projectile> shards = new ArrayList();
                    for (int i=0; i<8; i++) {
                        double x = getX() + (getWidth() / 2);
                        double y = getY() + (getHeight() / 2);
                        SpriteSheet sheet = new SpriteSheet(7, 8, 3, 1, "attacks/star_shard");
                        Animation a = new Animation(4, sheet.get(0, 0), sheet.get(1, 0), sheet.get(2, 0), sheet.get(1, 0));

                        Projectile shard = new Projectile(x, y, a) {
                            private double xMomentum = Utils.randomNumber(0, 300) / 100.0;
                            private double xDir = Utils.randomNumber(0, 1) == 0 ? -1 : 1;
                            private double yMomentum = Utils.randomNumber(-100, 100) / 100.0;
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
            }

            @Override
            public void draw(Graphics2D g) {
                super.draw(g);
                //g.setColor(Color.RED);
                //Rectangle box = getHitbox();
                //g.drawRect(box.x, box.y, box.width, box.height);
            }
        };
        p.moveAtAngle(45);
        p.setMoveSpeed(3);
        spawnProjectile(p);
    }

}
