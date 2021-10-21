package cc.carm.plugin.playerprefix.commands;

import cc.carm.plugin.playerprefix.managers.UserPrefixManager;
import cc.carm.plugin.playerprefix.models.UserPrefixCache;
import cc.carm.plugin.playerprefix.utils.MessageParser;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerPrefixCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            String aim = args[0];
            if (aim.equalsIgnoreCase("list")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("只有玩家才可使用此指令！");
                    return true;
                }

                Player player = (Player) sender;
                UserPrefixCache cache = UserPrefixManager.getData(player.getUniqueId());
                Map<Integer, String> prefixs = cache.getPrefixes();
                if (prefixs.size() < 1) {
                    sender.sendMessage("§f抱歉，但您当前暂无前缀！");
                    return true;
                }
                sender.sendMessage("§f您当前有以下前缀：");
                sender.sendMessage("§8| §70 §f(无)");
                prefixs.forEach((integer, s1) -> {
                    sender.sendMessage("§8| §7" + integer + " §f" + MessageParser.parseColor(s1));
                });
                sender.sendMessage("§f您可以输入 “§a/prefix set <ID> §f” 来更换您的前缀。");
                return true;
            } else if (aim.equalsIgnoreCase("toggle")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("只有玩家才可使用此指令！");
                    return true;
                }

                Player player = (Player) sender;
                UserPrefixCache cache = UserPrefixManager.getData(player.getUniqueId());
                cache.setEnablePrefix(!cache.isEnabledPrefix());
                if (cache.isEnabledPrefix()) {
                    UserPrefixManager.updatePrefix(player);
                } else {
                    UserPrefixManager.clearPrefix(player);
                }
                sender.sendMessage("当前前缀显示状态： " + (cache.isEnabledPrefix() ? "§a开启" : "§c关闭"));

            } else if (aim.equalsIgnoreCase("admin")) {
                if (sender.hasPermission("prefix.admin")) {
                    helpAdmin(sender);
                }
                return true;
            }
        } else if (args.length == 2) {
            String aim = args[0];
            if (aim.equalsIgnoreCase("list")) {
                if (!sender.hasPermission("prefix.admin")) {
                    return true;
                }

                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
                UUID playerUUID = targetPlayer.getUniqueId();
                UserPrefixCache cache = UserPrefixManager.loadData(playerUUID);
                sender.sendMessage("§f" + targetPlayer.getName() + "当前有以下前缀：");
                Map<Integer, String> prefixs = cache.getPrefixes();
                prefixs.forEach((integer, s1) -> {
                    sender.sendMessage("§8| §7" + integer + " §f" + MessageParser.parseColor(s1));
                });
                UserPrefixManager.unloadData(playerUUID);
                return true;
            } else if (aim.equalsIgnoreCase("set")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("只有玩家才可使用此指令！");
                    return true;
                }

                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    sender.sendMessage("提供的ID错误，请您输入/prefix list 查看您所拥有的前缀。");
                    return true;
                }

                Player player = (Player) sender;
                UserPrefixCache cache = UserPrefixManager.getData(player.getUniqueId());

                if (id == 0) {
                    cache.setUsingPrefix(id);
                    sender.sendMessage("您已摘下前缀。");
                    UserPrefixManager.updatePrefix(player);
                } else if (cache.getPrefixes().containsKey(id)) {
                    cache.setUsingPrefix(id);
                    sender.sendMessage("您当前的前缀已更新为：" + cache.getUsingPrefix());
                    UserPrefixManager.updatePrefix(player);
                } else {
                    sender.sendMessage("§c修改失败！§f您未拥有该ID的前缀。");
                    sender.sendMessage("您可以输入/prefix list 查看您所拥有的前缀。");
                }
                return true;
            } else {
                help(sender);
                return true;
            }
        } else if (args.length == 3) {
            if (!sender.hasPermission("prefix.admin")) {
                return true;
            }


            String aim = args[0];
            if (aim.equalsIgnoreCase("add")) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
                UUID playerUUID = targetPlayer.getUniqueId();
                UserPrefixCache cache = UserPrefixManager.loadData(playerUUID);
                cache.addPrefix(args[2]);
                UserPrefixManager.unloadData(playerUUID);
                sender.sendMessage("成功为玩家 " + targetPlayer.getPlayer() + " 添加前缀：" + args[2]);
                return true;

            } else if (aim.equalsIgnoreCase("remove")) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
                UUID playerUUID = targetPlayer.getUniqueId();
                UserPrefixCache cache = UserPrefixManager.loadData(playerUUID);

                int id;
                try {
                    id = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    sender.sendMessage("提供的ID错误，可以输入/prefix list <玩家名>查看该玩家所拥有的前缀。");
                    return true;
                }

                if (cache.getPrefixes().containsKey(id)) {
                    sender.sendMessage("已移除ID为 " + id + " 的前缀:" + cache.getPrefixes().get(id));
                    cache.removePrefix(id);
                    if (targetPlayer.isOnline()) {
                        UserPrefixManager.updatePrefix(Bukkit.getPlayer(playerUUID));
                    }
                } else {
                    sender.sendMessage("§c移除失败！§f该玩家未拥有该前缀ID。");
                    sender.sendMessage("您可以输入/prefix list <玩家名> 查看该玩家所拥有的前缀。");
                }

                UserPrefixManager.unloadData(playerUUID);
                return true;
            } else if (aim.equalsIgnoreCase("set")) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
                UUID playerUUID = targetPlayer.getUniqueId();
                UserPrefixCache cache = UserPrefixManager.loadData(playerUUID);

                int id;
                try {
                    id = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    sender.sendMessage("提供的ID错误，可以输入/prefix list <玩家名>查看该玩家所拥有的前缀。");
                    return true;
                }

                if (cache.getPrefixes().containsKey(id)) {
                    sender.sendMessage("成功设置玩家 " + targetPlayer.getName() + " 的前缀为" + cache.getPrefixes().get(id));
                    cache.setUsingPrefix(id);
                    if (targetPlayer.isOnline()) {
                        UserPrefixManager.updatePrefix(Bukkit.getPlayer(playerUUID));
                    }
                } else {
                    sender.sendMessage("§c设置失败！§f该玩家未拥有该前缀ID。");
                    sender.sendMessage("您可以输入/prefix list <玩家名> 查看该玩家所拥有的前缀。");
                }
                UserPrefixManager.unloadData(playerUUID);
                return true;
            } else if (aim.equalsIgnoreCase("copy")) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
                UUID playerUUID = targetPlayer.getUniqueId();
                UserPrefixCache cache = UserPrefixManager.loadData(playerUUID);


                OfflinePlayer sourcePlayer = Bukkit.getOfflinePlayer(args[2]);
                UUID sourceUUID = sourcePlayer.getUniqueId();
                UserPrefixCache sourceCache = UserPrefixManager.loadData(sourceUUID);

                if (sourceCache.getPrefixes().size() > 0) {
                    for (Integer prefixID : cache.getPrefixes().keySet()) {
                        cache.removePrefix(prefixID);
                    }
                    for (String value : sourceCache.getPrefixes().values()) {
                        cache.addPrefix(value);
                    }
                    sender.sendMessage("成功复制玩家 " + sourcePlayer.getName() + " 的前缀到" + targetPlayer.getPlayer());
                } else {
                    sender.sendMessage("§c复制失败！§f目标玩家的前缀列表为空。");
                }
                UserPrefixManager.unloadData(playerUUID);
                UserPrefixManager.unloadData(sourceUUID);
                return true;
            } else if (aim.equalsIgnoreCase("addall")) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
                UUID playerUUID = targetPlayer.getUniqueId();
                UserPrefixCache cache = UserPrefixManager.loadData(playerUUID);


                OfflinePlayer sourcePlayer = Bukkit.getOfflinePlayer(args[2]);
                UUID sourceUUID = sourcePlayer.getUniqueId();
                UserPrefixCache sourceCache = UserPrefixManager.loadData(sourceUUID);

                if (sourceCache.getPrefixes().size() > 0) {
                    for (String value : sourceCache.getPrefixes().values()) {
                        cache.addPrefix(value);
                    }
                    sender.sendMessage("成功添加玩家 " + sourcePlayer.getName() + " 的所有前缀到" + targetPlayer.getPlayer());
                } else {
                    sender.sendMessage("§c添加失败！§f目标玩家的前缀列表为空。");
                }
                UserPrefixManager.unloadData(playerUUID);
                UserPrefixManager.unloadData(sourceUUID);
                return true;
            } else {
                helpAdmin(sender);
                return true;
            }
        } else {
            help(sender);
            return true;
        }
        return true;
    }


    public static void help(CommandSender sender) {
        sender.sendMessage("§a§l前缀系统 §f指令列表");
        sender.sendMessage("§f/prefix list §7列出所有前缀");
        sender.sendMessage("§f/prefix toggle §7开关前缀显示");
        sender.sendMessage("§f/prefix set <ID> §7设置使用的前缀");
    }

    public static void helpAdmin(CommandSender sender) {
        sender.sendMessage("§a§l前缀系统 §f管理员指令列表");
        sender.sendMessage("§f/prefix list <玩家名> §7列出所有前缀");
        sender.sendMessage("§f/prefix add <玩家名> <前缀> §7添加一个前缀");
        sender.sendMessage("§f/prefix remove <玩家名> <ID> §7移除一个前缀");
        sender.sendMessage("§f/prefix set <玩家名> <ID> §7设置其使用的前缀");
        sender.sendMessage("§f/prefix addall <玩家名> <源玩家名>");
        sender.sendMessage("§8  - §7添加所有源玩家的前缀到目标玩家");
        sender.sendMessage("§f/prefix copy <玩家名> <源玩家名>");
        sender.sendMessage("§8  - §7设置目标玩家的前缀列表为源玩家的前缀列表");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1: {
                List<String> completions = new ArrayList<>();
                List<String> strings = new ArrayList<>();
                strings.add("list");
                strings.add("toggle");
                strings.add("set");
                if (sender.hasPermission("prefix.admin")) {
                    strings.add("add");
                    strings.add("remove");
                    strings.add("copy");
                    strings.add("addall");
                }
                for (String s : strings) {
                    if (StringUtil.startsWithIgnoreCase(s, args[0].toLowerCase())) {
                        completions.add(s);
                    }
                }
                return completions;
            }
            case 2: {
                if (!sender.hasPermission("prefix.admin")) {
                    return ImmutableList.of();
                }
                String aim = args[1];
                if (aim.equalsIgnoreCase("add") || aim.equalsIgnoreCase("remove") || aim.equalsIgnoreCase("list")) {
                    List<String> completions = new ArrayList<>();
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        if (StringUtil.startsWithIgnoreCase(pl.getName(), args[1].toLowerCase())) {
                            completions.add(pl.getName());
                        }
                    }
                    return completions;
                }
            }
            default:
                return ImmutableList.of();
        }
    }

}
