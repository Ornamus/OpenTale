package ryan.shavell.main.resources;

import java.awt.image.BufferedImage;

public class Animation {

    private int ticksPerFrame;
    private BufferedImage[] images;
    private boolean loop = true;

    private boolean paused = false;
    private boolean done = false;
    private int currentFrame = 0;
    private int ticksSinceLastChange = 0;

    public Animation(int ticksPerFrame, BufferedImage... images) {
        this.ticksPerFrame = ticksPerFrame;
        this.images = images;
    }

    public Animation(int ticksPerFrame, boolean loop, BufferedImage... images) {
        this.ticksPerFrame = ticksPerFrame;
        this.images = images;
        this.loop = loop;
    }

    public void reset() {
        currentFrame = 0;
        ticksSinceLastChange = 0;
        done = false;
        paused = false;
    }

    public void setCurrentFrame(int frame) {
        currentFrame = frame;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public BufferedImage getImage() {
        if (paused) {
            return getImageWithoutIncrement();
        } else {
            if (ticksSinceLastChange == ticksPerFrame) {
                ticksSinceLastChange = 0;
                currentFrame++;
            } else {
                ticksSinceLastChange++;
            }
            if (currentFrame == images.length) {
                if (loop) {
                    currentFrame = 0;
                } else {
                    currentFrame--;
                    done = true;
                }
            }
            return images[currentFrame];
        }
    }

    public BufferedImage getImageWithoutIncrement() {
        return images[currentFrame];
    }

    public boolean isDone() {
        return done;
    }
}
