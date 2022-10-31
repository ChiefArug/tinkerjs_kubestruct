package chiefarug.mods.tinkerjs_kubestruct;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.io.File;

import static chiefarug.mods.tinkerjs_kubestruct.TinkerJSKubestruct.MODID;

@Mod(MODID)
public class TinkerJSKubestruct {

    public static final String MODID = "tinkerjs_kubestruct";
	@SuppressWarnings("unused")
	public static final Logger LGGR = LogUtils.getLogger();

	public TinkerJSKubestruct() {

    }

	public static File getFileByID(String path) {
		return ModList.get().getModFileById(MODID).getFile().findResource(path).toFile();
	}
}
