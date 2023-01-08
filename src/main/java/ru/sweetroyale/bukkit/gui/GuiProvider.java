package ru.sweetroyale.bukkit.gui;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GuiProvider {

    private GuiService service;

    public GuiService get() {
        return service;
    }

    public void register(@NonNull GuiService service) {
        GuiProvider.service = service;
    }
}
