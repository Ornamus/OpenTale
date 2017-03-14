package ryan.shavell.main.logic.battle.attacks.Asriel;

import ryan.shavell.main.logic.battle.attacks.Projectile;
import ryan.shavell.main.stuff.Utils;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ProjectileStar extends Projectile {

    private boolean spinLeft;

    public ProjectileStar(int x, int y, double angleOfMovement, String img) {
        super(x, y, img);
        moveAtAngle(angleOfMovement);
        spinLeft = Utils.randomNumber(0, 1) == 0;
    }

    public ProjectileStar(int x, int y, double angleOfMovement) {
        super(x, y, "attacks/star");
        moveAtAngle(angleOfMovement);
        spinLeft = Utils.randomNumber(0, 1) == 0;
    }

    private double drawAngle = 0;

    @Override
    public Shape getHitbox() {
        Shape s = super.getHitbox();
        AffineTransform transform = new AffineTransform();
        Rectangle bounds = s.getBounds();
        transform.rotate(Math.toRadians(drawAngle), bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
        s = transform.createTransformedShape(s);
        return s;
    }

    @Override
    public void draw(Graphics2D g) {
        frame = animation.getImage();
        Utils.drawRotated(frame, drawAngle, getXRound(), getYRound(), g);
        //g.drawImage(frame, getXRound(), getYRound(), null);
        if (spinLeft) drawAngle -= moveSpeed;
        else drawAngle += moveSpeed;
    }
}
