package ryan.shavell.main.logic.entity.battle.attacks.Asriel;

import ryan.shavell.main.logic.entity.battle.attacks.Attack;
import ryan.shavell.main.stuff.Utils;

//Lazy random stars
public class TestAttack extends Attack {

    public TestAttack() {
        super();
        setTimeLength(5);
    }

    @Override
    public void start() {
        super.start();
        for (int i=0; i<10; i++) {
            spawnProjectile(new ProjectileStar(150, 400 - Utils.randomNumber(0, 200), 90 + Utils.randomNumber(-45, 45)));
        }
        /*
        spawnProjectile(new Projectile(200, 200, 0, "star"));
        spawnProjectile(new Projectile(200, 300, 90, "star"));
        spawnProjectile(new Projectile(200, 100, 45, "star"));
        */
    }
}