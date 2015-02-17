package io.github.cnly.RedPackets.RedPackets;

import java.io.File;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import io.github.Cnly.Crafter.Crafter.framework.configs.CrafterYamlConfigManager;

public class Configs
{
    
    private CrafterYamlConfigManager redPacketConfig = new CrafterYamlConfigManager(
            new File(Main.getInstance().getDataFolder(), "redPackets.yml"),
            false, Main.getInstance());
    
    public boolean hasRedPacket(String code)
    {
        return this.redPacketConfig.isSet("redPackets." + code);
    }
    
    public boolean hasTaken(Player p, String code)
    {
        
        UUID pid = p.getUniqueId();
        
        return this.redPacketConfig.getStringList(
                "redPackets." + code + ".taken").contains(pid.toString());
    }
    
    public double getRedPacket(Player p, String code)
    {
        
        UUID pid = p.getUniqueId();
        
        ConfigurationSection singleRedPacketSection = this.redPacketConfig
                .getConfigurationSection("redPackets." + code);
        
        int left = singleRedPacketSection.getInt("left") - 1;
        double totalMoney = singleRedPacketSection.getDouble("total");
        double devide = singleRedPacketSection.getDouble("devide");
        double moneyLeft = singleRedPacketSection.getDouble("moneyLeft");
        double moneyGet = 0;
        Random r = new Random();
        
        if (0 >= left)
        {
            
            moneyGet = moneyLeft;
            
            this.redPacketConfig.getYamlConfig()
                    .set("redPackets." + code, null);
            
        }
        else
        {
            
            double avgMoney = totalMoney / devide;
            do
            {
                
                if (r.nextInt(10) < 5)
                    moneyGet = avgMoney - (r.nextDouble() * 20);
                else
                    moneyGet = avgMoney + (r.nextDouble() * 20);
                
            }
            while (moneyLeft - moneyGet < 0 || moneyGet <= 0);
            
            moneyLeft -= moneyGet;
            
            singleRedPacketSection.set("left", left);
            singleRedPacketSection.set("moneyLeft", moneyLeft);
            this.redPacketConfig.addToList("redPackets." + code + ".taken",
                    pid.toString());
            
        }
        
        this.redPacketConfig.save();
        
        return moneyGet;
    }
    
    public String saveRedPacket(String owner, double totalMoney, double devide)
    {
        
        String code;
        String basePath;
        do
        {
            code = this.getRandomCode();
            basePath = "redPackets." + code;
        }
        while (this.redPacketConfig.isSet(basePath));
        
        ConfigurationSection singleRedPacketSection = this.redPacketConfig
                .getYamlConfig().createSection(basePath);
        
        singleRedPacketSection.set("owner", owner);
        singleRedPacketSection.set("total", totalMoney);
        singleRedPacketSection.set("moneyLeft", totalMoney);
        singleRedPacketSection.set("devide", devide);
        singleRedPacketSection.set("left", devide);
        singleRedPacketSection.set("taken", Collections.EMPTY_LIST);
        
        this.redPacketConfig.save();
        
        return code;
    }
    
    private String getRandomCode()
    {
        
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++)
            sb.append((char)(r.nextInt(26) + 97));
        
        return sb.toString();
    }
    
    public String getOwner(String code)
    {
        return this.redPacketConfig.getString("redPackets." + code + ".owner");
    }
    
    public String getRandomRedPacketCode()
    {
        
        Set<String> codes = this.redPacketConfig.getConfigurationSection(
                "redPackets").getKeys(false);
        
        if (0 == codes.size())
            return null;
        
        String[] tmp = new String[codes.size()];
        
        return codes.toArray(tmp)[new Random().nextInt(codes.size())];
    }
}