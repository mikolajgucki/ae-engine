package com.andcreations.ae.studio.plugin;

import com.google.gson.Gson;

/**
 * Represents a plugin state.
 * 
 * @author Mikolaj Gucki
 */
public abstract class PluginState {
	/**
	 * Serializes state to a string.
	 * 
	 * @param state The state.
	 * @return The serialized string.
	 */
	public static String serialize(Object state) {
		if (state == null) {
			return null;
		}
		
		Gson gson = new Gson();
		return gson.toJson(state);
	}
	
	/**
	 * Deserializes state.
	 * 
	 * @param stateStr The serialized state.
	 * @param clazz The state class.
	 * @return The state.
	 */
	public static <T>T deserialize(String stateStr,Class<T> clazz) {
		if (stateStr == null) {
			return null;
		}
		
		Gson gson = new Gson();
		return gson.fromJson(stateStr,clazz);
	}
}
