package ryan.shavell.main.logic.battle.attacks;

import ryan.shavell.main.scripting.Script;

public class JSAttack extends Attack {

    Script script;

    public JSAttack(String code) {
        try {
            script = Script.toScript(code);
            script.put("attack", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        super.start();
        script.invokeMethod("start");
    }

    @Override
    public void tick() {
        super.tick();
        script.invokeMethod("tick");
    }
}
