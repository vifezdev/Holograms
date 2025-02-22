package lol.vifez.holograms;

import co.aikar.commands.BukkitCommandManager;
import lol.vifez.holograms.api.HologramsAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class HologramsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new HologramsCommand(this));

        HologramsAPI.loadHologramsFromConfig();

        getLogger().info("Holograms Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        HologramsAPI.saveAllHolograms();

        getLogger().info("Holograms Plugin Disabled!");
    }
}