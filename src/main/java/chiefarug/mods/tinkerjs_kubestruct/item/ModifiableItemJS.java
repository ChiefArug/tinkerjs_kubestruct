package chiefarug.mods.tinkerjs_kubestruct.item;

import slimeknights.tconstruct.library.tools.item.ModifiableItem;

public class ModifiableItemJS extends ModifiableItem {
	public ModifiableItemJS(ModifiableItemBuilder builder) {
		super(builder.createItemProperties(), builder.			createToolDefinition());
	}
}

