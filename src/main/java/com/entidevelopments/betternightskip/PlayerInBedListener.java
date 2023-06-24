package com.entidevelopments.betternightskip;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class PlayerInBedListener implements Listener, CommandExecutor {
    boolean skipAtOne = true;
    int sleepingPlayers = 0;

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if (player.getServer().getOnlinePlayers().size() == 1) {
            return;
        }
        if (event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)) {
            if (skipAtOne) {
                Bukkit.getServer().broadcastMessage("§l[Server]§r " + player.getName() + " schläft! Die Nacht wird übersprungen! Ändere dies mit /bns disable");
            } else {
                Bukkit.getServer().broadcastMessage("§l[Server]§r " + player.getName() + " schläft! Es müssen alle Spieler schlafen! Ändere dies mit /bns enable");
                return;
            }
            sleepingPlayers++;
            if (sleepingPlayers > 1) {
                return;
            }
        } else {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isSleeping()) {
                    if (skipAtOne) {
                        Bukkit.getServer().getWorld("world").setTime(0);
                        sleepingPlayers = 0;
                        Bukkit.getServer().broadcastMessage("§l[Server]§r Die Nacht wurde übersprungen!");
                    } else {
                        cancel();
                    }
                } else {
                    if (skipAtOne) {
                        Bukkit.getServer().broadcastMessage("§l[Server]§r " + player.getName() + " ist aufgewacht! Die Nacht wird nicht übersprungen!");
                    }
                }
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("BetterNightSkip"), 100L);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
                if (args.length == 0) {
                    sender.sendMessage("§l[Server]§r Benutze /bns <enable/disable>");
                    return true;
                }

                if (args[0].equalsIgnoreCase("enable")) {
                    skipAtOne = true;
                    sender.sendMessage("§l[Server]§r" + sender.getName() + "hat die Settings geändert. Die Nacht wird übersprungen, wenn ein Spieler schläft!");
                    return true;
                }

                if (args[0].equalsIgnoreCase("disable")) {
                    skipAtOne = false;
                    sender.sendMessage("§l[Server]§r" + sender.getName() + "hat die Settings geändert. Die Nacht wird erst übersprungen, wenn alle Spieler schlafen!");
                    return true;
                }

                sender.sendMessage("§l[Server]§r Benutze /bns <enable/disable>");
        } else {
                sender.sendMessage("§l[Server]§r Dieser Befehl kann nur von einem Spieler ausgeführt werden!");
        }
        return true;
    }

}
