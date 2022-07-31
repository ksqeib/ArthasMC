package com.serverct.plugin.tool.arthas;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

public class Bootloader extends JavaPlugin {
    private static Logger pluginLogger;
    AgentLoader agentLoader = new AgentLoader();
    ConfigLoader configLoader;

    @Override
    public void onEnable() {
        pluginLogger = getLogger();

//        保存logback
        File spyJar=getOrSaveFile("arthas-spy.IJar");
        getOrSaveFile("logback.xml");
//        加载配置文件Map
        configLoader = new ConfigLoader(loadYamlFile("config.yml"));
        Map<String, String> configMap = configLoader.getConfigMap();
//        初始化一下instrumentation
        try {
            agentLoader.initInstrumentation();
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().severe("Load instrumentation fail!");
            return;
        }
        try {
            agentLoader.spyInit(spyJar);
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().severe("Init spy fail!");
            return;
        }
        agentLoader.load(configMap);
    }

    private FileConfiguration loadYamlFile(String path) {

        return YamlConfiguration.loadConfiguration(getOrSaveFile(path));
    }

    private File getOrSaveFile(String path) {
        //加载资源的方法
        //防止不存在 或者二次保存造成报错
        File file = new File(getDataFolder(), path);
        if (!file.exists())
            saveResource(path, false);
        return file;
    }

    public static void logSevere(String msg) {
        pluginLogger.severe(msg);
    }

    public static void logInfo(String msg) {
        pluginLogger.info(msg);
    }
}
