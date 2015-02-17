package io.github.cnly.RedPackets.RedPackets;

import io.github.Cnly.Crafter.Crafter.utils.CompatUtils;
import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RedPacketBroadcastTask extends BukkitRunnable
{
    
    private static final String PREFIX = ChatColor.RED + "[红包君] ";
    private Configs configs = Main.getInstance().getConfigs();
    
    @Override
    public void run()
    {
        
        String code = this.configs.getRandomRedPacketCode();
        if (null == code)
            return;
        
        FancyMessage msg = new FancyMessage(PREFIX).then("壕 ")
                .color(ChatColor.RED).then(this.configs.getOwner(code))
                .then(" 发的一个红包还没有被抢完！").color(ChatColor.RED).then("点此立即去抢")
                .color(ChatColor.RED).style(ChatColor.UNDERLINE)
                .command("/redpackets get " + code).then("！")
                .color(ChatColor.RED);
        
        for (Player p : CompatUtils.getOnlinePlayers())
        {
            
            if (this.configs.hasTaken(p, code))
                continue;
            
            msg.send(p);
            
        }
        
    }
    
}
