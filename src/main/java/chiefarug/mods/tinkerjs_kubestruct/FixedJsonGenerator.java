package chiefarug.mods.tinkerjs_kubestruct;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

// A fixed version of KubeJS json generator that won't crash when used before world load in certain circumstances...
public class FixedJsonGenerator {
	private final Map<ResourceLocation, JsonElement> map;

    public FixedJsonGenerator(Map<ResourceLocation, JsonElement> m) {
        this.map = m;
    }

    public void json(ResourceLocation id, JsonElement json) {
        this.map.put(id, json);
    }
}
