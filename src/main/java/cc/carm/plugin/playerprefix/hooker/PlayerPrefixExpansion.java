package cc.carm.plugin.playerprefix.hooker;

import cc.carm.plugin.playerprefix.managers.UserPrefixManager;
import cc.carm.plugin.playerprefix.models.UserPrefixCache;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerPrefixExpansion extends PlaceholderExpansion {

    JavaPlugin plugin;

    public PlayerPrefixExpansion(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        List<String> placeholders = new ArrayList<>();
        placeholders.add("%PlayerPrefix%");
        return placeholders;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getDescription().getName();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return "加载中...";

        UserPrefixCache cache = UserPrefixManager.getData(player.getUniqueId());

        return cache.getUsingPrefix();
    }

}
