package ryan.shavell.main.core.player;

public class Item {

    private String menuName;
    private String name;
    private int health;

    public Item(String name, int health) {
        this.menuName = name;
        this.name = name;
        this.health = health;
    }

    public Item(String menuName, String name, int health) {
        this.menuName = menuName;
        this.name = name;
        this.health = health;
    }

    public String getMenuName() {
        return menuName;
    }
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }
}
