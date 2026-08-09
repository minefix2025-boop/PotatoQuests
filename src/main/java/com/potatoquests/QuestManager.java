package com.potatoquests;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestManager implements Listener {

    private JavaPlugin plugin;
    private List<Quest> quests;
    private Map<Player, Integer> playerQuestPage;

    public QuestManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.quests = new ArrayList<>();
        this.playerQuestPage = new HashMap<>();
        loadQuests();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void loadQuests() {
        createDefaultQuests();
    }

    private void createDefaultQuests() {
        // Квест 1: Рубить дерево
        Quest quest1 = new Quest(
            "Рубить дерево",
            "Добыть 64 блока дерева",
            new Material[]{Material.OAK_LOG},
            new ItemStack[]{
                createReward(Material.DIAMOND, 1),
                createReward(Material.NETHERITE_INGOT, 1),
                createReward(Material.ENCHANTED_GOLDEN_APPLE, 1),
                createReward(Material.TOTEM_OF_UNDYING, 1)
            }
        );
        quests.add(quest1);

        // Квест 2: Копать
        Quest quest2 = new Quest(
            "Копать землю",
            "Добыть 32 блока земли",
            new Material[]{Material.DIRT},
            new ItemStack[]{
                createReward(Material.DIAMOND, 2),
                createReward(Material.NETHERITE_INGOT, 2),
                createReward(Material.ENCHANTED_GOLDEN_APPLE, 2),
                createReward(Material.TOTEM_OF_UNDYING, 2)
            }
        );
        quests.add(quest2);

        // Квест 3: Убить игроков
        Quest quest3 = new Quest(
            "Убить игроков",
            "Убить 5 игроков",
            new Material[]{Material.DIAMOND_SWORD},
            new ItemStack[]{
                createReward(Material.DIAMOND_BLOCK, 1),
                createReward(Material.NETHERITE_BLOCK, 1),
                createReward(Material.ENCHANTED_GOLDEN_APPLE, 3),
                createReward(Material.TOTEM_OF_UNDYING, 3)
            }
        );
        quests.add(quest3);

        // Квест 4: Скрафтить
        Quest quest4 = new Quest(
            "Скрафтить меч",
            "Скрафтить алмазный меч",
            new Material[]{Material.CRAFTING_TABLE},
            new ItemStack[]{
                createReward(Material.DIAMOND, 5),
                createReward(Material.NETHERITE_INGOT, 5),
                createReward(Material.ENCHANTED_GOLDEN_APPLE, 5),
                createReward(Material.TOTEM_OF_UNDYING, 5)
            }
        );
        quests.add(quest4);

        // Квест 5: Сдать ресурсы
        Quest quest5 = new Quest(
            "Сдать железо",
            "Сдать 32 железа",
            new Material[]{Material.IRON_INGOT},
            new ItemStack[]{
                createReward(Material.DIAMOND, 3),
                createReward(Material.NETHERITE_INGOT, 3),
                createReward(Material.ENCHANTED_GOLDEN_APPLE, 2),
                createReward(Material.TOTEM_OF_UNDYING, 2)
            }
        );
        quests.add(quest5);
    }

    public void openQuestGUI(Player player) {
        int page = playerQuestPage.getOrDefault(player, 0);
        Inventory inv = plugin.getServer().createInventory(null, 54, "§6§lКВЕСТЫ §7(Страница " + (page + 1) + ")");

        // Заполняем инвентарь квестами
        int startIndex = page * 45;
        int endIndex = Math.min(startIndex + 45, quests.size());

        for (int i = startIndex; i < endIndex; i++) {
            Quest quest = quests.get(i);
            ItemStack questItem = quest.getDisplayItem();
            inv.setItem(i - startIndex, questItem);
        }

        // Добавляем кнопки навигации снизу
        if (page > 0) {
            inv.setItem(45, createNavigationButton("§c◄ Назад", Material.ARROW));
        }

        if (endIndex < quests.size()) {
            inv.setItem(53, createNavigationButton("§aВперед ►", Material.ARROW));
        }

        player.openInventory(inv);
    }

    private ItemStack createNavigationButton(String name, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().contains("КВЕСТЫ")) {
            return;
        }

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        if (slot >= 54) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) {
            return;
        }

        // Навигация
        if (slot == 45 && clicked.getType() == Material.ARROW) {
            int page = playerQuestPage.getOrDefault(player, 0);
            if (page > 0) {
                playerQuestPage.put(player, page - 1);
                openQuestGUI(player);
            }
            return;
        }

        if (slot == 53 && clicked.getType() == Material.ARROW) {
            int page = playerQuestPage.getOrDefault(player, 0);
            if ((page + 1) * 45 < quests.size()) {
                playerQuestPage.put(player, page + 1);
                openQuestGUI(player);
            }
            return;
        }

        // Клик на квест
        if (clicked.getType() == Material.PAPER) {
            ItemMeta meta = clicked.getItemMeta();
            if (meta != null && meta.getDisplayName() != null) {
                String questName = meta.getDisplayName().replace("§e§l", "");
                player.sendMessage("§a✓ §fТы принял квест: §e" + questName);
                player.closeInventory();
            }
        }
    }

    private ItemStack createReward(Material material, int amount) {
        return new ItemStack(material, amount);
    }

    public List<Quest> getQuests() {
        return quests;
    }
}






