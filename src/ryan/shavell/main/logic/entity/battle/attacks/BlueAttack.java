package ryan.shavell.main.logic.entity.battle.attacks;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.logic.SoulType;
import ryan.shavell.main.logic.entity.battle.Arena;
import ryan.shavell.main.stuff.Utils;

public class BlueAttack extends Attack {

    private Long timeOfLastStar = null;

    public BlueAttack() {
        super(300, 140);
        setTimeLength(11);
        setNeededSoulType(SoulType.BLUE);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        if (timeOfLastStar == null || System.currentTimeMillis() - timeOfLastStar >= 800) {
            Projectile p = new ProjectileStar(Main.WIDTH - 200, 360, 270);
            p.setMoveSpeed(3);
            spawnProjectile(p);
            if (Utils.randomNumber(0, 3) == 0) {
                p = new ProjectileStar(Main.WIDTH - 200, 345, 270);
                p.setMoveSpeed(3);
                spawnProjectile(p);
            }
            timeOfLastStar = System.currentTimeMillis();
        }
    }
}
