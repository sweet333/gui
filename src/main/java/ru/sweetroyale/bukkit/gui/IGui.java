package ru.sweetroyale.bukkit.gui;

import gnu.trove.map.TIntObjectMap;
import org.bukkit.entity.Player;

public interface IGui {

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

}
