package io.github.boogiemonster1o1.nomcfluids.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import io.github.boogiemonster1o1.nomcfluids.api.store.FluidStorage;
import io.github.boogiemonster1o1.nomcfluids.api.store.FluidStorageHandler;

public class FluidHandlers {
	private static final HashMap<Predicate<Object>, Function<Object, FluidStorage>> HOLDERS = new LinkedHashMap<>();

	public static <T> void registerHolder(Class<T> clazz, Function<Object, FluidStorage> holderFunction) {
		registerHolder(object -> object.getClass() == clazz, holderFunction);
	}

	public static void registerHolder(Predicate<Object> supports, Function<Object, FluidStorage> holderFunction) {
		HOLDERS.put(supports, holderFunction);
	}

	public static FluidStorageHandler of(Object object, FluidType fluidType) {
		return optionalOf(object, fluidType).orElseThrow(() -> new RuntimeException(String.format("object type (%s) not supported", object.getClass().getName())));
	}

	public static Optional<FluidStorageHandler> optionalOf(Object object, FluidType fluidType) {
		if (object == null) {
			return Optional.empty();
		}
		for (Map.Entry<Predicate<Object>, Function<Object, FluidStorage>> holder : HOLDERS.entrySet()) {
			if (holder.getKey().test(object)) {
				return Optional.of(new FluidStorageHandler(holder.getValue().apply(object), fluidType));
			}
		}
		return Optional.empty();
	}

	public static boolean valid(Object object) {
		for (Predicate<Object> predicate : HOLDERS.keySet()) {
			if (predicate.test(object)) {
				return true;
			}
		}
		return false;
	}

	static {
		registerHolder(object -> object instanceof FluidStorage, object -> (FluidStorage) object);
	}
}
