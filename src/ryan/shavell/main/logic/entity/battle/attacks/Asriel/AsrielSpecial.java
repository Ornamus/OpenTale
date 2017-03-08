package ryan.shavell.main.logic.entity.battle.attacks.Asriel;

import ryan.shavell.main.logic.entity.battle.Arena;
import ryan.shavell.main.logic.entity.battle.attacks.Attack;
import ryan.shavell.main.logic.entity.battle.attacks.Projectile;
import ryan.shavell.main.resources.ImageLoader;
import ryan.shavell.main.stuff.Utils;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.lang.Math.cos;
import static java.lang.StrictMath.sin;

public class AsrielSpecial extends Attack {

    public AsrielSpecial() {
        super(225, 180);
        setTimeLength(22);
        doOnLoop(1.6, 0, spawnCircle);
    }

    private Runnable spawnCircle = new Runnable(){
        @Override
        public void run() {
            Rectangle bounds = Arena.getBattleBox().getBounds();
            final double a = bounds.getX() + (bounds.getWidth() / 2), b = bounds.getY() + (bounds.getHeight() / 2);
            final int r = 350;
            int angle = 0;

            int starsToSpawn = 100;
            int starSkipSize = Utils.round(starsToSpawn * .15);

            int skipStart = Utils.randomNumber(1, starsToSpawn - starSkipSize);
            //Log.d("StarsToSkip: " + starSkipSize + ", SkipStart: " + skipStart);

            List<Projectile> stars = new ArrayList<>();
            while (angle < 360) {
                double x = a + cos(angle)*r;//a + r * sin(angle);
                double y = b + sin(angle)*r;//b + r * cos(angle);
                double starX = x - 7.5;
                double starY = y - 7;

                Projectile p = new Projectile(Utils.round(starX), Utils.round(starY), ImageLoader.getImage("attacks/star_tiny")) {
                    @Override
                    public void tick() {
                        super.tick();
                        if (Math.abs(getX() - a) < 5 && Math.abs(getY() - b) < 5) {
                            setShouldDelete(true);
                        }
                    }
                };
                p.setMoveSpeed(2.25);
                double starAngle = Utils.getAngle(starX, starY, a, b);
                starAngle = -starAngle;
                starAngle -= 90;
                p.moveAtAngle(starAngle);
                stars.add(p);
                angle += (360 / starsToSpawn);
            }

            Collections.sort(stars, (o1, o2) -> Utils.round(o1.getAngle() - o2.getAngle()));
            int currentStar = 1;
            for (Projectile p : stars) {
                if (currentStar < skipStart || currentStar > skipStart + starSkipSize) {
                    spawnProjectile(p);
                }
                currentStar++;
            }
        }
    };
}
