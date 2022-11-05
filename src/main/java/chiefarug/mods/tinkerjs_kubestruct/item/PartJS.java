package chiefarug.mods.tinkerjs_kubestruct.item;

import chiefarug.mods.tinkerjs_kubestruct.Util;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

import static chiefarug.mods.tinkerjs_kubestruct.Util.inputId;

public class PartJS {

	public final ResourceLocation id;
	public ResourceLocation icon;
	public String texture;
	@Nullable
	public String brokenTexture;
	public int xPosition;
	public int yPosition;
	public Supplier<IngredientJS> filter;
	public String translationKey;

	public PartJS(String id, ResourceLocation icon, String texture, @Nullable String brokenTexture, int xPosition, int yPosition, Supplier<IngredientJS> filter, String translationKey) {
		this.id = Util.inputId(id);
		this.icon = icon;
		this.texture = texture;
		this.brokenTexture = brokenTexture;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.filter = filter;
		this.translationKey = translationKey;
	}

	static ResourceLocation iconFromId(String id) {
		return inputId(id);
	}

	// These return strings because the tool also determines part of the texture, so we do not know the full RL yet
	static String toolTextureFromId(String id) {
		return id;
	}

	static int defaultX() {
		return -1;
	}

	static int defaultY() {
		return -1;
	}

	static Supplier<IngredientJS> filterFromId(String id) {
		return () -> ItemStackJS.of(Util.inputId(id));
	}

	static String translationKeyFromId(String id) {
		var rl = inputId(id);
		return "item." + rl.getNamespace() + rl.getPath();
	}

	protected PartJS copy() {
		return new PartJS(id.toString(), icon, texture, brokenTexture, xPosition, yPosition, filter, translationKey);
	}

	protected void optionalUpdatePositions(PartPosition pos) {
		xPosition = xPosition == -1 ? pos.x() : xPosition;
		yPosition = yPosition == -1 ? pos.y() : yPosition;
	}

	protected void updatePositions(PartPosition pos) {
		xPosition = pos.x();
		yPosition = pos.y();
	}

	@NotNull
	static Supplier<IngredientJS> ingredientSupplier(ItemLike item) {
		return () -> new ItemStackJS(new ItemStack(item));
	}

	@Nullable
	public static PartJS of(Object o) {
		if (o instanceof PartJS p) {
			return p;
		} else if (o instanceof CharSequence cs) {
			return PartJSWrapper.of(cs.toString());
		}
		return null;
	}

	public PartJS setX(int x) {
		xPosition = x;
		return this;
	}

	public PartJS setY(int y) {
		yPosition = y;
		return this;
	}

	public PartJS move(int x, int y) {
		xPosition += x;
		yPosition += y;
		return this;
	}

	private ResourceLocation getTextureLocation(String base) {
		if (texture.indexOf(':') != -1) {
			return new ResourceLocation(texture);
		}
		return new ResourceLocation(base + "/" + id.getPath());
	}

	private ResourceLocation getBrokenTextureLocation(String base) {
		assert brokenTexture != null;
		if (brokenTexture.indexOf(':') != -1) {
			return new ResourceLocation(brokenTexture);
		}
		return new ResourceLocation(base + "/broken_" + id.getPath());

	}

	public JsonElement toToolDefinitionJson() {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("item", this.id.toString());
			return jsonObject;
	}
	public JsonElement toStationLayoutJson() {
			JsonObject json = new JsonObject();
			json.addProperty("icon", this.icon.toString());
			json.addProperty("translationKey", this.translationKey);
			json.addProperty("x", this.xPosition);
			json.addProperty("y", this.yPosition);
			json.add("filter", this.filter.get().toJson());
			return json;
	}

	public JsonElement toItemModelJson(int index, JsonObject textureJson, String baseTexture) {
		JsonObject partJson = new JsonObject();

		var textureId = getTextureId(textureJson, "", index);
		var texture = getTexture().indexOf(':') == -1 ? baseTexture + getTexture() : getTexture();
		partJson.addProperty("name", textureId);
		partJson.addProperty("index", index);
		textureJson.addProperty(textureId, texture);
		//FIXME: not addnig broken textureid and using broken texture location for non broken
		if (getBrokenTexture() != null) {
			var brokenTextureId = getTextureId(textureJson, "broken_", index);
			var brokenTexture = getBrokenTexture().indexOf(':') == -1 ? baseTexture + getBrokenTexture() : getBrokenTexture();
			partJson.addProperty("broken", brokenTextureId);
			textureJson.addProperty(brokenTextureId, brokenTexture);
		}

		return partJson;
	}

	private String getTextureId(JsonObject json, String prefix, int index) {
		String textureId = prefix + getPartId();
		if (json.has(textureId)) {
			// If it already has one by this name then add the index to the end, so it is unique
			textureId = textureId + index;
		}
		return textureId;
	}

	private String getPartId() {
		return id.getPath();
	}

	public ResourceLocation getId() {
		return this.id;
	}

	public ResourceLocation getIcon() {
		return this.icon;
	}

	public String getTexture() {
		return this.texture;
	}

	public @Nullable String getBrokenTexture() {
		return this.brokenTexture;
	}

	public int getXPosition() {
		return this.xPosition;
	}

	public int getYPosition() {
		return this.yPosition;
	}

	public Supplier<IngredientJS> getFilter() {
		return this.filter;
	}

	public String getTranslationKey() {
		return this.translationKey;
	}

	public PartJS setIcon(ResourceLocation icon) {
		this.icon = icon;
		return this;
	}

	public PartJS setTexture(String texture) {
		this.texture = texture;
		return this;
	}

	public PartJS setBrokenTexture(@Nullable String brokenTexture) {
		this.brokenTexture = brokenTexture;
		return this;
	}

	public PartJS setXPosition(int xPosition) {
		this.xPosition = xPosition;
		return this;
	}

	public PartJS setYPosition(int yPosition) {
		this.yPosition = yPosition;
		return this;
	}

	public PartJS setFilter(Supplier<IngredientJS> filter) {
		this.filter = filter;
		return this;
	}

	public PartJS setTranslationKey(String translationKey) {
		this.translationKey = translationKey;
		return this;
	}

	public String toString() {
		PartJS defaultValues = PartJSWrapper.DEFAULT_PARTS.get(id.toString());
		if (this.equals(defaultValues)) return "PartJS." + UtilsJS.convertSnakeCaseToCamelCase(id.getPath());

		defaultValues = defaultValues == null ? PartJSWrapper.of(id.toString()) : defaultValues;
		// If it is its default value then ignore it, otherwise append!
		return "PartJS.of('" + (this.getId().getPath().equals("tconstruct") ? this.getPartId() : this.getId()) + "')" +
				(this.getIcon().equals(defaultValues.getIcon()) ? "" :
						".setIcon('" + this.getIcon() + "')") +
				(this.getTexture().equals(defaultValues.getTexture()) ? "" :
						".setTexture('" + this.getTexture() + "')") +
				(this.getBrokenTexture() == null ? "" :
						".setBrokenTexture('" + this.getBrokenTexture() + "')") +
				(this.getXPosition() == defaultValues.getXPosition() ? "" :
						".setX('" + this.getXPosition() + "')") +
				(this.getYPosition() == defaultValues.getYPosition() ? "" :
						".setY('" + this.getYPosition() + "')") +
				(this.getTranslationKey().equals(defaultValues.getTranslationKey()) ? "" :
						".setTranslationKey('" + this.getTranslationKey() + "')");
	}

	// Generated using Lombok
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof final PartJS other)) return false;
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (!Objects.equals(this$id, other$id)) return false;
		final Object this$icon = this.getIcon();
		final Object other$icon = other.getIcon();
		if (!Objects.equals(this$icon, other$icon)) return false;
		final Object this$texture = this.getTexture();
		final Object other$texture = other.getTexture();
		if (!Objects.equals(this$texture, other$texture)) return false;
		final Object this$brokenTexture = this.getBrokenTexture();
		final Object other$brokenTexture = other.getBrokenTexture();
		if (!Objects.equals(this$brokenTexture, other$brokenTexture))
			return false;
		if (this.getXPosition() != other.getXPosition()) return false;
		if (this.getYPosition() != other.getYPosition()) return false;
		final Object this$filter = this.getFilter();
		final Object other$filter = other.getFilter();
		if (!Objects.equals(this$filter, other$filter)) return false;
		final Object this$translationKey = this.getTranslationKey();
		final Object other$translationKey = other.getTranslationKey();
		return Objects.equals(this$translationKey, other$translationKey);
	}
}
