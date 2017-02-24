package ryan.shavell.main.logic.entity.battle.attacks;

import ryan.shavell.main.stuff.Utils;

import java.awt.*;

public class ProjectileStar extends Projectile {

    private final int hitbox_niceness = 1;
    private boolean spinLeft;

    public ProjectileStar(int x, int y, double angleOfMovement) {
        super(x, y, angleOfMovement, "attacks/star");
        spinLeft = Utils.randomNumber(0, 1) == 0;
    }

    private double drawAngle = 0;

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(Utils.rotate(image, drawAngle), getXRound(), getYRound(), null);
        if (spinLeft) drawAngle -= moveSpeed;
        else drawAngle += moveSpeed;
    }
}
