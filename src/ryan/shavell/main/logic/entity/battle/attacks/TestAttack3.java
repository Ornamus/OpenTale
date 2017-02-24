package ryan.shavell.main.logic.entity.battle.attacks;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.logic.entity.battle.Arena;
import ryan.shavell.main.stuff.Utils;
import java.awt.*;

public class TestAttack3 extends Attack {

    private Long timeOfLastStar = null;

    public TestAttack3() {
        super(165, 50);
        setTimeLength(8);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        if (timeOfLastStar == null || System.currentTimeMillis() - timeOfLastStar >= 400) {
            Point soul = Arena.getBattleBox().getSOUL();
            double angle = Utils.getAngle(Main.WIDTH / 2, 90, soul.x, soul.y);
            angle = -angle;
            angle -= 90;
            int angleVary = 0;
            Projectile p1 = new ProjectileStar(Main.WIDTH / 2, 90, angle + Utils.randomNumber(-angleVary, angleVary));
            p1.setMoveSpeed(6);
            spawnProjectiles(p1);
            timeOfLastStar = System.currentTimeMillis();
        }
    }
}