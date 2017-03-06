package ryan.shavell.main.core.player;

import ryan.shavell.main.logic.SoulType;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfo {

    public static int level = 3;
    public static String name = "KEVIN";
    public static Weapon weapon = new Knife();
    public static int maxHealth = 30;
    public static int currentHealth = 30;
    public static SoulType soulType = SoulType.NORMAL;
    public static List<Item> items = new ArrayList<>();

}
