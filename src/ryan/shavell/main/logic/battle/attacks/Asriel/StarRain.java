package ryan.shavell.main.logic.battle.attacks.Asriel;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.logic.battle.Arena;
import ryan.shavell.main.logic.battle.attacks.Attack;
import ryan.shavell.main.logic.battle.attacks.Projectile;
import ryan.shavell.main.stuff.Utils;
import java.awt.*;

public class StarRain extends Attack {

    public StarRain() {
        super(335, 125);
    }

    @Override
    public void start() {
        super.start();
        timeSinceSpawn = System.currentTimeMillis() - 50;
        bounds = Arena.getBattleBox().getBounds();
        spawnX = bounds.x + (bounds.width / 2);
    }

    private long timeSinceSpawn;
    private int spawnX;
    private Rectangle bounds;
    private boolean right = true;

    @Override
    public void tick() {
        super.tick();
        int turn = Arena.getTurn();
        if (System.currentTimeMillis() - timeSinceSpawn >= 65) {
            int changeAmt = turn * 2;
            if (changeAmt > 25) changeAmt = 25;
            int chanceOfChange = Utils.round(50 - (turn > 3 ? changeAmt : 0));
            if (Utils.randomNumber(0, chanceOfChange) == 0) right = !right;
            if (spawnX > bounds.x + bounds.width - (turn > 4 ? 5 : 0)) {
                right = false;
            } else if (spawnX < bounds.x) {
                right = true;
            }
            spawnX += (turn > 4 ? 8 : 5) * (right ? 1 : -1);
            for (int i=0; i<2; i++) {
                int pX = (spawnX + (40 * (i == 0 ? 1 : -1)));
                Projectile p = new ProjectileStar(pX, Arena.getBattleBox().getBounds().y, 0, "attacks/star_tiny") {
                    @Override
                    public void tick() {
                        super.tick();
                        if (getY() > bounds.y + bounds.height - 17) {
                            setShouldDelete(true);
                        }
                    }
                };
                p.setMoveSpeed(4);
                if (!(p.getX() < bounds.x || p.getX() > bounds.x + bounds.width)) {
                    spawnProjectile(p);
                }
            }
            timeSinceSpawn = System.currentTimeMillis();
        }
        if (Utils.randomNumber(0, 0) == 0) {
            int x;
            while (!((x=Utils.randomNumber(bounds.x, bounds.x + bounds.width)) > spawnX + 50 || x < spawnX - 50));
            Projectile p = new ProjectileStar(x, Arena.getBattleBox().getBounds().y, 0, "attacks/star_tiny") {
                @Override
                public void tick() {
                    super.tick();
                    if (getY() > Main.HEIGHT) {
                        setShouldDelete(true);
                    }
                    if (getX() > spawnX - 50 && getX() < spawnX + 50 && bounds.contains(getX(), getY())) {
                        setShouldDelete(true);
                    }
                }
            };
            if (turn > 4) {
                p.setMoveSpeed(Utils.randomNumber(5, 7));
            } else {
                p.setMoveSpeed(Utils.randomNumber(3, 5));
            }
            spawnProjectile(p);
        }
    }
}
