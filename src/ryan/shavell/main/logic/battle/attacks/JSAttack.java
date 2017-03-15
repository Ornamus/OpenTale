package ryan.shavell.main.logic.battle.attacks;

import ryan.shavell.main.scripting.Script;

//TODO: somehow load up script sooner to prevent hang the first time this is used. Might require reusing attack objects/attack templates that are loaded sooner

public class JSAttack extends Attack {

    Script script;

    public JSAttack(String script) {
        try {
            this.script = Script.loadScript(script);
            this.script.put("attack", this);
            this.script.invokeMethod("init");
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
