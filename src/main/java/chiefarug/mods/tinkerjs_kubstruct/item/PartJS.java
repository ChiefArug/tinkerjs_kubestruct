package chiefarug.mods.tinkerjs_kubstruct.item;

import chiefarug.mods.tinkerjs_kubstruct.Util;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

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

	protected void optionalUpdatePositions(PartPosition pos) {
		xPosition = xPosition == -1 ? pos.x : xPosition;
		yPosition = yPosition == -1 ? pos.y : yPosition;
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

	PartModelEntry getTextureLocations(String base, int index) {
		var name = id.getPath();
		var texture = getTextureLocation(base);
		var brokenTexture = getBrokenTextureLocation(base);
		return new PartModelEntry(name, index, texture, brokenTexture);
	}

	record PartModelEntry(String name, int index, ResourceLocation texture, @Nullable ResourceLocation brokenTexture){}
}
