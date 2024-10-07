package wbe.laboursOfHercules.labours;

import org.bukkit.Material;

import java.util.List;

public class Crystal {

    private String id;

    private Material material;

    private String name;

    private List<String> lore;

    private boolean glow;

    public Crystal(String id, Material material, String name, List<String> lore, boolean glow) {
        this.id = id;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.glow = glow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public boolean isGlow() {
        return glow;
    }

    public void setGlow(boolean glow) {
        this.glow = glow;
    }
}
