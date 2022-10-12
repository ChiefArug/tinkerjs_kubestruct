package chiefarug.mods.tinkerjs_kubstruct.item;

import java.util.List;

public class PartPosition {

	public static final PartPosition[] CENTERED_LAYOUT = {new PartPosition(37,45)};
	public static final PartPosition[] DAGGER_LAYOUT = {new PartPosition(21, 53), new PartPosition(39, 35)};
	public static final PartPosition[] AXE_LAYOUT = {new PartPosition(22, 53), new PartPosition(51, 34), new PartPosition(31, 22)};
	public static final PartPosition[] HAMMER_LAYOUT = {new PartPosition(21, 52), new PartPosition(44, 29), new PartPosition(50, 48), new PartPosition(25, 20)};
	public static final PartPosition[][] DEFAULT_POSITIONS = {{},
			CENTERED_LAYOUT,
			DAGGER_LAYOUT,
			AXE_LAYOUT,
			HAMMER_LAYOUT
	};

	public static final PartPosition[] PICKAXE_LAYOUT = {new PartPosition(15, 60), new PartPosition(33, 42), new PartPosition(53, 22)};
	public static final PartPosition[] SWORD_LAYOUT = {new PartPosition(12, 62), new PartPosition(30, 44), new PartPosition(48, 26)};
	public static final PartPosition[] CLEAVER_LAYOUT = {new PartPosition(7, 62), new PartPosition(25, 46), new PartPosition(45, 46), new PartPosition(45, 26)};

	public static void apply(List<PartJS> list, PartPosition[] layout) {
		assert list.size() == layout.length;
		for (int i = 0; i < layout.length; i++) {
			list.get(i).optionalUpdatePositions(layout[i]);
		}
	}

	public final int x;
	public final int y;

	public PartPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
