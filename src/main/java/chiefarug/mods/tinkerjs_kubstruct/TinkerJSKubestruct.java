package chiefarug.mods.tinkerjs_kubstruct;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;

import static chiefarug.mods.tinkerjs_kubstruct.TinkerJSKubestruct.MODID;

@Mod(MODID)
public class TinkerJSKubestruct {

    public static final String MODID = "tinkerjs_kubestruct";
	private static final Logger LGGR = LogUtils.getLogger();

	public TinkerJSKubestruct() {

    }
}
