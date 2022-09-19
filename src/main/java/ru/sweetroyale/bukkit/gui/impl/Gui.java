package ru.sweetroyale.bukkit.gui.impl;

import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import ru.sweetroyale.bukkit.gui.GuiService;
import ru.sweetroyale.bukkit.gui.IGui;
import ru.sweetroyale.bukkit.gui.GuiItem;

import java.util.function.BiConsumer;

@Getter
public abstract class Gui implements IGui {

    private static GuiService guiService;
    private static Plugin plugin;

    public static void init(Plugin plugin, GuiService guiService) {
        Gui.guiService = guiService;
        Gui.plugin = plugin;
    }

    protected final int size;
    protected final String title;

    protected Inventory inventory;
    protected Player player;
    protected BukkitTask updater = null;

    @Setter
    protected boolean disableAction = true;

    private final TIntObjectMap<GuiItem> items = TCollections.synchronizedMap(new TIntObjectHashMap<>());

    public Gui(@NonNull Player player, int size, String title) {
        this.player = player;
        this.size = size * 9;
        this.title = title;
        this.inventory = Bukkit.createInventory(player, this.size, title);
    }

    @Override
    public void clearInventory() {
        items.clear();
        inventory.clear();
    }

    public void open() {
        player.openInventory(inventory);

        onOpen();

        guiService.addGui(this);
    }

    @Override
    public void setUpdater(long ticks) {
        if (updater != null) {
            updater.cancel();
        }

        updater = Bukkit.getScheduler()
                .runTaskTimer(plugin, this::draw, 0L, ticks);
    }

    @Override
    public void removeUpdater() {
        if (updater == null) {
            return;
        }

        updater.cancel();
    }

    public void setItem(int slot, @NonNull ItemStack itemStack, BiConsumer<Player, ClickType> onClick) {
        GuiItem guiItem = new GuiItem(itemStack, onClick);

        inventory.setItem(slot, itemStack);
        items.put(slot, guiItem);
    }

    public void setItem(int slot, @NonNull ItemStack itemStack) {
        GuiItem guiItem = new GuiItem(itemStack);

        inventory.setItem(slot, itemStack);
        items.put(slot, guiItem);
    }

    public void setItem(int slot, @NonNull GuiItem item) {
        inventory.setItem(slot, item.getItem());
        items.put(slot, item);
    }

    public void removeItem(int slot) {
        GuiItem item = items.remove(slot);

        if (item == null) {
            return;
        }

        inventory.setItem(slot, null);
    }

    public void addItem(@NonNull ItemStack itemStack, BiConsumer<Player, ClickType> onClick) {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            if (inventory.getItem(slot) == null) {
                setItem(slot, new GuiItem(itemStack, onClick));
                return;
            }
        }
    }

    public void addItem(@NonNull GuiItem item) {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            if (inventory.getItem(slot) == null) {
                setItem(slot, item);
                return;
            }
        }
    }

    public void addItem(@NonNull ItemStack itemStack) {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            if (inventory.getItem(slot) == null) {
                setItem(slot, new GuiItem(itemStack));
                return;
            }
        }
    }

    protected int toSlot(int x, int y) {
        return 9 * y + x - 10;
    }

    public abstract void draw();

    public void onClose() {

    }

    public void onOpen() {

    }

}
