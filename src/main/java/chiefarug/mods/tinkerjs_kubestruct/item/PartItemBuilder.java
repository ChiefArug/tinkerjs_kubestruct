package chiefarug.mods.tinkerjs_kubestruct.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.ExtraMaterialStats;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

import static chiefarug.mods.tinkerjs_kubestruct.item.PartItemBuilder.PartType.*;

public class PartItemBuilder extends ItemBuilder {

	public PartType type;

	public PartItemBuilder(ResourceLocation i) {
		super(i);
		type = HEAD;
	}

	public enum PartType {
		EXTRA(ExtraMaterialStats.ID), HANDLE(HandleMaterialStats.ID), HEAD(HeadMaterialStats.ID);
		private final MaterialStatsId statsId;

		PartType(MaterialStatsId statsId) {
			this.statsId = statsId;
		}

		MaterialStatsId getStatsId() {
			return statsId;
		}
	}

	public PartItemBuilder partType(PartType type) { // Would be nice if this could just be type
		this.type = type;
		return this;
	}

	public PartJS getPart() {
		return new PartJS(id.toString(), id, id.getPath(), type == HEAD ? "broken_" + id.getPath() : null, -1, -1, PartJS.ingredientSupplier(get()), "item." + id.getNamespace() + "." + id.getPath());
	}

	@Override
	public Item createObject() {
		return new ToolPartItem(createItemProperties(), type.getStatsId());
	}
}
