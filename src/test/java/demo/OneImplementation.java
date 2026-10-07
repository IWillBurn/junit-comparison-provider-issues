package demo;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver;

/**
 * Plain parameter resolver without comparison: injects "implementation A".
 */
public class OneImplementation extends TypeBasedParameterResolver<Implementation> {

	private final Implementation implementation;

	public OneImplementation() {
		this("A");
	}

	OneImplementation(String name) {
		this.implementation = new Implementation(name);
	}

	@Override
	public Implementation resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
		return this.implementation;
	}

}
