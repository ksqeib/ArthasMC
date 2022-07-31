package com.serverct.plugin.tool.arthas;

import com.taobao.arthas.core.server.ArthasBootstrap;
import net.bytebuddy.agent.ByteBuddyAgent;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.jar.JarFile;

public class AgentLoader {
    private Instrumentation instrumentation;

    public void initInstrumentation() throws Exception {
        instrumentation = ByteBuddyAgent.install();
    }

    public void load(Map<String, String> configMap){
        Object bootstrap;
        boolean isBind = false;
        Exception exception = null;
        try {
            bootstrap = ArthasBootstrap.class.getMethod("getInstance", Instrumentation.class, Map.class).invoke(null,
                    instrumentation, configMap);
            isBind = (Boolean) ArthasBootstrap.class.getMethod("isBind").invoke(bootstrap);
        } catch (Exception ex) {
            exception = ex;
        }

        if (!isBind) {
            String errorMsg = "Arthas server port binding failed! Please check ./logs/arthas/arthas.log for more details.";
            Bootloader.logSevere(errorMsg);
            if (exception != null)
                throw new RuntimeException(exception);
        }
    }

    public void spyInit(File spyJarFile) throws IOException {
        instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(spyJarFile));
    }

}
