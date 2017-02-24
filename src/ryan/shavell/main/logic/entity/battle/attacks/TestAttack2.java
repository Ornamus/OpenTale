package ryan.shavell.main.logic.entity.battle.attacks;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.stuff.Utils;

public class TestAttack2 extends Attack {

    private Long timeOfLastStar = null;

    public TestAttack2() {
        super(80, 140);
        setTimeLength(8);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        if (timeOfLastStar == null || System.currentTimeMillis() - timeOfLastStar >= 300) {
            int angleVary = 30;
            Projectile p1 = new ProjectileStar(0, 400 - Utils.randomNumber(0, 200), 90 + Utils.randomNumber(-angleVary, angleVary));
            Projectile p2 = new ProjectileStar(Main.WIDTH - 16, 400 - Utils.randomNumber(0, 200), 270 + Utils.randomNumber(-angleVary, angleVary));
            p1.setMoveSpeed(7);
            p2.setMoveSpeed(7);
            spawnProjectiles(p1, p2);
            timeOfLastStar = System.currentTimeMillis();
        }
    }
}