package lol.vifez.holograms.api;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private final String name;
    private Location location;
    private final List<ArmorStand> armorStands;

    public Hologram(String name, Location location) {
        this.name = name;
        this.location = location;
        this.armorStands = new ArrayList<>();
    }

    public void setLocation(Location location) {
        this.location = location;
        HologramsAPI.saveHologramLocation(name, location);
    }

    public List<String> getLines() {
        List<String> lines = new ArrayList<>();
        for (ArmorStand armorStand : armorStands) {
            lines.add(armorStand.getCustomName());
        }
        return lines;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public List<ArmorStand> getArmorStands() {
        return armorStands;
    }

    public void setLines(List<String> lines) {
        clearHologram();
        double yOffset = 0.0;

        for (String line : lines) {
            ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, yOffset, 0), EntityType.ARMOR_STAND);
            armorStand.setCustomName(line);
            armorStand.setCustomNameVisible(true);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStands.add(armorStand);

            yOffset -= 0.25;
        }

        HologramsAPI.saveHologramLocation(name, location);
    }

    public void addLine(String line) {
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, armorStands.size() * -0.25, 0), EntityType.ARMOR_STAND);
        armorStand.setCustomName(line);
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStands.add(armorStand);

        HologramsAPI.saveHologramLocation(name, location);
    }

    public void clearHologram() {
        for (ArmorStand armorStand : armorStands) {
            armorStand.remove();
        }
        armorStands.clear();
    }
}