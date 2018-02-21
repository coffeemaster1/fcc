package com.fuze.coreuc.fcc.api.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by asanchez on 28/03/2017.
 */
public class MapTypeAdapter<T extends Map<String, T>> implements JsonSerializer<Map<String, T>> {

	@Override
	public JsonElement serialize(Map<String, T> src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src.values());
	}
}
