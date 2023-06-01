package com.mclarkdev.tools.libmetrics;

import java.util.HashMap;

import org.json.JSONObject;;

public class LibMetrics {

	private static final HashMap<String, LibMetrics> caches = new HashMap<>();

	private final JSONObject counterCache;

	private LibMetrics() {
		counterCache = new JSONObject();
	}

	/**
	 * Increment the value of a single counter by one.
	 * 
	 * @param name
	 */
	public void hitCounter(String... name) {
		hitCounter(1, name);
	}

	/**
	 * Increment the value of a single counter by the specified value.
	 * 
	 * @param name
	 */
	public void hitCounter(double count, String... name) {
		if (name == null) {
			throw new IllegalArgumentException("name cannot be null");
		}

		synchronized (counterCache) {

			JSONObject outer = counterCache;
			for (int x = 0; x < name.length - 1; x++) {

				JSONObject inner = outer.optJSONObject(name[x]);
				if (inner == null) {

					inner = new JSONObject();
					outer.put(name[x], inner);
				}
				outer = inner;
			}

			String key = name[name.length - 1];
			double current = outer.optDouble(key, 0);
			outer.put(key, current + count);
		}
	}

	/**
	 * Get a single counter by name.
	 * 
	 * @param name
	 * @return
	 */
	public double getCounter(String... name) {
		if (name == null) {
			throw new IllegalArgumentException("name cannot be null");
		}

		JSONObject outer = counterCache;
		for (String key : name) {

			JSONObject inner = outer.optJSONObject(key);
			if (inner == null) {

				inner = new JSONObject();
				outer.put(key, inner);
			}
			outer = inner;
		}

		return outer.getDouble("value");
	}

	/**
	 * Set the value for a counter.
	 * 
	 * @param name
	 * @param value
	 */
	public void setValue(Object value, String... name) {
		if (name == null) {
			throw new IllegalArgumentException("name cannot be null");
		}

		synchronized (counterCache) {

			JSONObject outer = counterCache;
			for (int x = 0; x < name.length - 1; x++) {

				JSONObject inner = outer.optJSONObject(name[x]);
				if (inner == null) {

					inner = new JSONObject();
					outer.put(name[x], inner);
				}
				outer = inner;
			}

			String key = name[name.length - 1];
			outer.put(key, value);
		}
	}

	/**
	 * Delete a whole node from the metrics tree.
	 * 
	 * @param node
	 * @return
	 */
	public boolean deleteNode(String... node) {
		if (node == null) {
			throw new IllegalArgumentException("node cannot be null");
		}

		synchronized (counterCache) {

			JSONObject outer = counterCache;
			for (int x = 0; x < node.length - 1; x++) {

				JSONObject inner = outer.optJSONObject(node[x]);
				if (inner == null) {

					inner = new JSONObject();
					outer.put(node[x], inner);
				}
				outer = inner;
			}

			// Remove and return success
			String rem = node[node.length - 1];
			Object o = outer.remove(rem);
			return (o != null);
		}
	}

	/**
	 * Get the details of the counter cache.
	 * 
	 * @return
	 */
	public JSONObject getDetails() {

		return counterCache;
	}

	/**
	 * Hit a run counter for the method currently being executed.
	 */
	public static void hitMethodRunCounter() {
		StackTraceElement e = new Throwable().getStackTrace()[1];
		String[] classes = (e.getClassName() + "." + e.getMethodName()).split("\\.");
		instance("methods").hitCounter(classes);
	}

	/**
	 * Get a reference to the default metrics collector.
	 * 
	 * @return
	 */
	public static LibMetrics instance() {
		return instance(null);
	}

	/**
	 * Get a reference to a named metrics collector.
	 * 
	 * @return
	 */
	public static synchronized LibMetrics instance(String cache) {
		cache = (cache != null) ? cache : "default";

		if (!caches.containsKey(cache)) {
			caches.put(cache, new LibMetrics());
		}
		return caches.get(cache);
	}
}
