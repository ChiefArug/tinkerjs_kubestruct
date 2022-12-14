package chiefarug.mods.tinkerjs_kubestruct.item;

import chiefarug.mods.tinkerjs_kubestruct.FixedJsonGenerator;
import chiefarug.mods.tinkerjs_kubestruct.Util;
import chiefarug.mods.tinkerjs_kubestruct.VirtualPacks;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;

import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.stat.IToolStat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static chiefarug.mods.tinkerjs_kubestruct.item.ModifiableItemBuilder.StatMultiplierType.*;
import static chiefarug.mods.tinkerjs_kubestruct.item.PartPosition.CLEAVER_LAYOUT;
import static chiefarug.mods.tinkerjs_kubestruct.item.PartPosition.DEFAULT_POSITIONS;
import static chiefarug.mods.tinkerjs_kubestruct.item.PartPosition.PICKAXE_LAYOUT;
import static chiefarug.mods.tinkerjs_kubestruct.item.PartPosition.SWORD_LAYOUT;
import static slimeknights.tconstruct.common.TinkerTags.Items.*;

@SuppressWarnings("unused")
public class ModifiableItemBuilder extends ItemBuilder {

	@Nullable
	public Supplier<ItemStackJS> iconSupplier; // If they want advanced rendering then they can supply a supplier
	public final List<PartJS> parts = new ArrayList<>();
	public final List<ToolAction> actions = new ArrayList<>();
	public final Map<String, Integer> slots = new HashMap<>();
	public final Map<ResourceLocation, Integer> traits = new HashMap<>();
	public final Map<IToolStat<?>, Float> baseStats = new HashMap<>();
	public final Map<IToolStat<?>, Float> multiplierStats = new HashMap<>();
	public JsonObject harvestLogicJson = new JsonObject();
	public JsonObject attackLogicJson = new JsonObject();
	public JsonObject aoeJson = new JsonObject();
	public String description = "Custom Tool made with ??? by TinkerJS Kubestruct";
	public static int currentLargeSortIndex = 15; // this just places them after all default tinkers tools
	public transient int sortIndex;
	public transient boolean attack;
	public transient boolean harvest;
	public transient Boolean harvestMain;
	public boolean held = true;
	public boolean interactionModifiers;
	public boolean large;
	public boolean hasDurability = true;
	private boolean finished;
	private boolean hasRecipe = true;
	public List<ResourceLocation> modifierTextureRoots = new ArrayList<>();

	public ModifiableItemBuilder(ResourceLocation i) {
		super(i);
		this.sortIndex = currentLargeSortIndex++;
		this.interactionModifiers = true;
		VirtualPacks.INSTANCE.addBuilder(this);
	}

	public ModifiableItemBuilder description(String description) {
		this.description = description;
		return this;
	}

	public ModifiableItemBuilder part(PartJS part) {
		parts.add(part);
		return this;
	}

	public ModifiableItemBuilder noRecipe() {
		hasRecipe = false;
		return this;
	}

	public ModifiableItemBuilder icon(ResourceLocation icon) {
		return icon(() -> ItemStackJS.of(icon));
	}

	public ModifiableItemBuilder icon(Supplier<ItemStackJS> icon) {
		this.iconSupplier = icon;
		return this;
	}

	public ModifiableItemBuilder upgrades(int u) {
		slots.put("upgrades", u);
		return this;
	}

	public ModifiableItemBuilder abilities(int a) {
		slots.put("abilities", a);
		return this;
	}

	public ModifiableItemBuilder slot(String slot, int s) {
		slots.put(slot, s);
		return this;
	}

	public ModifiableItemBuilder trait(String trait) {
		return trait(trait, 1);
	}

	public ModifiableItemBuilder trait(String trait, int level) {
		traits.put(Util.inputId(trait), level);
		return this;
	}

	public ModifiableItemBuilder action(ToolAction action) {
		if (action != null)
			actions.add(action);
		else
			throw new NullPointerException("Invalid/null tool action");
		return this;
	}

	// Could move these to a wrapper class because they are so darn complex. That feels kind of unnecessary for such a small thing though
	public ModifiableItemBuilder aoe(JsonObject json) {
		aoeJson = json;
		return this;
	}

	private JsonObject boxJson(int[] box) {
		JsonObject json = new JsonObject();
		switch (box.length) {
			case 3:
				json.addProperty("depth", box[2]);
			case 2:
				json.addProperty("height", box[1]);
			case 1: { // fallthrough my beloved
				json.addProperty("width", box[0]);
				break;
			}
			default: {
				if (box.length > 3)
					throw new IllegalArgumentException("Box too large for custom tool " + id + ", maximum of three directions (width, height, depth)");
				else
					throw new IllegalArgumentException("Box too small for custom tool " + id + ", minimum of one direction (width, height, depth)");
			}
		}
		return json;
	}

	public ModifiableItemBuilder boxAoe(String direction, int bonusWidth, int bonusHeight, int bonusDepth, int[] ...expansions) {
		aoeJson = new JsonObject();
		aoeJson.addProperty("type", "tconstruct:box");
		aoeJson.add("bonus", boxJson(new int[]{bonusWidth, bonusHeight, bonusDepth}));

		JsonArray expansionsJson = new JsonArray();
		for (int[] expansion : expansions) {
			expansionsJson.add(boxJson(expansion));
		}

		if (expansionsJson.size() > 0)
			aoeJson.add("expansions", expansionsJson);

		aoeJson.addProperty("expansion_direction", Util.inputId(direction).toString());
		return this;
	}

	public ModifiableItemBuilder boxAoe(String direction, int[][] expansions) {
		return boxAoe(direction, 0, 0, 0, expansions);
	}

	public ModifiableItemBuilder boxAoe(int[][] expansions) {
		return boxAoe("side_hit", 0, 0, 0, expansions);
	}

	public ModifiableItemBuilder boxAoe(String direction) {
		return boxAoe(direction, new int[0][0]);
	}

	public ModifiableItemBuilder circleAoe(int diameter, boolean _3d) {
		aoeJson = new JsonObject();
		aoeJson.addProperty("type", "tconstruct:circle");
		aoeJson.addProperty("diameter", diameter);
		aoeJson.addProperty("3D", _3d);
		return this;
	}

	public ModifiableItemBuilder circleAoe(int diameter) {
		return circleAoe(diameter, false);
	}

	public ModifiableItemBuilder treeAoe(int widthBonus, int depthBonus) {
		aoeJson = new JsonObject();
		aoeJson.addProperty("type", "tconstruct:tree");
		aoeJson.addProperty("width_bonus", widthBonus);
		aoeJson.addProperty("depth_bonus", depthBonus);
		return this;
	}

	public ModifiableItemBuilder treeAoe() {
		return treeAoe(0, 0);
	}

	public ModifiableItemBuilder veinAoe(int distance) {
		aoeJson = new JsonObject();
		aoeJson.addProperty("type", "tconstruct:vein");
		aoeJson.addProperty("max_distance", distance);
		return this;
	}

	public ModifiableItemBuilder alternativeAoe(ResourceLocation tag, JsonObject match, JsonObject fallback) {
		aoeJson = new JsonObject();
		aoeJson.addProperty("type", "tconstruct:fallback");
		aoeJson.addProperty("tag", tag.toString());
		aoeJson.add("if_matches", match);
		aoeJson.add("fallback", fallback);
		return this;
	}


	public ModifiableItemBuilder sortIndex(int i) {
		sortIndex = i;
		return this;
	}

	public enum StatMultiplierType {
		// base is applied on top of materials, modifier on top of modifiers
		// called base and multiplier in other places
		base, modifier
	}

	public ModifiableItemBuilder statMultiplier(IToolStat<?> stat, float m) {
		return statMultiplier(modifier, stat, m);
	}

	public ModifiableItemBuilder statMultiplier(StatMultiplierType type, IToolStat<?> stat, float m) {
		switch (type) {
			case base -> baseStats.put(stat, m);
			case modifier -> multiplierStats.put(stat, m);
		}
		return this;
	}

	public ModifiableItemBuilder harvest(JsonObject json) {
		harvest = true;
		harvestLogicJson = json == null ? new JsonObject() : json;
		return this;
	}

	public ModifiableItemBuilder noHarvest() {
		harvestLogicJson = new JsonObject();
		harvest = false;
		return this;
	}

	public ModifiableItemBuilder effectiveTagHarvest(ResourceLocation tag) {
		harvest("tconstruct:effective_tag", tag);
		return this;
	}

	public ModifiableItemBuilder modifierTagHarvest(ResourceLocation tag, JsonObject... speedModifiers) {
		var modifiers = new JsonArray();
		for (JsonObject speedModifier : speedModifiers) {
			modifiers.add(speedModifier);
		}
		harvest("tconstruct:modified_tag", tag, "modifiers", modifiers);
		return this;
	}

	@SuppressWarnings("ConstantConditions")
	public ModifiableItemBuilder fixedTierHarvest(Tier t, ResourceLocation tag) {
		if (t != null) {
			harvest("tconstruct:fixed_tier", tag, "tier", new JsonPrimitive(TierSortingRegistry.getName(t).toString()));
		} else {
			Util.startupError("Unknown tier passed to fixedTierHarvest with tag " + tag.toString()); // We don't actually know what the bad tier was, because the typewrapper swallowed it
		}
		return this;
	}

	private void harvest(String type, ResourceLocation effectiveTag) {
		harvest = true;
		if (harvestMain == null) harvestMain = true;
		harvestLogicJson = new JsonObject();
		harvestLogicJson.addProperty("type", type);
		harvestLogicJson.add("effective", new JsonPrimitive(effectiveTag.toString()));
	}

	private void harvest(String type, ResourceLocation effectiveTag, String tertiaryKey, JsonElement tertiaryValue) {
		harvest(type, effectiveTag);
		harvestLogicJson.add(tertiaryKey, tertiaryValue);
	}

	public ModifiableItemBuilder noAttack() {
		attackLogicJson = new JsonObject();
		attack = false;
		return this;
	}

	public ModifiableItemBuilder attack(JsonObject json) {
		attack = true;
		attackLogicJson = json;
		return this;
	}

	public ModifiableItemBuilder sweepAttack(int range) {
		attack("tconstruct:sweep", "range", new JsonPrimitive(range));
		return this;
	}

	public ModifiableItemBuilder circleAttack(int diameter) {
		attack( "tconstruct:circle","diameter", new JsonPrimitive(diameter));
		return this;
	}

	public ModifiableItemBuilder particleAttack(ResourceLocation particle) {
		attack("tconstruct:particle", "particle", new JsonPrimitive(particle.toString()));
		return this;
	}

	private void attack(String type, String secondaryKey, JsonElement secondaryValue) {
		attack = true;
		if (harvestMain == null) harvestMain = false;
		attackLogicJson = new JsonObject();
		attackLogicJson.addProperty("type", type);
		attackLogicJson.add(secondaryKey, secondaryValue);
	}

	public ModifiableItemBuilder notHeld() {
		return held(false);
	}

	public ModifiableItemBuilder held(boolean b) {
		held = b;
		return this;
	}

	public ModifiableItemBuilder large() {
		large = true;
		throw new UnsupportedOperationException("Large tools are not supported yet");
//		return this;
	}

	public ModifiableItemBuilder small() {
		large = false;
		return this;
	}

	private void checkLayout(String layoutName, int size) {
		if (parts.size() < 2) {
			throw new IllegalStateException("Cannot use station layouts on less that 2 parts!");
		} else if (parts.size() != size) {
			throw new IllegalStateException("Can only use " + layoutName + " layout with " + size + " parts");
		}
	}

	private void defaultLayout(String name, int numOfParts) {
		checkLayout(name, numOfParts);
		PartPosition.forceApply(parts, DEFAULT_POSITIONS[numOfParts - 1]);
	}

	public ModifiableItemBuilder daggerLayout() {
		defaultLayout("dagger", 1);
		return this;
	}

	public ModifiableItemBuilder axeLayout() {
		defaultLayout("axe", 2);
		return this;
	}

	public ModifiableItemBuilder hammerLayout() {
		defaultLayout("hammer", 3);
		return this;
	}

	public ModifiableItemBuilder swordLayout() {
		checkLayout("sword", 3);
		PartPosition.forceApply(parts, SWORD_LAYOUT);
		return this;
	}

	public ModifiableItemBuilder pickaxeLayout() {
		checkLayout("pickaxe", 3);
		PartPosition.forceApply(parts, PICKAXE_LAYOUT);
		return this;
	}

	public ModifiableItemBuilder cleaverLayout() {
		checkLayout("cleaver", 4);
		PartPosition.forceApply(parts, CLEAVER_LAYOUT);
		return this;
	}

	public ModifiableItemBuilder addModifierTextureLocation(ResourceLocation rl) {
		this.modifierTextureRoots.add(rl);
		return this;
	}

	@Override // Don't generate regular model files
	public void generateAssetJsons(AssetJsonGenerator g) {}

	public void generateAssetJsons(FixedJsonGenerator generator) {
		if (modelJson != null) {
			generator.json(itemModelLocation(id), modelJson);
		} else {
			JsonObject model = new JsonObject();
			String baseTexture = texture != null ? texture : newID("item/", "/").toString();

			model.addProperty("loader", "tconstruct:tool");
			model.addProperty("parent", "forge:item/default-tool");

			var partsArray = new JsonArray();

			for (int i = 0; i < parts.size(); i++) {
				PartJS part = parts.get(i);
				// This method also handles adding information to the texture json.
				// Its kinda ugly but it all needs the same information
				JsonElement partModelJson = part.toItemModelJson(i, textureJson, baseTexture);
				partsArray.add(partModelJson);
			}
			model.add("textures", textureJson);
			model.add("parts", partsArray);

			var modifierRoots = new JsonArray();
			this.modifierTextureRoots.forEach(location -> modifierRoots.add(location.toString()));
			modifierRoots.add(baseTexture + "modifiers/");
			model.add("modifier_roots", modifierRoots);

			generator.json(itemModelLocation(id), model);
		}
	}

	private ResourceLocation itemModelLocation(ResourceLocation id) {
		return new ResourceLocation(id.getNamespace(), "models/item/" + id.getPath() + ".json");
	}

	@Override
	public void generateDataJsons(DataJsonGenerator gen) {}

	public void generateDataJsons(FixedJsonGenerator generator) {
		// DO NOT FORGET THE .JSON
		// At least for my impl of VirtualPacks. For KubeJS ???????????
		generator.json(newID("tinkering/tool_definitions/", ".json"), generateToolDefinitionJson());
		if (parts.size() > 1) // Tinker's doesn't like station layouts with only one part
			generator.json(newID("tinkering/station_layouts/", ".json"), generateStationLayoutJson());
		if (hasRecipe) {
			generator.json(newID("recipes/generated/", ".json"), generateRecipeJson());
		}
	}

	private JsonObject generateRecipeJson() {
		JsonObject json = new JsonObject();
		json.addProperty("type", "tconstruct:tool_building");
		json.addProperty("result", id.toString());
		return json;
	}

	private JsonObject generateToolDefinitionJson() {
		JsonObject json = new JsonObject();

		JsonArray partsJson = new JsonArray();
		for (PartJS part : parts) {
			partsJson.add(part.toToolDefinitionJson());
		}
		json.add("parts", partsJson);

		JsonObject statsJson = new JsonObject();
		JsonObject baseStatsJson = new JsonObject();
		JsonObject multiplierStatsJson = new JsonObject();

		baseStats.forEach((stat, level) -> baseStatsJson.addProperty(stat.getName().toString(), level));
		multiplierStats.forEach((stat, level) -> multiplierStatsJson.addProperty(stat.getName().toString(), level));
		statsJson.add("base", baseStatsJson);
		statsJson.add("multiplier", multiplierStatsJson);
		json.add("stats", statsJson);

		JsonObject slotsJson = new JsonObject();
		if (!slots.containsKey("upgrades"))
			slots.put("upgrades", 6 - parts.size());
		if (!slots.containsKey("abilities"))
			slots.put("abilities", 1);
		slots.forEach(slotsJson::addProperty);
		json.add("slots", slotsJson);

		JsonArray traitsJson = new JsonArray();
		traits.forEach((trait, level) -> {
			JsonObject tj = new JsonObject();
			tj.addProperty("name", trait.toString());
			tj.addProperty("level", level);
			traitsJson.add(tj);
		});
		json.add("traits", traitsJson);

		JsonArray actionsJson = new JsonArray();
		actions.forEach(action -> actionsJson.add(action.name()));
		json.add("actions", actionsJson);

		if (harvest) {
			JsonObject harvestJson = new JsonObject();
			if (harvestLogicJson.size() > 0)
				harvestJson.add("logic", harvestLogicJson);
			if (aoeJson.size() != 0) // if its not empty
				harvestJson.add("aoe", aoeJson);
			if (harvestJson.size() > 0)
				json.add("harvest", harvestJson);
		}
		if (attack && attackLogicJson.size() > 0)
			json.add("attack", attackLogicJson);

		return json;
	}

	private JsonObject generateStationLayoutJson() {
		JsonObject json = new JsonObject();

		json.addProperty("translation_key", translationKey);

		if (iconSupplier == null) {
			json.add("icon", getRenderTool().toJson());
		} else {
			json.add("icon", iconSupplier.get().toJson());
		}

		json.addProperty("sortIndex", sortIndex);

		JsonArray inputSlotsJson = new JsonArray();
		for (PartJS part : parts) {
			inputSlotsJson.add(part.toStationLayoutJson());
		}
		json.add("input_slots", inputSlotsJson);

//		LGGR.info(json.toString());
		return json;
	}

	@Override
	public void generateLang(Map<String, String> lang) {
		super.generateLang(lang);
		lang.put(translationKey + ".description", description);
	}

	private void tag(TagKey<Item> tag) {
		tag(tag.location());
	}

	private ItemStackJS getRenderTool() {
		return new ItemStackJS(((ModifiableItem) get()).getRenderTool());
	}

	public ToolDefinition createToolDefinition() {
		return ToolDefinition.builder(id).meleeHarvest().build();
	}

	@Override
	public ModifiableItem createObject() {
		finish();
		return new ModifiableItemJS(this);
	}

	private void finish() {
		if (finished) return;

		// Apply a default layout if one is available
		// Will ignore any manually set positions
		if (parts.size() < DEFAULT_POSITIONS.length) {
			PartPosition.apply(parts, DEFAULT_POSITIONS[parts.size()]);
		}
		// Apply tags based on set properties
		applyTags();

		finished = true;
	}

	private void applyTags() {
		// Tags are from TinkerTags
		if (parts.size() > 1)
			tag(MULTIPART_TOOL);
		if (harvest)
			tag(HARVEST);
		if (attack)
			tag(MELEE);
		// melee_or_harvest is automatically applied by tinkers
		if (harvestMain != null)
			if (harvestMain)
				tag(HARVEST_PRIMARY);
			else
				tag(MELEE_PRIMARY);
		if (held)
			tag(HELD);
		if (interactionModifiers)
			tag(INTERACTABLE);
		if (hasDurability)
			tag(DURABILITY);
		if (aoeJson.size() > 0)
			tag(AOE);
		// misc un-configurable tags
		tag(ONE_HANDED);
		tag(INTERACTABLE);
		tag(MODIFIABLE);
	}
}