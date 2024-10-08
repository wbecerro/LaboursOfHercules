package wbe.laboursOfHercules.items;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.laboursOfHercules.LaboursOfHercules;
import wbe.laboursOfHercules.util.Utilities;

import java.util.ArrayList;

public class RandomLabourItem extends ItemStack {

    private LaboursOfHercules plugin = LaboursOfHercules.getInstance();

    private Utilities utilities = new Utilities();


    public RandomLabourItem() {
        super(LaboursOfHercules.config.randomLabourMaterial);

        ItemMeta meta;
        if(hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(LaboursOfHercules.config.randomLabourMaterial);
        }

        meta.setDisplayName(LaboursOfHercules.config.randomLabourName);

        ArrayList<String> lore = new ArrayList<>();
        for(String line : LaboursOfHercules.config.randomLabourLore) {
            lore.add(line.replace("&", "§"));
        }
        meta.setLore(lore);

        NamespacedKey baseKey = new NamespacedKey(plugin, "randomLabour");
        meta.getPersistentDataContainer().set(baseKey, PersistentDataType.BOOLEAN, true);

        if(LaboursOfHercules.config.randomLabourGlow) {
            meta.addEnchant(Enchantment.INFINITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        setItemMeta(meta);
    }
}
