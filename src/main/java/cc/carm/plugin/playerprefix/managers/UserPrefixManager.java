package cc.carm.plugin.playerprefix.managers;

import cc.carm.plugin.playerprefix.Main;
import cc.carm.plugin.playerprefix.models.UserPrefixCache;
import cc.carm.plugin.playerprefix.utils.NamePrefix;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserPrefixManager {


    public static Map<UUID, UserPrefixCache> userDataCaches = new HashMap<>();

    public static void init() {
        File userdataFolder = new File(Main.getInstance().getDataFolder() + "/userdata");
        if (!userdataFolder.isDirectory() || !userdataFolder.exists()) {
            userdataFolder.mkdir();
        }
    }

    public static void updatePrefix(Player player) {
        UserPrefixCache playerCache = UserPrefixManager.getData(player.getUniqueId());

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            UserPrefixCache othersCache = UserPrefixManager.getData(onlinePlayer.getUniqueId());
            if (playerCache.isEnabledPrefix()) {
                NamePrefix.set(player, onlinePlayer, othersCache.getUsingPrefix());
            }
            if (othersCache.isEnabledPrefix()) {
                NamePrefix.set(onlinePlayer, player, playerCache.getUsingPrefix());
            }
        }
    }

    public static void clearPrefix(Player player) {

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            NamePrefix.reset(onlinePlayer, player);
            NamePrefix.reset(player, onlinePlayer);
        }
    }

    public static UserPrefixCache loadData(UUID uuid) {
        UserPrefixCache prefixCache = new UserPrefixCache(uuid);

        userDataCaches.put(uuid, prefixCache);

        return prefixCache;
    }

    public static void unloadData(UUID uuid) {
        userDataCaches.remove(uuid);
    }

    public static UserPrefixCache getData(UUID uuid) {
        return userDataCaches.getOrDefault(uuid, loadData(uuid));
    }

    public static boolean hasData(UUID uuid) {
        return userDataCaches.containsKey(uuid);
    }

    public static Map<UUID, UserPrefixCache> getUserDataCaches() {
        return userDataCaches;
    }
}
