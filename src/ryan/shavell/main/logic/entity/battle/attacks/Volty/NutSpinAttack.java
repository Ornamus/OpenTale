package ryan.shavell.main.logic.entity.battle.attacks.Volty;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.logic.entity.battle.Arena;
import ryan.shavell.main.logic.entity.battle.attacks.Attack;
import ryan.shavell.main.logic.entity.battle.attacks.Projectile;
import ryan.shavell.main.stuff.Utils;

import java.awt.*;

public class NutSpinAttack extends Attack {

    public NutSpinAttack() {
        //super(135, 200);
        setTimeLength(7);
    }
    @Override
    public void start() {
        super.start();
        for (int x=0; x<2; x++) {
            for (int i = 0; i < 4; i++) {
                int spawnX = (Main.WIDTH / 2) - (getWidth() / 2);
                if (x == 1) spawnX += getWidth() * 0.85;

                final int whichType = x;
                spawnProjectile(new NutProjectile(spawnX, 220 + (i * 70)) {

                    int whateverangle = 0;
                    int increment = 3;

                    @Override
                    public void tick() {
                        super.tick();
                        setMoveSpeed(3);
                        double diff = y - oldY;
                        y += diff * .25;
                        if (whichType == 0) whateverangle += increment;
                        else whateverangle -= increment;
                        moveAtAngle(whateverangle);
                    }
                });
            }
        }
    }

    private Long timeSinceSpawn = null;

    @Override
    public void tick() {
        super.tick();
        if (timeSinceSpawn == null || System.currentTimeMillis() - timeSinceSpawn >= 2000) {
            Point soul = Arena.getBattleBox().getSOUL();
            int spawnX = 500, spawnY = 500;

            double angle = Utils.getAngle(spawnX, spawnY, soul.x, soul.y);
            angle = -angle;
            angle -= 90;

            Projectile p = new NutProjectile(spawnX, spawnY);
            p.setMoveSpeed(4);
            p.moveAtAngle(angle);
            spawnProjectile(p);

            timeSinceSpawn = System.currentTimeMillis();
        }
    }
}
