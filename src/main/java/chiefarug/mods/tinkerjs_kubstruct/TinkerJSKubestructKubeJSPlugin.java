package chiefarug.mods.tinkerjs_kubstruct;

import chiefarug.mods.tinkerjs_kubstruct.item.ModifiableItemBuilder;
import chiefarug.mods.tinkerjs_kubstruct.item.PartJS;
import chiefarug.mods.tinkerjs_kubstruct.item.PartJSWrapper;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ToolAction;
import slimeknights.tconstruct.library.tools.stat.IToolStat;

@SuppressWarnings("unused")
public class TinkerJSKubestructKubeJSPlugin extends KubeJSPlugin {

	public void init() {
		RegistryObjectBuilderTypes.ITEM.addType("tinker_tool", ModifiableItemBuilder.class, ModifiableItemBuilder::new);
	}

	public void generateDataJsons(DataJsonGenerator generator) {
		super.generateDataJsons(generator);
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
		typeWrappers.register(Tier.class, Util::wrapTier);
	}

	public void generateAssetJsons(AssetJsonGenerator generator) {
		super.generateAssetJsons(generator);
	}
}
