package ryan.shavell.main.logic.entity.overworld;

import ryan.shavell.main.core.Main;
import ryan.shavell.main.dialogue.DialogBox;
import ryan.shavell.main.logic.InputTaker;
import ryan.shavell.main.render.BasicRenderedThing;
import ryan.shavell.main.render.Board;
import ryan.shavell.main.render.Drawable;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

//TODO: get dialog actions working here too

public class Overworld implements Drawable, InputTaker {

    private List<OverworldEntity> entities = new ArrayList<>();

    public Overworld() {
        entities.add(new BasicRenderedThing(0, 0, "home"));
        entities.add(new OverworldEntity(100, 100, "sans_temp"));
        entities.add(new OverworldPlayer(200, 200));
    }

    @Override
    public void tick() {
        entities.forEach(OverworldEntity::tick);
    }

    @Override
    public void draw(Graphics2D g) {
        for (OverworldEntity e : entities) {
            e.draw(g);
        }
    }

    @Override
    public void onKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_Z && !Board.dialogBox.isScrolling()) {
            //usingDialog = true;
            Board.dialogBox.setVisuals(Color.white, Main.SANS);
            Board.dialogBox.setText("nice overworld you've got here, buddy.", "sans_text");
        }

        for (OverworldEntity ent : entities) {
            if (ent instanceof InputTaker) {
                ((InputTaker)ent).onKeyPress(e);
            }
        }
    }

    @Override
    public void onKeyRelease(KeyEvent e) {
        for (OverworldEntity ent : entities) {
            if (ent instanceof InputTaker) {
                ((InputTaker)ent).onKeyRelease(e);
            }
        }
    }

    @Override
    public boolean shouldDoubleSize (){
        return true;
    }
}


