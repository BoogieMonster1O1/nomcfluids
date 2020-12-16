package io.github.boogiemonster1o1.nomcfluids.api.util;

public enum Side {
	DOWN,
	UP,
	NORTH,
	SOUTH,
	WEST,
	EAST,
	UNKNOWN;

	private static final Side[] VALUES = values();

	public static Side fromMinecraft(Enum<?> e){
		if(e == null){
			return UNKNOWN;
		}
		return VALUES[e.ordinal()];
	}

	public <T extends Enum<T>> T toMinecraft(Class<T> clazz) {
		return clazz.getEnumConstants()[this.ordinal()];
	}
}
