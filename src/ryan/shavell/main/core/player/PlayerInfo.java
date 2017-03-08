package ryan.shavell.main.core.player;

import ryan.shavell.main.logic.SoulType;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfo {

    public static int level = 1;
    public static String name = "SHIFTY";
    public static Weapon weapon = new Knife();
    public static int maxHealth = 20;
    public static int currentHealth = 20;
    public static SoulType soulType = SoulType.NORMAL;
    public static List<Item> items = new ArrayList<>();

}
