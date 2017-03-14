var Boogie = Java.type("ryan.shavell.main.logic.battle.encounters.Boogie");

function run() {
    var voice = "boogie_text";
    var portrait = new Animation(0, ImageLoader.getImage("boogie/boogie_portrait"));
    var list = new ArrayList();


    list.add(new ActionSong("Boogie"));
    list.add(new ActionDialog("* Hello./n* I'm [color(236,230,108)BOOGIE]./n* I'm a [color(236,230,108)FLOWER]. I guess.", voice, portrait, 0));
    list.add(new ActionDialog("* So, I've never seen you down here before. You must be new.", voice, portrait, 0));
    list.add(new ActionDialog("* New to the UNDERGROUND, that is.", voice, portrait, 0));
    list.add(new ActionDialog("* Gee, you must be awfully disoriented from that fall.", voice, portrait, 0));
    list.add(new ActionDialog("* I guess I can help you out by showing you how things work around here.", voice, portrait, 0));
    list.add(new ActionDialog("* Not like there's anyone else who can right now.", voice, portrait, 0));
    list.add(new ActionDialog("* Hope you're ready./n* Because, uh, I'm not.", voice, portrait, 0));
    list.add(new ActionStartEncounter(new Boogie()));

    return list;
}