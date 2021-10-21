package cc.carm.plugin.playerprefix;

import cc.carm.plugin.playerprefix.commands.PlayerPrefixCommand;
import cc.carm.plugin.playerprefix.hooker.PlayerPrefixExpansion;
import cc.carm.plugin.playerprefix.listeners.PrefixListener;
import cc.carm.plugin.playerprefix.managers.UserPrefixManager;
import cc.carm.plugin.playerprefix.utils.MessageParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return Main.instance;
    }


    @Override
    public void onEnable() {
        instance = this;

        log(getName() + " " + getDescription().getVersion() + " &7开始加载...");
        long startTime = System.currentTimeMillis();

        log("启用前缀管理器...");
        UserPrefixManager.init();

        log("注册指令...");
        this.getCommand("PlayerPrefix").setExecutor(new PlayerPrefixCommand());
        this.getCommand("PlayerPrefix").setTabCompleter(new PlayerPrefixCommand());

        log("注册监听器...");
        Bukkit.getPluginManager().registerEvents(new PrefixListener(), this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            log("注册变量...");
            new PlayerPrefixExpansion(getInstance()).register();
        } else {
            log("未安装 PlaceholderAPI 不进行变量注册...");
            log("若您想使用变量进行前缀的显示，请安装PlaceholderAPI！");
        }

        log("加载完成 ，共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(MessageParser.parseColor("[" + getInstance().getName() + "] " + message));
    }

}
