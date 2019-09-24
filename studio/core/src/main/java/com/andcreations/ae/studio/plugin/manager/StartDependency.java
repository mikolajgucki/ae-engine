package com.andcreations.ae.studio.plugin.manager;

/**
 * Represents a start dependency,
 * 
 * @author Mikolaj Gucki
 */
public class StartDependency {
	/** The identifier of the plugin required to be started.*/
	private String pluginId;
	
	/**
	 * Constructs a {@link StartDependency}.
	 * 
	 * @param pluginId The identifier of the plugin required to be started.
	 */
	public StartDependency(String pluginId) {
		this.pluginId = pluginId;
	}
	
	/**
	 * Gets the identifier of the plugin required to be started.
	 * 
	 * @return The plugin identifier.
	 */
	public String getPluginId() {
		return pluginId;
	}
}
