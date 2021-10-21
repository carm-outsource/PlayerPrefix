package cc.carm.plugin.playerprefix.listeners;

import cc.carm.plugin.playerprefix.managers.UserPrefixManager;
import cc.carm.plugin.playerprefix.models.UserPrefixCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PrefixListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        UserPrefixCache playerCache = UserPrefixManager.loadData(player.getUniqueId());
        UserPrefixManager.updatePrefix(player);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UserPrefixManager.clearPrefix(player);
        UserPrefixManager.unloadData(player.getUniqueId());
    }

}
