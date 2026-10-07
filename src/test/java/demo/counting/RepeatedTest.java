package demo.counting;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import demo.Implementation;
import demo.OneImplementation;
import demo.TwoImplementations;

@DisplayName("@RepeatedTest(value = 3, failureThreshold = 2): repetition 1 fails")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class RepeatedTest {

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

		@org.junit.jupiter.api.RepeatedTest(value = 3, failureThreshold = 2)
		void test(RepetitionInfo repetition, Implementation implementation) {
			assertNotEquals(1, repetition.getCurrentRepetition(), implementation.name() + " fails in repetition 1");
		}

	}

}
