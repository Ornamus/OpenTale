package ryan.shavell.main.resources;

import java.awt.image.BufferedImage;

public class Animation {

    private int ticksPerFrame;
    private BufferedImage[] images;

    private int currentFrame = 0;
    private int ticksSinceLastChange = 0;

    public Animation(int ticksPerFrame, BufferedImage... images) {
        this.ticksPerFrame = ticksPerFrame;
        this.images = images;
    }

    public void reset() {
        currentFrame = 0;
        ticksSinceLastChange = 0;
    }

    public void setCurrentFrame(int frame) {
        currentFrame = frame;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public BufferedImage getImage() {
        if (ticksSinceLastChange == ticksPerFrame) {
            ticksSinceLastChange = 0;
            currentFrame++;
        } else {
            ticksSinceLastChange++;
        }
        if (currentFrame == images.length) currentFrame = 0;
        return images[currentFrame];
    }

    public BufferedImage getImageWithoutIncrement() {
        return images[currentFrame];
    }
}
