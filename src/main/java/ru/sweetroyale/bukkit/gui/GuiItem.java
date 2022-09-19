package ru.sweetroyale.bukkit.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

@Setter
@Getter
@AllArgsConstructor
public class GuiItem {

    private ItemStack item;
    private BiConsumer<Player, ClickType> onClick;

    public GuiItem(ItemStack itemStack) {
        this(itemStack, (player, clickType) -> {
            //nothing
        });
    }
}
