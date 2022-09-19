package ru.sweetroyale.bukkit.gui.impl;

import ru.sweetroyale.bukkit.gui.GuiService;
import ru.sweetroyale.bukkit.gui.IGui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class GuiServiceImpl implements GuiService {

    private final Map<String, IGui> inventoryMap = new HashMap<>();

    @Override
    public void addGui(IGui gui) {
        inventoryMap.put(gui.getPlayer().getName(), gui);
    }

    @Override
    public void removeGui(IGui gui) {
        inventoryMap.remove(gui.getPlayer().getName());
    }

    @Nullable
    @Override
    public IGui getGui(InventoryInteractEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            event.setCancelled(true);
            return null;
        }

        return inventoryMap.get(event.getWhoClicked().getName());
    }

    @Nullable
    @Override
    public IGui getGui(Player player) {
        return inventoryMap.get(player.getName());
    }

}
