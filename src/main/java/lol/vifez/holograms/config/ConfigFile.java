package lol.vifez.holograms.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigFile {

    private final FileConfiguration config;
    private final File configFile;

    public ConfigFile(JavaPlugin plugin) {
        this.configFile = new File(plugin.getDataFolder(), "holograms.yml");
        if (!configFile.exists()) {
            plugin.saveResource("holograms.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveHologram(String name, Location location, List<String> lines) {
        String path = "holograms." + name;
        config.set(path + ".location.world", location.getWorld().getName());
        config.set(path + ".location.x", location.getX());
        config.set(path + ".location.y", location.getY());
        config.set(path + ".location.z", location.getZ());
        config.set(path + ".lines", lines);
        saveConfig();
    }

    public Map<String, Object> getHologramData(String name) {
        return config.getConfigurationSection("holograms." + name).getValues(false);
    }
}