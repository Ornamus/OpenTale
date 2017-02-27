package ryan.shavell.main.logic.entity.battle.attacks;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Utils;

import java.awt.image.BufferedImage;

/**
 * Created by Ryan Shavell on 2/24/2017.
 */
public class FireRain extends Attack {

    private Long timeOfLastFire = null;
    private SpriteSheet fire;

    public FireRain() {
        super(200, 140);
        setTimeLength(8);
        fire = new SpriteSheet(13, 14, 2, 1, "attacks/fireball");
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        if (timeOfLastFire == null || System.currentTimeMillis() - timeOfLastFire >= 200) {
            int moveSpeed = Utils.randomNumber(2, 4);
            int animSpeed = 0;
            if (moveSpeed == 4) animSpeed = 2;
            else if (moveSpeed == 3) animSpeed = 3;
            else if (moveSpeed == 2) animSpeed = 4;
            Animation a = new Animation(animSpeed, fire.getImage(0, 0), fire.getImage(1, 0));
            Projectile p = new Projectile(((Main.WIDTH / 2) - (getWidth() / 2)) + Utils.randomNumber(0, getWidth() - 20) + 10, 250, a);
            p.moveAtAngle(0);
            p.setMoveSpeed(moveSpeed);
            spawnProjectiles(p);
            timeOfLastFire = System.currentTimeMillis();
        }
    }
}
