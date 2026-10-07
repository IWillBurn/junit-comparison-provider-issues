package demo;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateComparisonProvider;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

/**
 * Comparison provider from PR #6127, like the example in its user guide: every test template with an
 * {@link Implementation} parameter runs against "implementation A" and "implementation B".
 */
public class TwoImplementations implements TestTemplateComparisonProvider {

	@Override
	public boolean supportsComparison(ExtensionContext context) {
		return List.of(context.getRequiredTestMethod().getParameterTypes()).contains(Implementation.class);
	}

	@Override
	public Stream<TestTemplateInvocationContext> provideComparisonSubjects(ExtensionContext context) {
		return Stream.of("A", "B").map(name -> new TestTemplateInvocationContext() {
			@Override
			public String getDisplayName(int invocationIndex) {
				return "implementation " + name;
			}

			@Override
			public List<Extension> getAdditionalExtensions() {
				return List.of(new OneImplementation(name));
			}
		});
	}

}
