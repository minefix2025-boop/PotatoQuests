package com.potatoquests;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private QuestManager questManager;

    @Override
    public void onEnable() {
        questManager = new QuestManager(this);
        
        // Регистрация команды /quest
        if (getCommand("quest") != null) {
            getCommand("quest").setExecutor(new QuestCommand(questManager));
        }
        
        getLogger().info("§e[PotatoQuests] §aПлагин включен! Версия 1.0");
        getLogger().info("§e[PotatoQuests] §aИспользуй /quest чтобы открыть меню квестов");
    }

    @Override
    public void onDisable() {
        getLogger().info("§e[PotatoQuests] §cПлагин отключен!");
    }

    public QuestManager getQuestManager() {
        return questManager;
    }
}
