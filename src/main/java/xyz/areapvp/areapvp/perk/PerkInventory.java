package xyz.areapvp.areapvp.perk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.areapvp.areapvp.AreaPvP;
import xyz.areapvp.areapvp.Items;
import xyz.areapvp.areapvp.inventory.ShopItem;
import xyz.areapvp.areapvp.level.PlayerInfo;
import xyz.areapvp.areapvp.level.PlayerModify;

public class PerkInventory
{
    public static Inventory getPerksInventory(Player player)
    {
        PlayerInfo info = PlayerModify.getInfo(player);
        if (info == null)
            return Bukkit.createInventory(null, 9, "ERROR");

        int balance = (int) AreaPvP.economy.getBalance(player);

        Inventory inventory = Bukkit.createInventory(null, (Perks.perks.size() % 9 == 0 ? 1: Perks.perks.size() % 9) * 9, ChatColor.BLUE + "Perk Shop");
        for (IPerkEntry item: Perks.perks)
        {
            if (info.perk.contains(item.getName()))
                inventory.addItem(Items.quickLore(Items.addGlow(item.getItem()), ChatColor.RED + "あなたはすでにこのPerkを適用しています！"));
            else if (info.ownPerk.contains(item.getName()))
                inventory.addItem(Items.quickLore(item.getItem(), ChatColor.YELLOW + "クリックして適用！"));
            else
            {
                ItemStack stack = ShopItem.getItem(item.getItem(),
                        balance,
                        item.getNeedGold(),
                        info.prestige,
                        item.getNeedPrestige());
                inventory.addItem(stack);
            }
        }

        return inventory;
    }

}