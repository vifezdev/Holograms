package lol.vifez.holograms;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lol.vifez.holograms.api.Hologram;
import.lol.vifez.holograms.api.HologramsAPI;
import lol.vifez.holograms.util.CC;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@CommandAlias("hologram|holo")
public class HologramsCommand extends BaseCommand {

    private final HologramsPlugin plugin;

    public HologramsCommand(HologramsPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    @CommandPermission("hologram.command")
    public void onDefault(CommandSender sender) {
        sender.sendMessage(" ");
        sender.sendMessage(CC.translate("&b&lHolograms Commands " + "&7[&b" + plugin.getDescription().getVersion() + "&7]"));
        sender.sendMessage(" ");
        sender.sendMessage(CC.translate("&7* &b/hologram create &7<name> <line>"));
        sender.sendMessage(CC.translate("&7* &b/hologram addLine &7<hologramName> <line>"));
        sender.sendMessage(CC.translate("&7* &b/hologram setLine &7<hologramName> <lineNumber> <line>"));
        sender.sendMessage(CC.translate("&7* &b/hologram removeLine &7<hologramName> <lineNumber>"));
        sender.sendMessage(CC.translate("&7* &b/hologram movehere &7<hologramName>"));
        sender.sendMessage(CC.translate("&7* &b/hologram reload"));
        sender.sendMessage(CC.translate("&7* &b/hologram list"));
        sender.sendMessage(" ");
    }

    @Subcommand("create")
    @Description("Create a new hologram")
    @CommandCompletion("<name> <line>")
    @CommandPermission("hologram.command.create")
    public void onCreateHologram(Player player, String name, String line) {
        List<String> lines = Arrays.asList(CC.translate(line));
        Hologram hologram = HologramsAPI.createHologram(name, player.getLocation().add(0, 2, 0));
        hologram.setLines(lines);

        player.sendMessage(CC.translate("&aHologram '" + name + "' created successfully with line: " + line));
    }

    @Subcommand("addLine")
    @Description("Add a line to an existing hologram")
    @CommandCompletion("<hologramName> <line>")
    @CommandPermission("hologram.command.addline")
    public void onAddLine(Player player, String name, String line) {
        Hologram hologram = HologramsAPI.getHologram(name);

        if (hologram == null) {
            player.sendMessage(CC.translate("&cHologram '" + name + "' not found."));
            return;
        }

        hologram.addLine(CC.translate(line));
        player.sendMessage(CC.translate("&aLine added to hologram '" + name + "'"));
    }

    @Subcommand("setLine")
    @Description("Set a specific line on an existing hologram")
    @CommandCompletion("<hologramName> <lineNumber> <line>")
    @CommandPermission("hologram.command.setline")
    public void onSetLine(Player player, String name, int lineNumber, String line) {
        Hologram hologram = HologramsAPI.getHologram(name);

        if (hologram == null) {
            player.sendMessage(CC.translate("&cHologram '" + name + "' not found."));
            return;
        }

        List<ArmorStand> lines = hologram.getArmorStands();
        if (lineNumber < 1 || lineNumber > lines.size()) {
            player.sendMessage(CC.translate("&cInvalid line number."));
            return;
        }

        lines.get(lineNumber - 1).setCustomName(CC.translate(line));
        player.sendMessage(CC.translate("&aLine " + lineNumber + " updated in hologram '" + name + "'"));
    }

    @Subcommand("movehere")
    @Description("Move a hologram to your current location")
    @CommandCompletion("<hologramName>")
    @CommandPermission("hologram.command.movehere")
    public void onMoveHologramHere(Player player, String name) {
        Hologram hologram = HologramsAPI.getHologram(name);

        if (hologram == null) {
            player.sendMessage(CC.translate("&cHologram '" + name + "' not found."));
            return;
        }

        Location newLocation = player.getLocation();
        hologram.setLocation(newLocation);
        hologram.clearHologram();
        hologram.setLines(hologram.getLines());

        HologramsAPI.saveHologramLocation(name, newLocation);

        player.sendMessage(CC.translate("&aHologram '" + name + "' has been moved to your location!"));
    }

    @Subcommand("removeLine")
    @Description("Remove a line from an existing hologram")
    @CommandCompletion("<hologramName> <lineNumber>")
    @CommandPermission("hologram.command.removeline")
    public void onRemoveLine(Player player, String name, int lineNumber) {
        Hologram hologram = HologramsAPI.getHologram(name);

        if (hologram == null) {
            player.sendMessage(CC.translate("&cHologram '" + name + "' not found."));
            return;
        }

        List<ArmorStand> lines = hologram.getArmorStands();
        if (lineNumber < 1 || lineNumber > lines.size()) {
            player.sendMessage(CC.translate("&cInvalid line number."));
            return;
        }

        ArmorStand armorStand = lines.get(lineNumber - 1);
        armorStand.remove();
        hologram.getArmorStands().remove(armorStand);

        hologram.setLines(hologram.getLines());

        player.sendMessage(CC.translate("&aLine " + lineNumber + " has been removed from hologram '" + name + "'"));
    }

    @Subcommand("list")
    @Description("List all existing holograms")
    @CommandPermission("hologram.command.list")
    public void onListHolograms(Player player) {
        List<Hologram> holograms = HologramsAPI.getHolograms();

        if (holograms.isEmpty()) {
            player.sendMessage(CC.translate("&cNo holograms found."));
            return;
        }

        StringBuilder hologramList = new StringBuilder();
        hologramList.append(CC.translate("&b&lHOLOGRAMS:"));

        for (Hologram hologram : holograms) {
            Location location = hologram.getLocation();
            String formattedLocation = CC.translate(
                    String.format("&7* &b%s &7(%.2f, %.2f, %.2f, %s)",
                            hologram.getName(),
                            location.getX(),
                            location.getY(),
                            location.getZ(),
                            location.getWorld().getName())
            );
            hologramList.append("\n").append(formattedLocation);
        }

        player.sendMessage(hologramList.toString());
    }


    @Subcommand("delete")
    @Description("Delete an existing hologram")
    @CommandPermission("hologram.command.delete")
    public void onDeleteHologram(Player player, String name) {
        Hologram hologram = HologramsAPI.getHologram(name);

        if (hologram == null) {
            player.sendMessage(CC.translate("&cHologram '" + name + "' not found."));
            return;
        }

        HologramsAPI.removeHologram(name);

        player.sendMessage(CC.translate("&aHologram '" + name + "' has been deleted."));
    }



    @Subcommand("reload")
    @Description("Reload all holograms from the config")
    @CommandPermission("hologram.command.reload")
    public void onReloadHolograms(Player player) {
        HologramsAPI.reloadHolograms();
        player.sendMessage(CC.translate("&aAll holograms have been reloaded from the config."));
    }
}