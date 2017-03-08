package ryan.shavell.main.logic.entity.battle.attacks.Asriel;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.logic.entity.battle.attacks.Attack;
import ryan.shavell.main.logic.entity.battle.attacks.Projectile;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Utils;

public class FireRain extends Attack {

    private SpriteSheet fire;

    public FireRain() {
        super(200, 140);
        setTimeLength(8);
        fire = new SpriteSheet(13, 14, 2, 1, "attacks/fireball");
        doOnLoop(.2, ()-> {
            int moveSpeed = Utils.randomNumber(2, 4);
            int animSpeed = 0;
            if (moveSpeed == 4) animSpeed = 2;
            else if (moveSpeed == 3) animSpeed = 3;
            else if (moveSpeed == 2) animSpeed = 4;
            Animation a = new Animation(animSpeed, fire.get(0, 0), fire.get(1, 0));
            Projectile p = new Projectile(((Main.WIDTH / 2) - (getWidth() / 2)) + Utils.randomNumber(0, getWidth() - 20) + 10, 250, a);
            p.moveAtAngle(0);
            p.setMoveSpeed(moveSpeed);
            spawnProjectiles(p);
        });
    }
}
