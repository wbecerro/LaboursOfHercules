package wbe.laboursOfHercules.items;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.labours.Crystal;

import java.util.ArrayList;

public class CrystalItem extends ItemStack {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    private Crystal crystal;

    public CrystalItem(Crystal crystal) {
        super(crystal.getMaterial());
        this.crystal = crystal;

        ItemMeta meta;
        if(hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(crystal.getMaterial());
        }

        meta.setDisplayName(crystal.getName());

        ArrayList<String> lore = new ArrayList<>();
        for(String line : crystal.getLore()) {
            lore.add(line.replace("&", "§"));
        }
        meta.setLore(lore);

        NamespacedKey crystalKey = new NamespacedKey(plugin, crystal.getId());
        meta.getPersistentDataContainer().set(crystalKey, PersistentDataType.BOOLEAN, true);

        if(crystal.isGlow()) {
            meta.addEnchant(Enchantment.INFINITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        setItemMeta(meta);
    }
}
