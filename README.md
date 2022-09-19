# gui

easy-to-use minecraft gui

Initialization

```java
public class BukkitPluginExample extends JavaPlugin {

    @Override
    public void onEnable() {
        GuiService guiService = new GuiServiceImpl();

        Gui.init(this, guiService);
        PagedGui.init(this, guiService);

        getServer().getPluginManager().registerEvents(new GuiListener(guiService), this);
    }
}

```

Simple gui example:

```java
public class TestGui extends PagedGui {

    public TestGui(Player player) {
        super(player, 5, "Какой-то странный инвентарь");

        draw();
        open();
    }

    @Override
    public void draw() {
        setButton(22, new ItemStack(Material.SAND), (player1, event) -> {
            player1.sendMessage("Wow, I'm sand!");
        });
    }
}
```

Paged gui example:

```java
public class TestPagedGui extends PagedGui {

    public TestPagedGui(Player player) {
        super(player, 5, "Какой-то страничный инвентарь");

        draw();
        open();
    }

    @Override
    public void draw() {
        for (int i = 0; i < 128; i++) {
            addItem(new ItemStack(Material.SAND), (player1, event) -> {
                player1.sendMessage("Wow, I'm sand!");
            });
        }

        drawMarkup();
    }
}
```

