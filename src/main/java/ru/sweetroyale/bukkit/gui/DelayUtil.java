package ru.sweetroyale.bukkit.gui;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
class DelayUtil {

    private final Table<String, String, Long> delayTable = HashBasedTable.create();

    public void putDelay(@NonNull String delayName, @NonNull Player player, long mills) {
        delayTable.put(delayName, player.getName(), System.currentTimeMillis() + mills);
    }

    public long getDelay(@NonNull String delayName, @NonNull Player player) {
        Long playerDelay = delayTable.get(delayName, player.getName());
        return playerDelay == null ? 0 : playerDelay - System.currentTimeMillis();
    }

    public void removeDelay(@NonNull String delayName, @NonNull Player player) {
        delayTable.remove(delayName, player.getName());
    }

    public boolean hasDelay(@NonNull String delayName, @NonNull Player player) {
        return getDelay(delayName, player) > 0;
    }

}
