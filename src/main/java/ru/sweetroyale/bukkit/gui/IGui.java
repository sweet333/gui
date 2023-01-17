package ru.sweetroyale.bukkit.gui;

import gnu.trove.map.TIntObjectMap;
import org.bukkit.entity.Player;

public interface IGui {

    GuiDrawer getGuiDrawer();

    GuiInitializer getGuiInitializer();

    void setGuiDrawer(GuiDrawer guiDrawer);

    void setGuiInitializer(GuiInitializer guiInitializer);

    void onClose();

    void clearInventory();

    int getSize();

    boolean isDisableAction();

    void setDisableAction(boolean isDisableAction);

    void setUpdater(long ticks);

    void removeUpdater();

    String getTitle();

    Player getPlayer();

    TIntObjectMap<GuiItem> getItems();

    void open();
}
