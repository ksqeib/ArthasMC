package com.serverct.plugin.tool.arthas;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {

    private final FileConfiguration config;

    private final Map<String, String> configMap = new HashMap<>();

    public ConfigLoader(FileConfiguration config) {
        this.config = config;
    }

    public Map<String, String> getConfigMap() {
        configMap.clear();
        genConfigMap();
        return configMap;
    }

    private void genConfigMap() {
        ConfigurationSection serverConf = config.getConfigurationSection("serverConfigMap");
        for (String key : serverConf.getKeys(true)) {
            if (serverConf.get(key) instanceof MemorySection) continue;
            String value = serverConf.get(key).toString();
            configMap.put(key, value);
//            Bootloader.logInfo("Config:" + key + ":" + value);
        }
    }
}
