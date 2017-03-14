package ryan.shavell.main.stuff;

import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    public static Random random = new Random();

    public static int randomNumber(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }

    public static double getAngle(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(y1 - y2, x1 - x2));
        /*if(angle < 0){
            angle += 360;
        }*/
        angle = (-angle) - 90;
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

    /**
     * Gets the borders of a Shape.
     *
     * @param s The Shape.
     * @return The borders of the Rectangle. 0 is up, 1 is left, 2 is right, 3 is down.
     */
    public static Line2D[] getBorders(Shape s) {
        Rectangle r = s.getBounds();
        Line2D[] borders = new Line2D[4];
        borders[0] = new Line2D.Double(r.x, r.y, r.x + r.width, r.y);
        borders[1] = new Line2D.Double(r.x, r.y, r.x, r.y + r.height);
        borders[2] = new Line2D.Double(r.x + r.width, r.y, r.x + r.width, r.y + r.height);
        borders[3] = new Line2D.Double(r.x, r.y + r.height, r.x + r.width, r.y + r.height);
        return borders;
    }


    public static void drawRotated(BufferedImage i, double angle, int x, int y, Graphics2D g)
    {
        double rotationRequired = Math.toRadians(angle);
        double locationX = i.getWidth() / 2;
        double locationY = i.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        //BufferedImage rotated = op.filter(i, null);
        //return rotated;
        g.drawImage(op.filter(i, null), x, y, null);
    }

    public static List<JSONObject> getObjects(JSONArray a) {
        List<JSONObject> objects = new ArrayList<>();
        for (int i=0; i<a.length(); i++) {
            JSONObject jO = a.getJSONObject(i);
            if (jO != null) objects.add(jO);
        }
        return objects;
    }

    /**
     * Draws one of Undertale's signature black boxes with a white border.
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param g
     */
    public static void drawBox(int x, int y, int width, int height, Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
        for (int i = 0; i < 5; i++) {
            g.setColor(Color.WHITE);
            g.drawRect(x + i, y + i, width - (i * 2), height - (i * 2));
        }
    }


    /*

    public static void drawRotated(BufferedImage i, double angle, int x, int y, Graphics2D g) {
        double degreesToRotate = angle;
        double locationX = i.getWidth() / 2;
        double locationY = i.getHeight() / 2;

        double diff = Math.abs(i.getWidth() - i.getHeight());

//To correct the set of origin point and the overflow
        double rotationRequired = Math.toRadians(degreesToRotate);
        double unitX = Math.abs(Math.cos(rotationRequired));
        double unitY = Math.abs(Math.sin(rotationRequired));

        double correctUx = unitX;
        double correctUy = unitY;

//if the height is greater than the width, so you have to 'change' the axis to correct the overflow
        if(i.getWidth() < i.getHeight()){
            correctUx = unitY;
            correctUy = unitX;
        }

        int posAffineTransformOpX = x-(int)(correctUx*diff);
        int posAffineTransformOpY = y-(int)(correctUy*diff);

//translate the image center to same diff that dislocates the origin, to correct its point set
        AffineTransform objTrans = new AffineTransform();
        objTrans.translate(correctUx*diff, correctUy*diff);
        objTrans.rotate(rotationRequired, locationX, locationY);

        AffineTransformOp op = new AffineTransformOp(objTrans, AffineTransformOp.TYPE_BILINEAR);

        g.drawImage(op.filter(i, null), posAffineTransformOpX, posAffineTransformOpY, null);
    }
    */

}
