package fr.robot.robottags.utility.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class Config {

    private final Plugin main;
    private final String name;

    private File database = null;
    private FileConfiguration databaseConfig = null;

    public Config(Plugin main, String name) {
        this.main = main;
        this.name = name;
    }

    public void setup() {
        if(this.database == null)
            database = new File(main.getDataFolder(), name + ".yml");
        if(!database.exists()) {
            if(database.getParentFile().exists())
                database.getParentFile().mkdir();
            main.saveResource(name + ".yml", false);
        }
    }

    public FileConfiguration get() {
        if(databaseConfig == null)
            reload();
        return databaseConfig;
    }

    public void save() {
        if(database == null || databaseConfig == null)
            return;
        try {
            get().save(database);
        } catch (IOException e) {
            main.getLogger().log(Level.SEVERE, "Erreur lors de la sauvegarde de la configuration " + name + ".yml");
        }
    }

    public void reload() {
        if(this.database == null)
            database = new File(main.getDataFolder(), name + ".yml");
        this.databaseConfig = YamlConfiguration.loadConfiguration(database);

        InputStream defaultStream = main.getResource(name + ".yml");
        if(defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.databaseConfig.setDefaults(defaultConfig);
        } else {
            try {
                database.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
