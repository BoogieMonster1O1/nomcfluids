package io.github.boogiemonster1o1.nomcfluids.api;

import java.util.Objects;

import io.github.boogiemonster1o1.nomcfluids.api.settings.FluidSettings;

public class FluidType {
	private final String id;
	private final FluidSettings settings;

	FluidType(String id, FluidSettings settings) {
		this.id = id;
		this.settings = settings;
	}

	public String getId() {
		return this.id;
	}

	public FluidSettings getSettings() {
		return this.settings;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FluidType)) return false;
		FluidType fluidType = (FluidType) o;
		return Objects.equals(this.id, fluidType.id) &&
				Objects.equals(this.settings, fluidType.settings);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.settings);
	}

	@Override
	public String toString() {
		return "FluidType{" + "id='" + this.id + '\'' +
				", settings=" + this.settings +
				'}';
	}
}
