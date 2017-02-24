package ryan.shavell.main.logic.entity.battle.attacks;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Utils;

import java.awt.image.BufferedImage;

/**
 * Created by Ryan Shavell on 2/24/2017.
 */
public class FireRain extends Attack {

    private Long timeOfLastFire = null;
    private BufferedImage fire;

    public FireRain() {
        super(200, 140);
        setTimeLength(8);
        fire = new SpriteSheet(13, 14, 2, 1, "attacks/fireball").getImage(0, 0);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        if (timeOfLastFire == null || System.currentTimeMillis() - timeOfLastFire >= 200) {
            Projectile p = new Projectile(((Main.WIDTH / 2) - (getWidth() / 2)) + Utils.randomNumber(0, getWidth() - 20) + 10, 250, 0, fire);
            p.setMoveSpeed(Utils.randomNumber(2, 4));
            spawnProjectiles(p);
            timeOfLastFire = System.currentTimeMillis();
        }
    }
}
