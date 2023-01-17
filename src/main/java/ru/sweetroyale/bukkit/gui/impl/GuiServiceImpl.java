package ru.sweetroyale.bukkit.gui.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.sweetroyale.bukkit.gui.GuiDrawer;
import ru.sweetroyale.bukkit.gui.GuiInitializer;
import ru.sweetroyale.bukkit.gui.GuiService;
import ru.sweetroyale.bukkit.gui.IGui;

import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class GuiServiceImpl implements GuiService {

    private final Map<String, IGui> inventoryMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final Plugin plugin;
    private ItemStack nextPageItem = new ItemStack(Material.ARROW);
    private ItemStack backPageItem = new ItemStack(Material.ARROW);

    @Override
    public MultiPageGui createMultiPageGui(Player player, String title, int size, GuiDrawer guiDrawer, GuiInitializer guiInitializer) {
        return new MultiPageGui(backPageItem, nextPageItem, player, size, title, guiDrawer, guiInitializer, this, inventoryMap, plugin);
    }

    @Override
    public MultiPageGui createMultiPageGui(Player player, String title, int size, GuiDrawer guiDrawer) {
        return new MultiPageGui(backPageItem, nextPageItem, player, size, title, guiDrawer, gui -> {
            MultiPageGui pagedGui = (MultiPageGui) gui;
            pagedGui.setDefaultMarkupItems();

            gui.getGuiDrawer().draw(gui);
            gui.open();
        }, this, inventoryMap, plugin);
    }

    @Override
    public OnePageGui createOnePageGui(Player player, String title, int size, GuiDrawer guiDrawer, GuiInitializer guiInitializer) {
        return new OnePageGui(this, inventoryMap, plugin, player, size, title, guiDrawer, guiInitializer);
    }

    @Override
    public OnePageGui createOnePageGui(Player player, String title, int size, GuiDrawer guiDrawer) {
        return new OnePageGui(this, inventoryMap, plugin, player, size, title, guiDrawer, gui -> {
            gui.getGuiDrawer().draw(gui);
            gui.open();
        });
    }
}
