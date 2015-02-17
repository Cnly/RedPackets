package io.github.cnly.RedPackets.RedPackets.commands;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.Cnly.Crafter.Crafter.framework.commands.AbstractCrafterCommand;
import io.github.cnly.RedPackets.RedPackets.Main;

public class NewCommand extends AbstractCrafterCommand
{
    
    public NewCommand()
    {
        this.setAction("new");
        this.setPermission("RedPackets.new");
        this.setPlayerNeeded(true);
        this.setHelp(ChatColor.RED + "/redpackets new <金钱数量> <份数> - 我是壕，我要发红包！");
    }
    
    @Override
    protected void executeCommand(CommandSender sender, String[] args)
    {
        
        if (3 != args.length)
        {
            sender.sendMessage(this.getHelp());
            return;
        }
        
        Player p = (Player)sender;
        
        double totalMoney = 0;
        double devide = 0;
        try
        {
            totalMoney = Double.parseDouble(args[1]);
            devide = Math.round(Double.parseDouble(args[2]));
        }
        catch (NumberFormatException e)
        {
            p.sendMessage(ChatColor.GRAY + "请勿调戏红包君！");
            return;
        }
        
        if (0 >= devide || 0 >= totalMoney)
        {
            p.sendMessage(ChatColor.GRAY + "请勿调戏红包君！");
            return;
        }
        
        if (0.1 > totalMoney / devide)
        {
            p.sendMessage(ChatColor.GRAY + "壕就要有壕的样子！这么多人抢这么少钱算啥嘛！");
            return;
        }
        
        EconomyResponse er = Main.economy.withdrawPlayer(p, totalMoney);
        
        if (!er.transactionSuccess())
        {
            p.sendMessage(ChatColor.GRAY + "Vault君说资金转移出现问题！你是不是不够钱呀？");
            return;
        }
        
        String code = Main.getInstance().getConfigs()
                .saveRedPacket(p.getDisplayName(), totalMoney, devide);
        
        p.sendMessage(ChatColor.RED + "发布成功！兑换码：" + code);
        p.sendMessage(ChatColor.RED + "你可以把兑换码发给其他玩家；同时你的红包也会不定时在公屏出现！");
        Bukkit.broadcastMessage(ChatColor.RED + "[红包君] " + p.getDisplayName()
                + ChatColor.RED + " 发布了一个红包！");
        
    }
    
}
