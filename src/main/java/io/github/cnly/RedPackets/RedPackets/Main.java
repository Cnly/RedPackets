package io.github.cnly.RedPackets.RedPackets;

import io.github.Cnly.Crafter.Crafter.framework.commands.CrafterMainCommand;
import io.github.Cnly.Crafter.Crafter.framework.notifies.AbstractFutureNotifier;
import io.github.Cnly.Crafter.Crafter.framework.notifies.BootCompleteNotifier;
import io.github.cnly.RedPackets.RedPackets.commands.GetCommand;
import io.github.cnly.RedPackets.RedPackets.commands.NewCommand;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
    
    private static Main instance;
    
    private Configs configs;
    
    public static Economy economy;
    
    @Override
    public void onEnable()
    {
        
        instance = this;
        
        AbstractFutureNotifier bcn = new BootCompleteNotifier(this)
                .setMessage("插件未正常启动！请检查报错信息！");
        
        this.configs = new Configs();
        
        if (!this.setupEconomy())
        {
            getLogger().severe("Vault not found or economy plugin not found!");
        }
        
        CrafterMainCommand mainCommand = new CrafterMainCommand(this);
        mainCommand.addSubcommand(new NewCommand());
        mainCommand.addSubcommand(new GetCommand());
        getCommand("redpackets").setExecutor(mainCommand);
        
        new RedPacketBroadcastTask().runTaskTimer(this, 0L, 1200L);
        
        bcn.cancel();
        
    }
    
    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTasks(this);
    }
    
    public static Main getInstance()
    {
        return instance;
    }
    
    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer()
                .getServicesManager().getRegistration(
                        net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null)
        {
            economy = economyProvider.getProvider();
        }
        
        return (economy != null);
    }
    
    public Configs getConfigs()
    {
        return configs;
    }
    
}