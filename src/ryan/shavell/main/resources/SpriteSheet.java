package ryan.shavell.main.resources;

import ryan.shavell.main.stuff.Log;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpriteSheet {

    private BufferedImage source;
    private int frameWidth, frameHeight, horizontalRows, verticalRows;
    private BufferedImage[][] images;

    public SpriteSheet(int frameWidth, int frameHeight, int horizRows, int vertRows, String imageName) {
        this(frameWidth, frameHeight, horizRows, vertRows, ImageLoader.getImage(imageName));
    }

    /*
    public SpriteSheet(int frameWidth, int frameHeight, int horizRows, int vertRows, BufferedImage image) {
        source = image;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        horizontalRows = horizRows;
        verticalRows = vertRows;
        images = new BufferedImage[horizRows][vertRows];
        for (int x=0; x<horizRows; x++) {
            int imgX = 1 + (x * (frameWidth + 1));
            for (int y=0; y<vertRows; y++) {
                int imgY = 1 + (y * (frameHeight + 1));
                images[x][y] = source.getSubimage(imgX, imgY, frameWidth, frameHeight);
            }
        }
    }
    */

    public SpriteSheet(int frameWidth, int frameHeight, int horizRows, int vertRows, BufferedImage image) {
        this(frameWidth, frameHeight, horizRows, vertRows, 1, 1, 1, image);
    }

    public SpriteSheet(int frameWidth, int frameHeight, int horizRows, int vertRows, int spacing, int startX, int startY, BufferedImage image) {
        //spacing -= 1;
        source = image;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        horizontalRows = horizRows;
        verticalRows = vertRows;
        images = new BufferedImage[horizRows][vertRows];
        for (int x=0; x<horizRows; x++) {
            int imgX = startX + (x * (frameWidth)) + (spacing * x);
            for (int y=0; y<vertRows; y++) {
                int imgY = startY + (y * (frameHeight)) + (spacing * y);
                //Log.d("x : " + imgX  + ", y: " + imgY + ", width: " + frameWidth + ", frameHeight: " + frameHeight);
                images[x][y] = source.getSubimage(imgX, imgY, frameWidth, frameHeight);
            }
        }
    }

    public BufferedImage get(int x, int y) {
        return images[x][y];
    }

    public List<BufferedImage> getAllImages()  {
        List<BufferedImage> images = new ArrayList();
        for (int y=0;y<verticalRows;y++) {
            List<BufferedImage> row = new ArrayList<>();
            for (int x=0;x<horizontalRows;x++) {
                images.add(get(x, y));
            }
        }
        return images;
    }

    public BufferedImage[] getImageArray() {
        BufferedImage[] allImages = new BufferedImage[getAllImages().size()];
        int index = 0;
        for (BufferedImage[] array : images) {
            for (BufferedImage i : array) {
                allImages[index] = i;
                index++;
            }
        }
        return allImages;
    }

    public List<List<BufferedImage>> getImageRows() {
        List<List<BufferedImage>> images = new ArrayList<>();
        for (int y=0;y<verticalRows;y++) {
            List<BufferedImage> row = new ArrayList<>();
            for (int x=0;x<horizontalRows;x++) {
                row.add(get(x, y));
            }
            images.add(row);
        }
        return images;
    }
}
