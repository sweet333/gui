package ru.sweetroyale.bukkit.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;

class GuiListener implements Listener {

    private final Map<String, IGui> inventoryMap;

    public GuiListener(Map<String, IGui> inventoryMap) {
        this.inventoryMap = inventoryMap;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        IGui gui = inventoryMap.get(player.getName());

        if (gui == null) {
            return;
        }

        Inventory inventory = event.getView().getTopInventory();

        if (!gui.getPlayer().getName().equals(player.getName())) {
            return;
        }

        event.setCancelled(event.getClick().isShiftClick() || gui.isDisableAction());

        int slot = event.getRawSlot();

        if (slot >= 0 && slot < inventory.getSize()) {
            event.setCancelled(true);

            GuiItem guiItem = gui.getItems().get(slot);

            if (guiItem == null || inventory.getItem(slot) == null) {
                return;
            }

            if (DelayUtil.hasDelay("gui_click", player)) {
                return;
            }

            DelayUtil.putDelay("gui_click", player, 200L);

            guiItem.getOnClick().accept(player, event.getClick());
        }
    }

    @EventHandler
    private void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        IGui gui = inventoryMap.get(player.getName());

        if (gui == null) {
            return;
        }

        if (!player.getName().equalsIgnoreCase(gui.getPlayer().getName())) {
            return;
        }

        if (gui.isDisableAction()) {
            event.setCancelled(true);
        }

        for (int slot : event.getRawSlots()) {
            if (slot >= 0 && slot < gui.getSize()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();

        IGui gui = inventoryMap.get(player.getName());

        if (gui == null) {
            return;
        }

        inventoryMap.remove(player.getName());

        gui.removeUpdater();
        gui.onClose();
    }
}
