# gui

easy-to-use minecraft gui

Initialization

```java
public class BukkitPluginExample extends JavaPlugin {

    @Override
    public void onEnable() {
        GuiServiceImpl.registerWith(this);
        
    }
}

```

Simple gui example:

```java
public class TestGui extends PagedGui {

    public TestGui(Player player) {
        super(player, 5, "Some strange inventory");

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
        super(player, 5, "Some paged inventory");

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

