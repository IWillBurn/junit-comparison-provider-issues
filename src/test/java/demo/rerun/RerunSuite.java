package demo.rerun;

import org.junit.platform.suite.api.Select;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Only argument 2 of {@link RerunTest}, selected by its zero-based invocation index like
 * {@code ConsoleLauncher --select-iteration}.
 */
@Suite
@SuiteDisplayName("RerunTest, only argument 2 (IterationSelector)")
@Select({ "iteration:method:demo.rerun.RerunTest$WithoutComparison#test(int,demo.Implementation)[1]",
		"iteration:method:demo.rerun.RerunTest$WithComparison#test(int,demo.Implementation)[1]" })
class RerunSuite {
}
