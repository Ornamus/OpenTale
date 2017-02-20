package ryan.shavell.main.resources;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage source;
    private int frameWidth, frameHeight, horizontalRows, verticalRows;
    private BufferedImage[][] images;

    public SpriteSheet(int frameWidth, int frameHeight, int horizRows, int vertRows, String imageName) {
        this(frameWidth, frameHeight, horizRows, vertRows, ImageLoader.getImage(imageName));
    }

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
                //System.out.println("x: " + imgX + ", y: " + imgY + ", width: " + frameWidth + ", height: " + frameHeight);
                images[x][y] = source.getSubimage(imgX, imgY, frameWidth, frameHeight);
            }
        }
    }

    public BufferedImage getImage(int x, int y) {
        return images[x][y];
    }
}
