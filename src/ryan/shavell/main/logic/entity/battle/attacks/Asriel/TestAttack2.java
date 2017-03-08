package ryan.shavell.main.logic.entity.battle.attacks.Asriel;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.logic.entity.battle.Arena;
import ryan.shavell.main.logic.entity.battle.attacks.Asriel.ProjectileStar;
import ryan.shavell.main.logic.entity.battle.attacks.Attack;
import ryan.shavell.main.logic.entity.battle.attacks.Projectile;
import ryan.shavell.main.stuff.Utils;

//STAR BLAZING
public class TestAttack2 extends Attack {

    public TestAttack2() {
        super(80, 140);
        setTimeLength(8);
        doOnLoop(.3, 0, () -> {
            int angleVary = 30;
            Projectile p1 = new ProjectileStar(0, 400 - Utils.randomNumber(0, 200), 90 + Utils.randomNumber(-angleVary, angleVary));
            Projectile p2 = new ProjectileStar(Main.WIDTH - 16, 400 - Utils.randomNumber(0, 200), 270 + Utils.randomNumber(-angleVary, angleVary));
            int speed = 7 + (Arena.getTurn() >= 3 ? Arena.getTurn() / 4 : 0);
            p1.setMoveSpeed(speed);
            p2.setMoveSpeed(speed);
            spawnProjectiles(p1, p2);
        });
    }
}