# LibMetrics

A simple java helper library for collecting application runtime metrics.

## Maven Dependency

Include the library in your project by adding the following dependency to your pom.xml

```
<dependency>
	<groupId>com.mclarkdev.tools</groupId>
	<artifactId>libmetrics</artifactId>
	<version>1.5.1</version>
</dependency>
```

## Example

Hit counters from anywhere in your code to track important metrics.

```
// Increment counter by (1)
LibMetrics.instance().hitCounter("a", "b", "c");

// Increment counter by (N)
LibMetrics.instance().hitCounter(N, "a", "b", "c");

// Set the value of a counter
LibMetrics.instance().setValue(N, "a", "b", "c");

// Retrieve the value of a counter
double d = LibMetrics.instance().getCounter("a", "b", "c");

// Delete a whole counter node
LibMetrics.instance().deleteNode("a", "b");

// Retrieve all counters in JSON format
LibMetrics.instance().getDetails();

// Track each time a specific method is run
LibMetrics.hitMethodRunCounter();
```

# License

Open source & free for all. ❤
