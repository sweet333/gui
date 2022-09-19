package ru.sweetroyale.bukkit.gui.impl;

import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import ru.sweetroyale.bukkit.gui.GuiService;
import ru.sweetroyale.bukkit.gui.IGui;
import ru.sweetroyale.bukkit.gui.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

@Getter
public abstract class PagedGui implements IGui {

    private static final ItemStack NEXT_PAGE_ITEM = new ItemStack(Material.ARROW);
    private static final ItemStack BACK_PAGE_ITEM = new ItemStack(Material.ARROW);

    private static GuiService guiService;
    private static Plugin plugin;

    public static void init(Plugin plugin, GuiService guiService) {
        PagedGui.guiService = guiService;
        PagedGui.plugin = plugin;
    }

    protected final int size;
    protected final String title;

    protected Player player;

    protected int currentPage = 0;
    protected Gui currentGui = null;

    protected BukkitTask updater = null;

    private boolean updated = false;
    private long updateTicks = 0;

    @Setter
    protected boolean disableAction = true;

    private final TIntObjectMap<GuiItem> buttons = TCollections.synchronizedMap(new TIntObjectHashMap<>());
    private final List<GuiItem> items = new ArrayList<>();
    private final List<Integer> markup = new ArrayList<>();

    public PagedGui(@NonNull Player player, int size, String title) {
        this.player = player;
        this.size = size * 9;
        this.title = title;

        setPage(0);
    }

    @Override
    public TIntObjectMap<GuiItem> getItems() {
        return currentGui.getItems();
    }

    @Override
    public void clearInventory() {
        buttons.clear();
        items.clear();

        currentGui.clearInventory();
    }

    @Override
    public void setUpdater(long updateTicks) {
        this.updateTicks = updateTicks;

        removeUpdater();

        updater = Bukkit.getScheduler()
                .runTaskTimer(plugin, () -> {
                    clearMarkup();

                    draw();

                    drawMarkup();

                    if (!updated) {
                        updated = true;

                        onFirstUpdate();
                    }
                }, updateTicks, updateTicks);
    }

    @Override
    public void removeUpdater() {
        if (updater == null) {
            return;
        }

        updater.cancel();
    }

    public void setPage(int pageId) {
        currentPage = pageId;

        currentGui = new Gui(player, size / 9, title + (currentPage > 0 ? " ▸ " + (currentPage + 1) : "")) {

            @Override
            public void draw() {
                int i = 0;

                if (hasBackPage())
                    setItem(size - 6, BACK_PAGE_ITEM, (player1, clickType) -> {
                        setPage(currentPage - 1);
                        PagedGui.this.open();
                    });


                if (hasNextPage())
                    setItem(size - 4, NEXT_PAGE_ITEM, (player1, clickType) -> {
                        setPage(currentPage + 1);
                        PagedGui.this.open();
                    });

                for (GuiItem guiItem : getItemsByPage(currentPage)) {
                    setItem(markup.get(i), guiItem);
                    i++;
                }
            }

            @Override
            public void onOpen() {
                if (updateTicks > 0) {
                    PagedGui.this.setUpdater(updateTicks);
                }
            }

            @Override
            public void onClose() {
                PagedGui.this.removeUpdater();
            }
        };

        currentGui.draw();
    }

    public void open() {
        if (player == null || !player.isOnline()) {
            removeUpdater();
            return;
        }

        currentGui.open();
        onOpen();
    }

    private List<GuiItem> getItemsByPage(int page) {
        List<GuiItem> guiItemList = new ArrayList<>();

        int pageMin = page * markup.size();
        int pageMax = pageMin + markup.size() - 1;

        for (int i = pageMin; i <= pageMax; i++)
            try {
                guiItemList.add(items.get(i));
            } catch (IndexOutOfBoundsException e) {
                break;
            }

        return guiItemList;
    }

    public boolean hasNextPage() {
        int pageMaxItemCount = (currentPage + 1) * markup.size();
        int itemCount = items.size();

        return pageMaxItemCount < itemCount;
    }

    public boolean hasBackPage() {
        return currentPage > 0;
    }

    public void setButton(int slot, @NonNull ItemStack itemStack, BiConsumer<Player, ClickType> onClick) {
        buttons.put(slot, new GuiItem(itemStack, onClick));
        currentGui.setItem(slot, new GuiItem(itemStack, onClick));
    }

    public void setButton(int slot, @NonNull GuiItem item) {
        buttons.put(slot, item);
        currentGui.setItem(slot, item);
    }

    public void removeButton(int slot) {
        buttons.remove(slot);
        currentGui.removeItem(slot);
    }

    public void setButton(int slot, @NonNull ItemStack itemStack) {
        buttons.put(slot, new GuiItem(itemStack));
        currentGui.setItem(slot, new GuiItem(itemStack));
    }

    public void addItem(ItemStack itemStack, BiConsumer<Player, ClickType> onClick) {
        items.add(new GuiItem(itemStack, onClick));
    }

    public void addItem(@NonNull GuiItem item) {
        items.add(item);
    }

    public void addItem(ItemStack itemStack) {
        items.add(new GuiItem(itemStack));
    }

    protected int toSlot(int x, int y) {
        return 9 * y + x - 10;
    }

    public void setMarkup(Integer... slots) {
        this.markup.clear();
        this.markup.addAll(Arrays.asList(slots));
    }

    public void setMarkup(List<Integer> slots) {
        this.markup.clear();
        this.markup.addAll(slots);
    }

    public void setDefaultMarkupItems() {
        setMarkup(10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34);
    }

    public void drawMarkup() {
        currentGui.draw();
    }

    public void clearMarkup() {
        items.clear();
        markup.forEach(integer -> currentGui.removeItem(integer));
    }

    public void onClose() {

    }

    public void onOpen() {

    }

    public void onFirstUpdate() {

    }

    public abstract void draw();
}
