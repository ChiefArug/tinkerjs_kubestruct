package chiefarug.mods.tinkerjs_kubstruct.item;

import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import slimeknights.tconstruct.tools.TinkerToolParts;

import java.util.function.Supplier;

import static chiefarug.mods.tinkerjs_kubstruct.Util.inputId;
import static chiefarug.mods.tinkerjs_kubstruct.item.PartJS.ingredientSupplier;


@SuppressWarnings("unused")
public class PartJSWrapper {

	public static final PartJS PICK_HEAD = headPart("pick_head", "pickaxe", TinkerToolParts.pickHead);
	public static final PartJS HAMMER_HEAD = headPart("hammer_head","sledge_hammer", TinkerToolParts.hammerHead);
	public static final PartJS AXE_HEAD = headPart("small_axe_head", "hand_axe", TinkerToolParts.smallAxeHead);
	public static final PartJS BROAD_AXE_HEAD = headPart("broad_axe_head", "broad_axe/", TinkerToolParts.broadAxeHead);
	public static final PartJS BLADE = headPart("small_blade","sword", TinkerToolParts.smallBlade);
	public static final PartJS BROAD_BLADE = headPart("broad_blade", "cleaver", TinkerToolParts.broadBlade);
	public static final PartJS BINDING = part("tool_binding", "pickaxe", "binding", TinkerToolParts.toolBinding);
	public static final PartJS PLATE = part("large_plate", "sledge_hammer", "back", TinkerToolParts.largePlate);
	public static final PartJS ROUND_PLATE = part("round_plate", "pickadze", "pick", TinkerToolParts.roundPlate);
	public static final PartJS HANDLE = part("tool_handle", "pickaxe", "handle", TinkerToolParts.toolHandle);
	public static final PartJS TOUGH_HANDLE = part("tough_handle", "sledge_hammer", "handle", TinkerToolParts.toughHandle);
	public static final PartJS REPAIR_KIT = new PartJS("repair_kit", new ResourceLocation("tconstruct:repair_kit"), "tconstruct:item/parts/repair_kit", null, -1, -1, ingredientSupplier(TinkerToolParts.repairKit), "item.tconstruct.repair_kit");


	private static PartJS headPart(String id, String defaultTool, ItemLike item) {
		return part(id, defaultTool, "head", item);
	}
	private static PartJS part(String id, String defaultTool, String toolPartName, ItemLike item) {
		return new PartJS(id, new ResourceLocation("tconstruct:" + id), "tconstruct:item/tool" + defaultTool + "/" + toolPartName, null, -1, -1, ingredientSupplier(item), "item.tconstruct." + id);
	}


	public static PartJS of(String id) {
		return of(id, defaultX(), defaultY());
	}

	public static PartJS of(String id, int x, int y) {
		return of(id, iconFromId(id), x, y);
	}

	public static PartJS of(String id, ResourceLocation iconFilter, int x, int y) {
		return of(id, iconFilter, filterFromId(iconFilter), x, y);
	}

	public static PartJS of(String id, ResourceLocation icon, Supplier<IngredientJS> filter, int x, int y) {
		return of(id, icon, filter, x, y, translationKeyFromId(id));
	}

	public static PartJS of(String id, ResourceLocation filterIcon, int x, int y, String translationKey) {
		return of(id, filterIcon, filterFromId(filterIcon), x, y, translationKey);
	}

	public static PartJS of(String id, ResourceLocation icon, Supplier<IngredientJS> filter, int x, int y, String translationKey) {
		return of(id, icon, filter, x, y, translationKey, toolTextureFromId(id), brokenToolTextureFromId(id));
	}

	public static PartJS of(String id, ResourceLocation icon, Supplier<IngredientJS> filter, int x, int y, String translationKey, String toolTexture, String brokenToolTexture) {
		var rl = inputId(id);
		if (!rl.getNamespace().equals("tconstruct")) {
			ScriptType.STARTUP.console.warn("Part constructed using Part#of from non tconstruct namespace. Make sure you know what you are doing!");
		}
		return new PartJS(id, icon, toolTexture, brokenToolTexture, x, y, filter, translationKey);
	}

	private static ResourceLocation iconFromId(String id) {
		return inputId(id);
	}
	// These return strings because the tool also determines part of the texture, so we do not know the full RL yet
	private static String toolTextureFromId(String id) {
		return id;
	}
	private static String brokenToolTextureFromId(String id) {
		return "broken_" + id;
	}
	private static int defaultX() {
		return -1;
	}
	private static int defaultY() {
		return -1;
	}
	private static Supplier<IngredientJS> filterFromId(ResourceLocation id) {
		return () -> ItemStackJS.of(id);
	}
	private static String translationKeyFromId(String id) {
		var rl = inputId(id);
		return "item." + rl.getNamespace() + rl.getPath();
	}



}
