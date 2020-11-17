package xyz.areapvp.areapvp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import xyz.areapvp.areapvp.inventory.Shop;
import xyz.areapvp.areapvp.item.IShopItem;
import xyz.areapvp.areapvp.perk.IPerkEntry;
import xyz.areapvp.areapvp.perk.Perks;

public class GUI implements Listener
{

    @EventHandler
    private void onEntityRight(PlayerInteractEntityEvent e)
    {
        if (e.getRightClicked().getType() != EntityType.VILLAGER)
            return;

        if (e.getPlayer().hasPermission("areapvp.admin"))
        {
            if (e.getPlayer().getInventory().getItemInMainHand() != null && e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR &&
                    e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null)
            {
                if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Shop Creator 3000"))
                {
                    e.getRightClicked().addScoreboardTag("areaPvP::Item");
                    e.getPlayer().sendMessage(ChatColor.GREEN + "アイテムショップを作成しました。");
                    return;
                }
                if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "PerkShop Creator 3000"))
                {
                    e.getRightClicked().addScoreboardTag("areaPvP::Perk");
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Perkショップを作成しました。");
                    return;
                }
            }
        }

        String type = null;

        if (e.getRightClicked().getScoreboardTags().contains("areaPvP::Perk"))
            type = "perk";
        else if (e.getRightClicked().getScoreboardTags().contains("areaPvP::Item"))
            type = "item";
        else if (e.getRightClicked().getScoreboardTags().contains("areaPvP::Prestige"))
            type = "prestige";

        if (type == null)
            return;

        e.setCancelled(true);
        switch (type)
        {
            case "item":
                AreaPvP.gui.put(e.getPlayer().getUniqueId(), "item");
                Shop.openInventory(e.getPlayer());
                break;
            case "perk":
                AreaPvP.gui.put(e.getPlayer().getUniqueId(), "perk");
                Shop.openPerkInventory(e.getPlayer());
                break;
        }
    }

    public static void playerPerkBuyProcess(Player player, IPerkEntry item)
    {
        if (item == null)
            return;

        if (AreaPvP.economy.getBalance(player) >= item.getNeedGold())
        {
            player.getInventory().addItem(item.getItem());
            player.sendMessage(ChatColor.GREEN + "Perkを購入しました！！");
            AreaPvP.economy.withdrawPlayer(player, item.getNeedGold());
            item.onBuy(player);
            player.closeInventory();
        }
    }


    public static void playerItemBuyProcess(Player player, IShopItem item)
    {

        if (item == null)
            return;

        if (AreaPvP.economy.getBalance(player) >= item.getNeedGold())
        {
            player.getInventory().addItem(item.getItem());
            player.sendMessage(ChatColor.GREEN + "アイテムを購入しました！");
            AreaPvP.economy.withdrawPlayer(player, item.getNeedGold());
            player.closeInventory();
        }
    }

    @EventHandler
    private void onPickUp(InventoryClickEvent e)
    {
        Player player = (Player) e.getWhoClicked();
        if (player == null)
            return;
        String type = AreaPvP.gui.get(player.getUniqueId());
        if (type == null)
            return;
        e.setCancelled(true);

        if (e.getInventory() instanceof PlayerInventory)
            return;

        ItemStack item = e.getCurrentItem();

        if (item == null)
            return;

        if (item.getType() == Material.BEDROCK)
        {
            player.sendMessage(ChatColor.RED + "あなたはこれを購入することができません！");
            return;
        }

        switch (type)
        {
            case "item":
                IShopItem shopItem = xyz.areapvp.areapvp.item.Items.getItem(Items.getMetaData(item, "type"));
                playerItemBuyProcess(player, shopItem);
                break;
            case "perk":
                IPerkEntry entry = Perks.getPerk(Items.getMetaData(item, "type"));
                playerPerkBuyProcess(player, entry);
                break;
        }

    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent e)
    {
        if (e.getEntity().getType() != EntityType.VILLAGER)
            return;
        String type = null;

        if (e.getEntity().getScoreboardTags().contains("areaPvP::Perk"))
            type = "perk";
        else if (e.getEntity().getScoreboardTags().contains("areaPvP::Item"))
            type = "item";
        else if (e.getEntity().getScoreboardTags().contains("areaPvP::Prestige"))
            type = "prestige";

        if (type == null)
            return;
        e.setCancelled(true);
    }

}