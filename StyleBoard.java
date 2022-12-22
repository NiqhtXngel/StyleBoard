package net.niqhtxngel.styleboard.styleboard;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;

public class StyleBoard extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("scoreboardreload").setExecutor(this);

        saveDefaultConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        updateScoreboard(player);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("scoreboardreload")) {
            reloadConfig();

            if (sender instanceof Player) {
                updateScoreboard((Player) sender);
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateScoreboard(player);
                }
            }

            sender.sendMessage(ChatColor.GREEN + "Scoreboard configuration reloaded.");
            return true;
        }

        return false;
    }

    private void updateScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        Scoreboard scoreboard = manager.getNewScoreboard();

        String name = PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', getConfig().getString("name")));

        Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy", ChatColor.translateAlternateColorCodes('&', getConfig().getString("name")));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<String> lines = getConfig().getStringList("lines");

        for (int i = lines.size() - 1; i >=0; i--) {

            String text = PlaceholderAPI.setPlaceholders(player, lines.get(i));

            objective.getScore(ChatColor.translateAlternateColorCodes('&', text)).setScore(i);
        }

        player.setScoreboard(scoreboard);
    }
}
