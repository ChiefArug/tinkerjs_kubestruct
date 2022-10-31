package chiefarug.mods.tinkerjs_kubestruct;

import chiefarug.mods.tinkerjs_kubestruct.item.ModifiableItemBuilder;
import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.metadata.pack.PackMetadataSectionSerializer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static chiefarug.mods.tinkerjs_kubestruct.TinkerJSKubestruct.MODID;
import static net.minecraft.server.packs.PackType.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID)
public class VirtualPacks {

	public static final VirtualPacks INSTANCE = new VirtualPacks();

	private final List<ModifiableItemBuilder> buildersToGenerate = new ArrayList<>();
	private VirtualPackResources assets = new VirtualPackResources(PackType.CLIENT_RESOURCES);
	private VirtualPackResources data = new VirtualPackResources(SERVER_DATA);
	private final Map<PackType, VirtualPackResources> resourcesMap = Map.of(CLIENT_RESOURCES, assets, SERVER_DATA, data);

	@SubscribeEvent
	static void addPacks(AddPackFindersEvent event) {
		PackType type = event.getPackType();
		event.addRepositorySource((consumer, constructor) -> consumer.accept(constructor.create(
				"generated:tinkerjs_kubestruct_" + getPackName(type),
				new TextComponent("TinkerJS Kubestruct generated " + getPackName(type)),
				true, //what does this do? Think it's if the pack is enabled by default
				() -> INSTANCE.resourcesMap.get(type),
				getPackMetaData(type),
				Pack.Position.BOTTOM,
				component -> component.copy().withStyle(ChatFormatting.BLUE), // Cause we are cool, style our generated data blue #trendsetter
				true
		)));
	}

	public void addBuilder(ModifiableItemBuilder builder) {
		buildersToGenerate.add(builder);
	}

	public void clearAssets() {
		this.assets = new VirtualPackResources(CLIENT_RESOURCES);
	}

	public void clearData() {
		this.data = new VirtualPackResources(SERVER_DATA);
	}

	public void clearBuilders() {
		this.buildersToGenerate.clear();
	}

	public void generateDataJsons() {
		var gen = new DataJsonGenerator(this.data.getResourceMap());
		for (ModifiableItemBuilder modifiableItemBuilder : buildersToGenerate) {
			modifiableItemBuilder.generateDataJsons(gen);
		}
	}
	public void generateAssetJsons() {
		var gen = new AssetJsonGenerator(this.assets.getResourceMap());
		for (ModifiableItemBuilder modifiableItemBuilder : buildersToGenerate) {
			modifiableItemBuilder.generateAssetJsons(gen);
		}
	}

	private class VirtualPackResources implements PackResources {

		private final Map<ResourceLocation, JsonElement> resources;
		private final PackType type;

		public VirtualPackResources(PackType type) {
			this.resources = new HashMap<>();
			this.type = type;
		}

		Map<ResourceLocation, JsonElement> getResourceMap() {
			return resources;
		}

		private void verifyPackType(PackType t) {
			if (t != this.type) {
				throw new PackTypeException(this.type, t);
			}
		}

		@Nullable
		@Override
		public InputStream getRootResource(@NotNull String location) throws IOException {
			if (location.equals("pack.png")) {
				return new FileInputStream(TinkerJSKubestruct.getFileByID(location));
			}
			return null;
		}

		@Override
		@NotNull
		public InputStream getResource(@NotNull PackType type, @NotNull ResourceLocation location) throws IOException {
			verifyPackType(type);
			if (location.getPath().endsWith(".json")) {
				JsonElement json = this.resources.get(location);
				if (json != null) {
					return new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8));
				}
			}
			throw new VirtualResourcePackFileNotFoundException(location);
		}

		@Override
		@NotNull
		public Collection<ResourceLocation> getResources(@NotNull PackType type, @NotNull String namespace, @NotNull String path, int maxDepth, @NotNull Predicate<String> filter) {
			verifyPackType(type);
			return resources.keySet().stream()
					.filter(resourceLocation -> resourceLocation.getNamespace().equals(namespace))
					.filter(resourceLocation -> resourceLocation.getPath().startsWith(path))
					.filter(resourceLocation -> filter.test(resourceLocation.toString()))
					.collect(Collectors.toSet());
		}

		@Override
		public boolean hasResource(@NotNull PackType type, @NotNull ResourceLocation rl) {
			return this.type == type && this.resources.containsKey(rl);
		}

		@Override
		@NotNull
		public Set<String> getNamespaces(@NotNull PackType p_10283_) {
			return VirtualPacks.this.buildersToGenerate.stream().map(builder -> builder.id.getNamespace()).collect(Collectors.toSet());
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T> T getMetadataSection(@NotNull MetadataSectionSerializer<T> serializer) {
			if (serializer instanceof PackMetadataSectionSerializer) {
				return (T) getPackMetaData(this.type);
			}
			return null;
		}

		@Override
		@NotNull
		public String getName() {
			return "tinkerjs_kubestruct_generated_" + getPackName(type);
		}

		@Override
		public void close() {
		}

	}

	@NotNull
	private static String getPackName(PackType packType) {
		return packType.getDirectory();
	}

	private static PackMetadataSection getPackMetaData(PackType type) {
		return new PackMetadataSection(
			new TextComponent("Stuff for tinkers tools made with TinkerJS Kubestruct because KubeJS doesn't provide it in a way TiC recognises"),
			Util.getCurrentPackVersion(type)
		);
	}

	private static class VirtualResourcePackFileNotFoundException extends FileNotFoundException {
		public VirtualResourcePackFileNotFoundException(ResourceLocation location) {
			super("No virtual file found at " + location + " for TinkerJS Kubestruct virtual resource pack");
		}
	}

	private static class PackTypeException extends IllegalArgumentException {
		public PackTypeException(PackType current, PackType triedLoad) {
			super("TinkerJS Kubestruct virtual " + getPackName(current) + " pack cannot load resources for pack of type" + getPackName(triedLoad) + "!");
		}
	}

}
