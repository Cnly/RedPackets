package io.github.cnly.RedPackets.RedPackets.commands;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.Cnly.Crafter.Crafter.framework.commands.AbstractCrafterCommand;
import io.github.cnly.RedPackets.RedPackets.Main;

public class GetCommand extends AbstractCrafterCommand
{
    
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(
            "#.##");
    
    public GetCommand()
    {
        this.setAction("get");
        this.setPermission("RedPackets.get");
        this.setPlayerNeeded(true);
        this.setHelp(ChatColor.RED + "/redpackets get <兑换码> - 抢红包！");
    }
    
    @Override
    protected void executeCommand(CommandSender sender, String[] args)
    {
        
        if (2 != args.length)
        {
            sender.sendMessage(this.getHelp());
            return;
        }
        
        Player p = (Player)sender;
        String code = args[1];
        
        if (!Main.getInstance().getConfigs().hasRedPacket(code))
        {
            p.sendMessage(ChatColor.GRAY + "这个红包不存在！");
            return;
        }
        
        if (Main.getInstance().getConfigs().hasTaken(p, code))
        {
            p.sendMessage(ChatColor.GRAY + "你已经领取过这个红包了，不能太贪心噢！");
            return;
        }
        
        double moneyGet = Main.getInstance().getConfigs().getRedPacket(p, code);
        Main.economy.depositPlayer(p, moneyGet);
        
        p.sendMessage(ChatColor.RED + "红包领取成功！");
        p.sendMessage(new StringBuilder(18).append(ChatColor.RED).append("你抢到了 ")
                .append(DECIMAL_FORMAT.format(moneyGet)).append(' ')
                .append(Main.economy.currencyNamePlural()).append('！').toString());
        
        return;
    }
    
}
