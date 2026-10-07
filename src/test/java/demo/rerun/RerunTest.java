package demo.rerun;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import demo.Implementation;
import demo.OneImplementation;
import demo.TwoImplementations;

/**
 * All arguments. {@link RerunSuite} runs only argument 2 of the same tests.
 */
@DisplayName("@ParameterizedTest: argument 2 fails")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class RerunTest {

	@Nested
	@Order(1)
	@DisplayName("without comparison (implementation A)")
	@ExtendWith(OneImplementation.class)
	class WithoutComparison extends Scenario {
	}

	@Nested
	@Order(2)
	@DisplayName("with comparison (implementations A and B)")
	@ExtendWith(TwoImplementations.class)
	class WithComparison extends Scenario {
	}

	abstract static class Scenario {

		@ParameterizedTest(name = "argument {0}")
		@ValueSource(ints = { 1, 2 })
		void test(int argument, Implementation implementation) {
			assertNotEquals(2, argument, implementation.name() + " fails for argument 2");
		}

	}

}
