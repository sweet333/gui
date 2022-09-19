# gui

easy-to-use minecraft gui

Пример обычного инвентаря

```java
public class LobbySelectorGui extends PagedGui {

    public LobbySelectorGui(Player player) {
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

Пример страничного инвентаря:

```java
public class LobbySelectorGui extends PagedGui {

    public LobbySelectorGui(Player player) {
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

