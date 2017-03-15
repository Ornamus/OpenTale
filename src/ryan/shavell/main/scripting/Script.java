package ryan.shavell.main.scripting;

import org.omg.CORBA.IMP_LIMIT;
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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        List<Class> classes = new ArrayList<>();
        Collections.addAll(classes, new Class[]{
                Collections.class,
                Log.class,
                Utils.class,
                Main.class,
                Game.class,
                Projectile.class,
                ProjectileStar.class,
                SpriteSheet.class,
                Animation.class,
                ImageLoader.class,
                AudioHandler.class,
                BufferedImage.class,
                ArrayList.class,
                System.class
        });

        Collections.addAll(classes, Action.getCoreActions());
        loadClasses(classes.toArray(new Class[classes.size()]));
    }

    public void loadClass(Class clazz) {
        try {
            engine.eval("var " + clazz.getSimpleName() + " = Java.type(\"" + clazz.getName() + "\");");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadClasses(Class... classes) {
        String s = "";
        for (Class c : classes) {
            s += "var " + c.getSimpleName() + " = Java.type(\"" + c.getName() + "\");\n";
        }
        try {
            engine.eval(s);
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
