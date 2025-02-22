package lol.vifez.holograms.api;

import lol.vifez.holograms.HologramsPlugin;
import lol.vifez.holograms.config.ConfigFile;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HologramsAPI {

    private static final ConfigFile configFile;
    private static final List<Hologram> holograms = new ArrayList<>();

    static {
        configFile = new ConfigFile(JavaPlugin.getPlugin(HologramsPlugin.class));
    }

    public static Hologram createHologram(String name, Location location) {
        List<String> lines = new ArrayList<>();
        holograms.add(new Hologram(name, location));
        configFile.saveHologram(name, location, lines);
        return getHologram(name);
    }

    public static void saveHologramLocation(String name, Location location) {
        Hologram hologram = getHologram(name);
        if (hologram != null) {
            List<String> lines = hologram.getLines();
            configFile.saveHologram(name, location, lines);
        }
    }

    public static Hologram getHologram(String name) {
        return holograms.stream()
                .filter(hologram -> hologram.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static void removeHologram(String name) {
        Hologram hologram = getHologram(name);
        if (hologram != null) {
            hologram.clearHologram();
            holograms.remove(hologram);
        }
    }

    public static void loadHologramsFromConfig() {
        Set<String> names = configFile.getConfig().getConfigurationSection("holograms").getKeys(false);
        for (String name : names) {
            Location location = loadLocationFromConfig(name);
            List<String> lines = configFile.getConfig().getStringList("holograms." + name + ".lines");
            Hologram hologram = new Hologram(name, location);
            hologram.setLines(lines);
            holograms.add(hologram);
        }
    }

    public static List<Hologram> getHolograms() {
        return new ArrayList<>(holograms);
    }


    private static Location loadLocationFromConfig(String name) {
        String worldName = configFile.getConfig().getString("holograms." + name + ".location.world");
        double x = configFile.getConfig().getDouble("holograms." + name + ".location.x");
        double y = configFile.getConfig().getDouble("holograms." + name + ".location.y");
        double z = configFile.getConfig().getDouble("holograms." + name + ".location.z");

        return new Location(JavaPlugin.getPlugin(HologramsPlugin.class).getServer().getWorld(worldName), x, y, z);
    }

    public static void saveAllHolograms() {
        for (Hologram hologram : holograms) {
            saveHologramLocation(hologram.getName(), hologram.getLocation());
        }
    }

    public static void reloadHolograms() {
        for (Hologram hologram : holograms) {
            hologram.clearHologram();
        }
        holograms.clear();

        loadHologramsFromConfig();
    }
}