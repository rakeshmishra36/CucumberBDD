package BaseUtil.cucumber.api.testng;

import gherkin.events.PickleEvent;

/**
 * The only purpose of this interface is to be able to provide a custom
 * 
toString()
, making TestNG reports look more descriptive.
 *
 * @see AbstractTestNGCucumberTests#runScenario(cucumber.api.testng.PickleEventWrapper, cucumber.api.testng.CucumberFeatureWrapper)
 */
public interface PickleEventWrapper {

    PickleEvent getPickleEvent();

}