package xyz.areapvp.areapvp.perk;

import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import xyz.areapvp.areapvp.level.PlayerInfo;
import xyz.areapvp.areapvp.level.PlayerModify;

import java.util.Optional;

public class Perk
{
    public static void update(Player player)
    {
        PlayerInfo info = PlayerModify.getInfo(player);
        if (info == null)
            return;
        setPerk(player, info.perk.toArray(new String[0]));
    }

    public static void setPerk(Player player, String... perk)
    {
        if (perk.length < 4)
            return;

        PlayerModify.removeMetaData(player, "perk1");
        PlayerModify.removeMetaData(player, "perk2");
        PlayerModify.removeMetaData(player, "perk3");
        PlayerModify.removeMetaData(player, "perk4");

        PlayerModify.setMetaData(player, "perk1", perk[0]);
        PlayerModify.setMetaData(player, "perk2", perk[1]);
        PlayerModify.setMetaData(player, "perk3", perk[2]);
        if (perk.length > 4)
            PlayerModify.setMetaData(player, "perk4", perk[3]);
    }

    public static boolean contains(Player player, String perk)
    {
        Optional<MetadataValue> perk1 = PlayerModify.getMetaData(player, "perk1");
        Optional<MetadataValue> perk2 = PlayerModify.getMetaData(player, "perk2");
        Optional<MetadataValue> perk3 = PlayerModify.getMetaData(player, "perk3");
        Optional<MetadataValue> perk4 = PlayerModify.getMetaData(player, "perk4");

        if (perk1.isPresent() && perk1.get().asString().equals(perk))
            return true;
        else if (perk2.isPresent() && perk2.get().asString().equals(perk))
            return true;
        else if (perk3.isPresent() && perk3.get().asString().equals(perk))
            return true;
        else return perk4.isPresent() && perk4.get().asString().equals(perk);
    }
}
