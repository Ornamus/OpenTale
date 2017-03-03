package ryan.shavell.main.logic.entity.battle.attacks;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.logic.entity.battle.Arena;
import ryan.shavell.main.stuff.Utils;
import java.awt.*;

//Shooting at the SOUL from Asriel's hand
public class TestAttack3 extends Attack {

    private Long timeOfLastStar = null;

    public TestAttack3() {
        super(135, 80);
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

            int spawnX = (Main.WIDTH / 2) + 15;
            int spawnY = 130;

            double angle = Utils.getAngle(spawnX, spawnY, soul.x, soul.y);
            angle = -angle;
            angle -= 90;
            int angleVary = 0;
            Projectile p1 = new ProjectileStar(spawnX, spawnY, angle + Utils.randomNumber(-angleVary, angleVary));

            int speed = 6 + (Arena.getTurn() >= 3 ? Arena.getTurn() / 4 : 0);
            p1.setMoveSpeed(speed);
            spawnProjectiles(p1);
            timeOfLastStar = System.currentTimeMillis();
        }
    }
}