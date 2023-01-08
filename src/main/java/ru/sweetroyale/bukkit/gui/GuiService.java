package ru.sweetroyale.bukkit.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public interface GuiService {

    Plugin getPlugin();

    void addGui(IGui gui);

    void removeGui(IGui gui);

    @Nullable
    IGui getGui(InventoryInteractEvent event);

    @Nullable
    IGui getGui(Player player);

}
