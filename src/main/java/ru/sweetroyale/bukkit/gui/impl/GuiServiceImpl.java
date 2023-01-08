package ru.sweetroyale.bukkit.gui.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import ru.sweetroyale.bukkit.gui.GuiProvider;
import ru.sweetroyale.bukkit.gui.GuiService;
import ru.sweetroyale.bukkit.gui.IGui;
import ru.sweetroyale.bukkit.gui.listener.GuiListener;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class GuiServiceImpl implements GuiService {

    @Getter
    private Plugin plugin;
    private Map<String, IGui> inventoryMap;

    public static GuiService registerWith(Plugin plugin) {
        GuiService service = new GuiServiceImpl(plugin, new HashMap<>());

        GuiProvider.register(service);

        plugin.getServer().getPluginManager().registerEvents(new GuiListener(service), plugin);
        return service;
    }

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
