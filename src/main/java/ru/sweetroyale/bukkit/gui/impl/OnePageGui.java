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
import ru.sweetroyale.bukkit.gui.*;

import java.util.Map;
import java.util.function.BiConsumer;

@Getter
public class OnePageGui implements IGui {

    private final GuiService guiService;
    private final Map<String, IGui> inventoryMap;
    private final Plugin plugin;
    protected final int size;
    protected final String title;
    @Setter
    protected GuiDrawer guiDrawer;
    @Setter
    protected GuiInitializer guiInitializer;
    protected Inventory inventory;
    protected Player player;
    protected BukkitTask updater = null;

    @Setter
    protected boolean disableAction = true;

    private final TIntObjectMap<GuiItem> items = TCollections.synchronizedMap(new TIntObjectHashMap<>());

    public OnePageGui(GuiService guiService, Map<String, IGui> inventoryMap, Plugin plugin, @NonNull Player player, int size, String title, GuiDrawer guiDrawer, GuiInitializer guiInitializer) {
        this.guiService = guiService;
        this.inventoryMap = inventoryMap;
        this.plugin = plugin;
        this.player = player;
        this.size = size * 9;
        this.title = title;
        this.guiDrawer = guiDrawer;
        this.guiInitializer = guiInitializer;
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

        inventoryMap.put(player.getName(), this);
    }

    @Override
    public void setUpdater(long ticks) {
        if (updater != null) {
            updater.cancel();
        }

        updater = Bukkit.getScheduler()
                .runTaskTimer(plugin, () -> this.guiDrawer.draw(this), 0L, ticks);
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

    public void onClose() {

    }

    public void onOpen() {

    }

}
