package chiefarug.mods.tinkerjs_kubestruct.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import slimeknights.tconstruct.tools.TinkerToolParts;

import java.util.HashMap;
import java.util.Map;

import static chiefarug.mods.tinkerjs_kubestruct.item.PartJS.ingredientSupplier;


@SuppressWarnings("unused")
public class PartJSWrapper {

	public static final Map<String, PartJS> DEFAULT_PARTS = new HashMap<>();
	private static final PartJS PICK_HEAD = headPart("pick_head", "pickaxe", TinkerToolParts.pickHead);
	private static final PartJS HAMMER_HEAD = headPart("hammer_head","sledge_hammer", TinkerToolParts.hammerHead);
	private static final PartJS AXE_HEAD = headPart("small_axe_head", "hand_axe", TinkerToolParts.smallAxeHead);
	private static final PartJS BROAD_AXE_HEAD = headPart("broad_axe_head", "broad_axe/", TinkerToolParts.broadAxeHead);
	private static final PartJS BLADE = headPart("small_blade","sword", TinkerToolParts.smallBlade);
	private static final PartJS BROAD_BLADE = headPart("broad_blade", "cleaver", TinkerToolParts.broadBlade);
	private static final PartJS BINDING = part("tool_binding", "pickaxe", "binding", TinkerToolParts.toolBinding);
	private static final PartJS PLATE = part("large_plate", "sledge_hammer", "back", TinkerToolParts.largePlate);
	private static final PartJS ROUND_PLATE = part("round_plate", "mattock", "pick", TinkerToolParts.roundPlate);
	private static final PartJS HANDLE = part("tool_handle", "pickaxe", "handle", TinkerToolParts.toolHandle);
	private static final PartJS TOUGH_HANDLE = part("tough_handle", "sledge_hammer", "handle", TinkerToolParts.toughHandle);
	private static final PartJS REPAIR_KIT = new PartJS("repair_kit", new ResourceLocation("tconstruct:repair_kit"), "tconstruct:item/parts/repair_kit", null, -1, -1, ingredientSupplier(TinkerToolParts.repairKit), "item.tconstruct.repair_kit");
	static { DEFAULT_PARTS.put("repair_kit", REPAIR_KIT); }


	private static PartJS headPart(String id, String defaultTool, ItemLike item) {
		return part(id, defaultTool, "head", item);
	}
	private static PartJS part(String id, String defaultTool, String toolPartName, ItemLike item) {
		PartJS partjs = new PartJS(id, new ResourceLocation("tconstruct:" + id), "tconstruct:item/tool/" + defaultTool + "/" + toolPartName, null, -1, -1, ingredientSupplier(item), "item.tconstruct." + id);
		DEFAULT_PARTS.put(id, partjs);
		return partjs;
	}


	public static PartJS of(String id) {
		return new PartJS(id, PartJS.iconFromId(id), PartJS.toolTextureFromId(id), null, PartJS.defaultX(), PartJS.defaultY(), PartJS.filterFromId(id), PartJS.translationKeyFromId(id));
	}

	public static PartJS getPickHead() {
		return PartJSWrapper.PICK_HEAD.copy();
	}

	public static PartJS getHammerHead() {
		return PartJSWrapper.HAMMER_HEAD.copy();
	}

	public static PartJS getAxeHead() {
		return PartJSWrapper.AXE_HEAD.copy();
	}

	public static PartJS getBroadAxeHead() {
		return PartJSWrapper.BROAD_AXE_HEAD.copy();
	}

	public static PartJS getBlade() {
		return PartJSWrapper.BLADE.copy();
	}

	public static PartJS getBroadBlade() {
		return PartJSWrapper.BROAD_BLADE.copy();
	}

	public static PartJS getBinding() {
		return PartJSWrapper.BINDING.copy();
	}

	public static PartJS getPlate() {
		return PartJSWrapper.PLATE.copy();
	}

	public static PartJS getRoundPlate() {
		return PartJSWrapper.ROUND_PLATE.copy();
	}

	public static PartJS getHandle() {
		return PartJSWrapper.HANDLE.copy();
	}

	public static PartJS getToughHandle() {
		return PartJSWrapper.TOUGH_HANDLE.copy();
	}

	public static PartJS getRepairKit() {
		return PartJSWrapper.REPAIR_KIT.copy();
	}
}
