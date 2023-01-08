package ru.sweetroyale.bukkit.gui.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import ru.sweetroyale.bukkit.gui.GuiItem;
import ru.sweetroyale.bukkit.gui.GuiService;
import ru.sweetroyale.bukkit.gui.IGui;

@RequiredArgsConstructor
public class GuiListener implements Listener {

    private final GuiService guiService;

    @EventHandler(priority = EventPriority.MONITOR)
    private void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        IGui gui = guiService.getGui(event);

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
        IGui gui = guiService.getGui(event);

        if (gui == null) {
            return;
        }

        if (!event.getWhoClicked().getName().equalsIgnoreCase(gui.getPlayer().getName())) {
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

        IGui gui = guiService.getGui(player);

        if (gui == null) {
            return;
        }

        guiService.removeGui(gui);

        gui.removeUpdater();
        gui.onClose();
    }
}
