package com.fuze.coreuc.fcc.api.serialization;

import com.google.gson.GsonBuilder;
import com.thinkingphones.absolute.api.serialization.GsonProvider;
import io.gsonfire.GsonFireBuilder;

import java.util.Map;

/**
 * Created by asanchez on 24/03/2017.
 */
public class CustomGsonProvider extends GsonProvider {

	@Override
	protected GsonBuilder createGsonBuilder() {
		return new GsonFireBuilder()
				.enableExposeMethodResult()
				.createGsonBuilder()
				.registerTypeHierarchyAdapter(Map.class, new MapTypeAdapter<>());
	}
}
