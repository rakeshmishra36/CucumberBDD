package BaseUtil.cucumber.api.testng;

import org.testng.SkipException;

import cucumber.api.Result;
import cucumber.api.event.Event;
import cucumber.api.event.EventHandler;
import cucumber.api.event.TestCaseFinished;
import BaseUtil.cucumber.runner.EventBus;
import cucumber.runtime.CucumberException;

class TestCaseResultListener {
    static final String UNDEFINED_MESSAGE = "There are undefined steps";
    static final String SKIPPED_MESSAGE = "This scenario is skipped";
    private final EventBus bus;
    private boolean strict;
    private Result result;
    private final EventHandler testCaseFinishedHandler = new EventHandler() {
      

	@Override
	public void receive(Event event) {
		// TODO Auto-generated method stub
		
	}
    };

    TestCaseResultListener(EventBus eventBus, boolean strict) {
        this.strict = strict;
        this.bus = eventBus;
        eventBus.registerHandlerFor(TestCaseFinished.class, testCaseFinishedHandler);
    }

    void finishExecutionUnit() {
        bus.removeHandlerFor(TestCaseFinished.class, testCaseFinishedHandler);
    }


    void receiveResult(Result result) {
        this.result = result;
    }

    boolean isPassed() {
        return result == null || result.is(Result.Type.PASSED);
    }

    Throwable getError() {
        if (result == null) {
            return null;
        }
        switch (result.getStatus()) {
        case FAILED:
        case AMBIGUOUS:
            return result.getError();
        case PENDING:
            if (strict) {
                return result.getError();
            } else {
                return new SkipException(result.getErrorMessage(), result.getError());
            }
        case UNDEFINED:
            if (strict) {
                return new CucumberException(UNDEFINED_MESSAGE);
            } else {
                return new SkipException(UNDEFINED_MESSAGE);
            }
        case SKIPPED:
            Throwable error = result.getError();
            if (error != null) {
                if (error instanceof SkipException) {
                    return error;
                } else {
                    return new SkipException(result.getErrorMessage(), error);
                }
            } else {
                return new SkipException(SKIPPED_MESSAGE);
            }
        case PASSED:
            return null;
        default:
            throw new IllegalStateException("Unexpected result status: " + result.getStatus());
        }
    }

}