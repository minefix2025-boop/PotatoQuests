package com.potatoquests;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Quest {

    private String name;
    private String description;
    private Material[] requiredItems;
    private ItemStack[] rewards;

    public Quest(String name, String description, Material[] requiredItems, ItemStack[] rewards) {
        this.name = name;
        this.description = description;
        this.requiredItems = requiredItems;
        this.rewards = rewards;
    }

    public ItemStack getDisplayItem() {
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§e§l" + this.name);
            
            List<String> lore = new ArrayList<>();
            lore.add("§f" + this.description);
            lore.add("");
            lore.add("§6§lЗАДАНИЕ:");
            for (Material mat : requiredItems) {
                String materialName = mat.toString()
                    .replace("_", " ")
                    .toLowerCase();
                // Капитализация первой буквы
                materialName = materialName.substring(0, 1).toUpperCase() + materialName.substring(1);
                lore.add("§f  ✓ " + materialName);
            }
            lore.add("");
            lore.add("§6§lНАГРАДЫ:");
            for (int i = 0; i < Math.min(4, rewards.length); i++) {
                String rewardName = rewards[i].getType().toString()
                    .replace("_", " ")
                    .toLowerCase();
                // Капитализация
                rewardName = rewardName.substring(0, 1).toUpperCase() + rewardName.substring(1);
                lore.add("§f  ★ " + rewardName + " x" + rewards[i].getAmount());
            }
            lore.add("");
            lore.add("§a§lНажми ЛКМ чтобы принять!");
            
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        
        return item;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Material[] getRequiredItems() {
        return requiredItems;
    }

    public ItemStack[] getRewards() {
        return rewards;
    }
}
