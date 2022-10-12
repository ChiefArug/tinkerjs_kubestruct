package chiefarug.mods.tinkerjs_kubstruct;

import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.MapJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class Util {


	public static ResourceLocation inputId(String id) {
		if (id.indexOf(':') == -1) {
			return new ResourceLocation("tconstruct:" + id);
		}
		return new ResourceLocation(id);
	}

	public static ResourceLocation newId(ResourceLocation id, String before, String after) {
		if (before.isBlank() && after.isBlank()) return id;
		return new ResourceLocation(id.getNamespace() + ':' + before + id.getPath() + after);
	}

	@Nullable
	public static ToolAction wrapToolAction(Object o) {
		if (o instanceof ToolAction ta) {
			return ta;
		} else if (o instanceof CharSequence cs) {
			return ToolAction.get(cs.toString());
		}
		var map = MapJS.of(o);
		if (map != null && map.containsKey("action") && map.get("action") instanceof String s) {
			return ToolAction.get(s);
		}
		return null;
	}

	@Nullable // I take back what I said about default values
	public static IToolStat<?> wrapToolStat(Object o) {
		if (o instanceof IToolStat ts) {
			return ts;
		} else if (o instanceof ResourceLocation rl) {
			return ToolStats.getToolStat(new ToolStatId(rl));
		} else if (o instanceof CharSequence cs) {
			return ToolStats.getToolStat(new ToolStatId(cs.toString()));
		}
		return null;
	}

	@Nullable
	public static Tier wrapTier(Object o) {
		if (o instanceof Tier t) {
			return t;
		} else if (o instanceof ResourceLocation rl) {
			return TierSortingRegistry.byName(rl);
		} else if (o instanceof CharSequence cs) {
			return TierSortingRegistry.byName(new ResourceLocation(cs.toString()));
		}
		return null;
	}

	public static void startupError(Object message) {
		ScriptType.STARTUP.console.error(message);
	}

}
