package ryan.shavell.main.logic.entity.battle.attacks.Volty;

import ryan.shavell.main.logic.entity.battle.attacks.Projectile;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.SpriteSheet;

public class Lightning extends Projectile {

    public Lightning(int x, int y) {
        //SpriteSheet s = new SpriteSheet(22, 22, 2, 1, "attacks/lightning");
        //Animation a = new Animation(3, s.get(0, 0), s.get(0, 1));
        super(x, y, new Animation(3, new SpriteSheet(22, 22, 2, 1, "attacks/lightning").getImageArray()));
    }
}
