package ryan.shavell.main.scripting;

import ryan.shavell.main.core.Game;
import ryan.shavell.main.core.Main;
import ryan.shavell.main.dialogue.actions.Action;
import ryan.shavell.main.logic.battle.attacks.Asriel.ProjectileStar;
import ryan.shavell.main.logic.battle.attacks.Projectile;
import ryan.shavell.main.resources.Animation;
import ryan.shavell.main.resources.AudioHandler;
import ryan.shavell.main.resources.ImageLoader;
import ryan.shavell.main.resources.SpriteSheet;
import ryan.shavell.main.stuff.Log;
import ryan.shavell.main.stuff.Utils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.image.BufferedImage;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;

public class Script {
    private ScriptEngineManager engineManager;
    private ScriptEngine engine;
    private Invocable invocable;

    private Object result = null;

    protected Script(String code) {
        init();
        try {
            result = engine.eval(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Script(Reader r) {
        init();
        try {
            result = engine.eval(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        engineManager = new ScriptEngineManager();
        engine = engineManager.getEngineByName("nashorn");
        invocable = (Invocable) engine;

        loadClass(Collections.class);
        loadClass(Log.class);
        loadClass(Utils.class);
        loadClass(Main.class);
        loadClass(Game.class);
        loadClass(Projectile.class);
        loadClass(ProjectileStar.class); //TODO: remove?
        loadClass(SpriteSheet.class);
        loadClass(Animation.class);
        loadClass(ImageLoader.class);
        loadClass(AudioHandler.class);
        loadClass(BufferedImage.class);
        loadClass(ArrayList.class);
        for (Class c : Action.getCoreActions()) {
            loadClass(c);
        }
    }

    public void loadClass(Class clazz) {
        try {
            engine.eval("var " + clazz.getSimpleName() + " = Java.type(\"" + clazz.getName() + "\");");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getResult() {
        return result;
    }

    public Object invokeMethod(String method, Object...args) {
        try {
            return invocable.invokeFunction(method, args);
        } catch (Exception e) {
            Log.e("Script.invokeMethod() exception!");
            e.printStackTrace();
            return null;
        }
    }

    public void put(String key, Object value) {
        engine.put(key, value);
    }

    public static Script toScript(String code) {
        return new Script(code);
    }

    public static Script toScript(Reader r) {
        return new Script(r);
    }

    public static Script loadScript(String scriptDir) {
        try {
            return toScript(new InputStreamReader(ImageLoader.class.getResource("scripts/" + scriptDir + ".js").openStream()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
