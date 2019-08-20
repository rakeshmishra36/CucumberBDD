package BaseUtil.cucumber.runner;

import cucumber.api.Pending;
import cucumber.api.Result;
import cucumber.api.TestCase;
import cucumber.api.event.TestStepFinished;
import cucumber.api.event.TestStepStarted;
import cucumber.runtime.StepDefinitionMatch;

import java.io.IOException;
import java.util.Arrays;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import BaseUtil.CommonMethod;

abstract class TestStep extends CommonMethod implements cucumber.api.TestStep {
    private static final String[] ASSUMPTION_VIOLATED_EXCEPTIONS = {
        "org.junit.AssumptionViolatedException",
        "org.junit.internal.AssumptionViolatedException",
        "org.testng.SkipException"
    };

    static {
        Arrays.sort(ASSUMPTION_VIOLATED_EXCEPTIONS);
    }

    private final StepDefinitionMatch stepDefinitionMatch;

    TestStep(StepDefinitionMatch stepDefinitionMatch) {
        this.stepDefinitionMatch = stepDefinitionMatch;
    }

    @Override
    public String getCodeLocation() {
        return stepDefinitionMatch.getCodeLocation();
    }

    /**
     * Runs a test step.
     *
     * @param testCase
     * @param bus
     * @param scenario
     * @param skipSteps
     * @return true iff subsequent skippable steps should be skipped
     * @throws IOException 
     */
    boolean run(TestCase testCase, EventBus bus, Scenario scenario, boolean skipSteps) throws IOException {
        Long startTimeMillis = bus.getTimeMillis();
        Long startTimeNanos = bus.getTime();
        bus.send(new TestStepStarted(startTimeNanos, startTimeMillis, testCase, this));
        Result.Type status;
        Throwable error = null;
        try {
            status = executeStep(scenario, skipSteps);
        } catch (Throwable t) {
            error = t;
            status = mapThrowableToStatus(t);
            if( Result.Type.FAILED.name().equalsIgnoreCase("FAILED") && driverClosed == false) {
            	scenario.embed(fullPageScreenshot(), "image/jpeg");
            	try {
        			inputStream.close();
        		} catch (IOException e) {
        			System.out.println("Exception >> " + e.getMessage());
        		} finally {			
        			driver.quit();
        			driverClosed = true;
        		}	
            }            
        }
        Long stopTimeNanos = bus.getTime();
        Long stopTimeMillis = bus.getTimeMillis();
        Result result = mapStatusToResult(status, error, stopTimeNanos - startTimeNanos);
        scenario.add(result);
        bus.send(new TestStepFinished(stopTimeNanos, stopTimeMillis, testCase, this, result));
        return !result.is(Result.Type.PASSED);
    }

    private Result.Type executeStep(Scenario scenario, boolean skipSteps) throws Throwable {
        if (!skipSteps) {
            stepDefinitionMatch.runStep(scenario);
            if (!(driverClosed == true)) {
             	scenario.embed(visiblePageScreenshot(), "image/jpeg");
            }            
            return Result.Type.PASSED;            
        } else {
            stepDefinitionMatch.dryRunStep(scenario);
            return Result.Type.SKIPPED;
        }
    }

    private Result.Type mapThrowableToStatus(Throwable t) {
        if (t.getClass().isAnnotationPresent(Pending.class)) {
            return Result.Type.PENDING;
        }
        if (Arrays.binarySearch(ASSUMPTION_VIOLATED_EXCEPTIONS, t.getClass().getName()) >= 0) {
            return Result.Type.SKIPPED;
        }
        if (t.getClass() == UndefinedStepDefinitionException.class) {
            return Result.Type.UNDEFINED;
        }
        if (t.getClass() == AmbiguousStepDefinitionsException.class) {
            return Result.Type.AMBIGUOUS;
        }
        return Result.Type.FAILED;
    }

    private Result mapStatusToResult(Result.Type status, Throwable error, long duration) {
        if (status == Result.Type.UNDEFINED) {
            return Result.UNDEFINED;
        }
        return new Result(status, duration, error);
    }
}
