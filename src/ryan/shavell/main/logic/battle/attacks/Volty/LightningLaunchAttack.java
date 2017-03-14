package ryan.shavell.main.logic.battle.attacks.Volty;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.logic.battle.Arena;
import ryan.shavell.main.logic.battle.attacks.Attack;
import ryan.shavell.main.logic.battle.attacks.Projectile;
import ryan.shavell.main.stuff.Utils;

import java.awt.*;

public class LightningLaunchAttack extends Attack {

    public LightningLaunchAttack() {
        super(300, 140);
        setTimeLength(7);
    }

    private Long timeSinceSpawn = null;

    @Override
    public void start() {
        super.start();

        for (int i=0; i<3; i++) {
            Projectile p = new NutProjectile(200 + (i * 110), 220) {

                boolean up = false;

                @Override
                public void tick() {
                    super.tick();
                    if (getY() > 380) {
                        up = true;
                    } else if (getY() < 220){
                        up = false;
                    }
                    //System.out.println("up: " + up);
                    if (up) y -= moveSpeed;
                    else y += moveSpeed;
                }
            };
            p.setMoveSpeed(3);
            spawnProjectile(p);
        }
        timeSinceSpawn = System.currentTimeMillis();
    }

    @Override
    public void tick() {
        super.tick();
        if (timeSinceSpawn == null || System.currentTimeMillis() - timeSinceSpawn >= 500) {
            Point soul = Arena.getBattleBox().getSOUL();

            int spawnX = (Main.WIDTH / 2) - 8, spawnY = 170;
            spawnX += Utils.randomNumber(-70, 70);

            double angle = Utils.getAngle(spawnX, spawnY, soul.x, soul.y);
            angle = -angle;
            angle -= 90;

            Projectile p = new Lightning(spawnX, spawnY);
            p.setMoveSpeed(7);
            p.moveAtAngle(angle);
            spawnProjectile(p);

            timeSinceSpawn = System.currentTimeMillis();
        }
    }
}
