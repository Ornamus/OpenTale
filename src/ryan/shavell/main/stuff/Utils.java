package ryan.shavell.main.stuff;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Utils {

    public static Random random = new Random();

    public static int randomNumber(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }

    //TODO: make these angles make more sense
    public static double getAngle(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(y1 - y2, x1 - x2));
        if(angle < 0){
            angle += 360;
        }
        return angle;
    }

    public static int round(double d) {
        return (int) Math.round(d);
    }

    public static int getSign(double i) {
        if (i >= 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt( Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2) );
    }

    public static BufferedImage rotate(BufferedImage i, double angle)
    {
        double rotationRequired = Math.toRadians(angle);
        double locationX = i.getWidth() / 2;
        double locationY = i.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage rotated = op.filter(i, null);
        return rotated;
        //g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY, null);
    }
}
