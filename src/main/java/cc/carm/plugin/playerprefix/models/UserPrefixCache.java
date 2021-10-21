package cc.carm.plugin.playerprefix.models;

import cc.carm.plugin.playerprefix.Main;
import cc.carm.plugin.playerprefix.utils.MessageParser;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class UserPrefixCache {

    UUID uuid;

    private final File datafile;
    private FileConfiguration dataConfiguration;

    boolean fileLoaded;

    private final Map<Integer, String> prefixes;
    private int usingPrefix;
    private boolean enablePrefix;


    public UserPrefixCache(UUID uuid) {
        this.uuid = uuid;
        File userdataFolder = new File(Main.getInstance().getDataFolder() + "/userdata");
        if (!userdataFolder.isDirectory() || !userdataFolder.exists()) {
            userdataFolder.mkdir();
        }
        this.datafile = new File(userdataFolder, this.uuid + ".yml");

        this.fileLoaded = datafile.exists();


        this.prefixes = new HashMap<>();
        this.usingPrefix = 0;

        if (!fileLoaded) {
            checkFile();
            setEnablePrefix(true);
        } else {
            this.dataConfiguration = YamlConfiguration.loadConfiguration(datafile);

            readPrefix();
        }


    }

    public Map<Integer, String> getPrefixes() {
        return prefixes;
    }


    public void readPrefix() {
        try {
            Set<String> idString = getDataConfiguration().getConfigurationSection("prefixes.list").getKeys(false);
            for (String s : idString) {
                try {
                    int id = Integer.parseInt(s);
                    String prefix = getDataConfiguration().getString("prefixes.list." + s);
                    getPrefixes().put(id, prefix);
                } catch (Exception e) {
                    Main.getInstance().getLogger().log(Level.WARNING, "前缀加载有误 " + uuid + ".yml -> prefixes.list." + s);
                }
            }
        } catch (Exception ignore) {
        }

        int usingPrefixData = getDataConfiguration().getInt("prefixes.using", 0);

        if (getPrefixes().containsKey(usingPrefixData)) {
            this.usingPrefix = usingPrefixData;
        }

        this.enablePrefix = getDataConfiguration().getBoolean("prefixes.enable", true);


    }

    public String getUsingPrefix() {
        return MessageParser.parseColor(getPrefixes().getOrDefault(this.usingPrefix, ""));
    }

    public void setUsingPrefix(int id) {
        if (getPrefixes().containsKey(id)) {
            this.usingPrefix = id;
            checkFile();
            getDataConfiguration().set("prefixes.using", id);
        } else {
            this.usingPrefix = 0;
            checkFile();
            getDataConfiguration().set("prefixes.using", 0);
        }
        saveDatas();

    }

    public void setEnablePrefix(boolean toggle) {
        this.enablePrefix = toggle;
        checkFile();
        getDataConfiguration().set("prefixes.enable", toggle);
        saveDatas();
    }

    public boolean isEnabledPrefix() {
        return this.enablePrefix;
    }

    private void checkFile() {
        if (!datafile.exists()) {
            try {
                datafile.createNewFile();
            } catch (IOException ex) {
                Bukkit.getLogger().info("Could not load file " + "/userdata/" + "yml" + ex);
            }
        }
        if (!fileLoaded) {
            this.dataConfiguration = YamlConfiguration.loadConfiguration(datafile);
            this.fileLoaded = true;
        }
    }

    public void addPrefix(String prefix) {
        int id = getPrefixes().size() > 0 ? Collections.max(getPrefixes().keySet()) + 1 : 1;
        getPrefixes().put(id, prefix);
        checkFile();
        getDataConfiguration().set("prefixes.list." + id, prefix);
        saveDatas();
    }

    public void removePrefix(int prefixID) {
        if (this.usingPrefix == prefixID) {
            setUsingPrefix(0);
        }
        getPrefixes().remove(prefixID);
        checkFile();
        getDataConfiguration().set("prefixes.list." + prefixID, null);
        saveDatas();
    }

    public boolean isFileLoaded() {
        return fileLoaded;
    }

    public FileConfiguration getDataConfiguration() {
        return this.dataConfiguration;
    }

    public void saveDatas() {
        try {
            getDataConfiguration().save(datafile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
