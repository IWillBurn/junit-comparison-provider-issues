package demo.counting;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.RetryingTest;

import demo.Implementation;
import demo.OneImplementation;
import demo.TwoImplementations;

/**
 * Each implementation fails its first attempt and passes the second one. Run this class on its own.
 */
@DisplayName("JUnit Pioneer @RetryingTest(2): first attempt fails")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class RetryTest {

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

		static final Map<String, Integer> attempts = new HashMap<>();

		@RetryingTest(2)
		void test(Implementation implementation) {
			int attempt = attempts.merge(getClass().getSimpleName() + " " + implementation.name(), 1, Integer::sum);
			assertTrue(attempt > 1, implementation.name() + " fails its first attempt");
		}

	}

}
