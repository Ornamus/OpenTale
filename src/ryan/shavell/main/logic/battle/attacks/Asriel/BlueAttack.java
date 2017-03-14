package ryan.shavell.main.logic.battle.attacks.Asriel;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.logic.SoulType;
import ryan.shavell.main.logic.battle.Arena;
import ryan.shavell.main.logic.battle.attacks.Attack;
import ryan.shavell.main.logic.battle.attacks.Projectile;
import ryan.shavell.main.stuff.Utils;

public class BlueAttack extends Attack {

    public BlueAttack() {
        super(300, 140);
        setTimeLength(11);
        setNeededSoulType(SoulType.BLUE);
        doOnLoop(.8, ()-> {
            Projectile p = new ProjectileStar(Main.WIDTH - 200, 360, 270);
            int speed = 3 + (Arena.getTurn() >= 3 ? Arena.getTurn() / 4 : 0);
            p.setMoveSpeed(speed);
            spawnProjectile(p);
            if (Utils.randomNumber(0, 3) == 0) {
                p = new ProjectileStar(Main.WIDTH - 200, 345, 270);
                p.setMoveSpeed(speed + (Arena.getTurn() >= 6 ? Utils.randomNumber(-2, 1) : 0));
                spawnProjectile(p);
            }
        });
    }
}
