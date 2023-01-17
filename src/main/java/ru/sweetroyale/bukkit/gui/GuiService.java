package ru.sweetroyale.bukkit.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.sweetroyale.bukkit.gui.impl.MultiPageGui;
import ru.sweetroyale.bukkit.gui.impl.OnePageGui;

import java.util.Map;

public interface GuiService {
    MultiPageGui createMultiPageGui(Player player, String title, int size, GuiDrawer guiDrawer, GuiInitializer guiInitializer);

    MultiPageGui createMultiPageGui(Player player, String title, int size, GuiDrawer guiDrawer);

    OnePageGui createOnePageGui(Player player, String title, int size, GuiDrawer guiDrawer, GuiInitializer guiInitializer);

    OnePageGui createOnePageGui(Player player, String title, int size, GuiDrawer guiDrawer);

    Map<String, IGui> getInventoryMap();

    ItemStack getNextPageItem();

    ItemStack getBackPageItem();

    void setNextPageItem(ItemStack nextPageItem);

    void setBackPageItem(ItemStack backPageItem);
}
