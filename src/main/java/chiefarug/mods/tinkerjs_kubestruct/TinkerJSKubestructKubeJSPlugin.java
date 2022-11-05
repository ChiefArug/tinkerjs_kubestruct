package chiefarug.mods.tinkerjs_kubestruct;

import chiefarug.mods.tinkerjs_kubestruct.item.ModifiableItemBuilder;
import chiefarug.mods.tinkerjs_kubestruct.item.PartJS;
import chiefarug.mods.tinkerjs_kubestruct.item.PartJSWrapper;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraftforge.common.ToolAction;
import slimeknights.tconstruct.library.tools.stat.IToolStat;

//@SuppressWarnings("unused")
public class TinkerJSKubestructKubeJSPlugin extends KubeJSPlugin {

	@Override
	public void init() {
			RegistryObjectBuilderTypes.ITEM.addType("tconstruct:modifiable", ModifiableItemBuilder.class, ModifiableItemBuilder::new);
	}

	@Override
	public void addBindings(BindingsEvent event) {
		event.add("Part", PartJSWrapper.class);
	}

	@Override
	public void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
		typeWrappers.register(PartJS.class, PartJS::of);
		typeWrappers.register(ToolAction.class, Util::wrapToolAction);
		typeWrappers.register(IToolStat.class, Util::wrapToolStat);
	}

	@Override
	public void generateAssetJsons(AssetJsonGenerator generator) {}

	@Override
	public void generateDataJsons(DataJsonGenerator generator) {}
}
