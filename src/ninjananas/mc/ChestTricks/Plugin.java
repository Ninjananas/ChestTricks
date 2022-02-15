package ninjananas.mc.ChestTricks;

import org.bukkit.plugin.java.JavaPlugin;


public class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
    	this.getCommand("autostore").setExecutor(new AutoStoreCommand(this));
    }

    @Override
    public void onDisable() {
    }
}
