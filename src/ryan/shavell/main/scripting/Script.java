package ryan.shavell.main.scripting;

import ryan.shavell.main.stuff.Log;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.Reader;

public class Script {

    private static ScriptEngineManager engineManager;
    private static ScriptEngine engine;
    private static Invocable invocable;

    protected Script(String code) {
        init();
        try {
            engine.eval(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Script(Reader r) {
        init();
        try {
            engine.eval(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        engineManager = new ScriptEngineManager();
        engine = engineManager.getEngineByName("nashorn");
        invocable = (Invocable) engine;
    }

    public void invokeMethod(String method, Object...args) {
        try {
            invocable.invokeFunction(method, args);
        } catch (Exception e) {
            Log.e("Script.invokeMethod() exception!");
            e.printStackTrace();
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
}
