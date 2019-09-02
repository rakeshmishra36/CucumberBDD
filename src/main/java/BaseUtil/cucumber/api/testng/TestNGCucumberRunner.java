package BaseUtil.cucumber.api.testng;

import cucumber.api.event.TestRunFinished;
import cucumber.api.event.TestRunStarted;
import BaseUtil.cucumber.runner.*;
import cucumber.runtime.*;
import cucumber.runtime.filter.Filters;
import cucumber.runtime.formatter.PluginFactory;
import cucumber.runtime.formatter.Plugins;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import BaseUtil.cucumber.runtime.model.CucumberFeature;
import BaseUtil.cucumber.runtime.model.FeatureLoader;
import gherkin.events.PickleEvent;
import BaseUtil.cucumber.runtime.FeaturePathFeatureSupplier;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import BaseUtil.CommonMethod;
import BaseUtil.cucumber.api.testng.CucumberFeatureWrapperImpl;
import BaseUtil.cucumber.api.testng.PickleEventWrapperImpl;

/**
 * Glue code for running Cucumber via TestNG.
 */
public class TestNGCucumberRunner extends CommonMethod {
	private final EventBus bus;
	private final Filters filters;
	private final FeaturePathFeatureSupplier featureSupplier;
	private final ThreadLocalRunnerSupplier runnerSupplier;
	private final RuntimeOptions runtimeOptions;
	private final Plugins plugins;

	/**
	 * Bootstrap the cucumber runtime
	 *
	 * @param clazz Which has the {@link cucumber.api.CucumberOptions} and
	 *              {@link org.testng.annotations.Test} annotations
	 * @throws IOException 
	 */
	public TestNGCucumberRunner(Class clazz) throws IOException {
		ClassLoader classLoader = clazz.getClassLoader();
		ResourceLoader resourceLoader = new MultiLoader(classLoader);

		RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
		runtimeOptions = runtimeOptionsFactory.create();
		
		addTagToRunner(runtimeOptions);
		
		ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
		BackendModuleBackendSupplier backendSupplier = new BackendModuleBackendSupplier(resourceLoader, classFinder,
				runtimeOptions);
		bus = new TimeServiceEventBus(TimeService.SYSTEM);
		plugins = new Plugins(classLoader, new PluginFactory(), runtimeOptions);
		FeatureLoader featureLoader = new FeatureLoader(resourceLoader);
		filters = new Filters(runtimeOptions);
		this.runnerSupplier = new ThreadLocalRunnerSupplier(runtimeOptions, bus, backendSupplier);
		featureSupplier = new FeaturePathFeatureSupplier(featureLoader, runtimeOptions);
	}

	private void addTagToRunner(RuntimeOptions runtimeOptions) throws IOException {
		getPropValues();
		List<List<String>> allTagNames = readExcel("./src/main/resources/", "FeatureConfiguration.xlsx", "Tags");
		List<String> modifiedTagNames = new ArrayList<>();;
		int size = allTagNames.size();
		String tagName = System.getProperty("TagName");
		String tagFromExcelIn;
		String tagFromExcelOut = System.getProperty("TagFromExcel");
		
		if (!isNullOrEmpty(tagFromExcelOut)) {
			tagFromExcelIn = tagFromExcelOut;
		}else {
			tagFromExcelIn = prop.getProperty("TagFromExcel");
		} 

		if (tagFromExcelIn.equalsIgnoreCase("Y") && tagName == null) {
			System.out.println("<<<<<<<<<< TAGS ARE ADDED FROM EXCELSHEET >>>>>>>>>>");
			for (int i = 0; i < size; i++) {
				if (allTagNames.get(i).get(1).equalsIgnoreCase("Y")) {
					modifiedTagNames.add("@"+allTagNames.get(i).get(0));					
				}				
			}
			String tags = String.join(",", modifiedTagNames);
			runtimeOptions.getTagFilters().add(tags);
			
		} else if (tagFromExcelIn.equalsIgnoreCase("N") && tagName == null) {
			System.out.println("<<<<<<<<<< DEFAULT TAGS ARE ADDED >>>>>>>>>>");
			
		} else {
			System.out.println("<<<<<<<<<< TAG IS BEING EXECUTED >>>>>>>>>> "+ tagName);
			runtimeOptions.getTagFilters().add(tagName);
		}
	}

	public void runScenario(PickleEvent pickle) throws Throwable {
		// Possibly invoked in a multi-threaded context
		Runner runner = runnerSupplier.get();
		TestCaseResultListener testCaseResultListener = new TestCaseResultListener(runner.getBus(), runtimeOptions.isStrict());
		runner.runPickle(pickle);
		testCaseResultListener.finishExecutionUnit();

		if (!testCaseResultListener.isPassed()) {
			throw testCaseResultListener.getError();
		}
	}

	public void finish() {
		bus.send(new TestRunFinished(bus.getTime(), bus.getTimeMillis()));
	}

	/**
	 * @return returns the cucumber scenarios as a two dimensional array of
	 *         {@link PickleEventWrapper} scenarios combined with their
	 *         {@link CucumberFeatureWrapper} feature.
	 * @throws IOException
	 */
	public Object[][] provideScenarios() throws IOException {
		try {
			List<Object[]> scenarios = new ArrayList<Object[]>();
			List<CucumberFeature> features = getFeatures();
			List<List<String>> allScenarios = readExcel("./src/main/resources/", "FeatureConfiguration.xlsx",
					"Scenarios");
			int size = allScenarios.size();
			String scenarioName = System.getProperty("ScenarioName");
			String scenFromExcelIn;
			String scenFromExcelOut = System.getProperty("ScenarioFromExcel");
			
			if (!isNullOrEmpty(scenFromExcelOut)) {
				scenFromExcelIn = scenFromExcelOut;
			}else {
				scenFromExcelIn = prop.getProperty("ScenarioFromExcel");
			} 

			for (CucumberFeature feature : features) {
				if (scenFromExcelIn.equalsIgnoreCase("Y") && scenarioName == null) {
					for (PickleEvent pickle : feature.getPickles()) {
						for (int i = 0; i < size; i++) {
							if (allScenarios.get(i).get(2).equalsIgnoreCase("Y")) {
								if (allScenarios.get(i).get(1).equalsIgnoreCase(pickle.pickle.getName())) {
									scenarios.add(new Object[] { new PickleEventWrapperImpl(pickle),
											new CucumberFeatureWrapperImpl(feature) });									
									break;
								}								
							}
						}
					}
					System.out.println("<<<<<<<<<< SCENARIOS ARE ADDED FROM EXCELSHEET >>>>>>>>>>");
					
				} else if (scenFromExcelIn.equalsIgnoreCase("N") && scenarioName == null) {
					
					for (PickleEvent pickle : feature.getPickles()) {
						if (filters.matchesFilters(pickle)) {
							scenarios.add(new Object[] { new PickleEventWrapperImpl(pickle),
									new CucumberFeatureWrapperImpl(feature) });
						}
					}
					System.out.println("<<<<<<<<<< ALL SCENARIOS ARE ADDED >>>>>>>>>>");
					
				} else {
					for (PickleEvent pickle : feature.getPickles()) {
						if (scenarioName.equalsIgnoreCase(pickle.pickle.getName())) {
							System.out.println("<<<<<<<<<< SCENARIO IS BEING EXECUTED >>>>>>>>>> "+ pickle.pickle.getName());
							scenarios.add(new Object[] { new PickleEventWrapperImpl(pickle),new CucumberFeatureWrapperImpl(feature) });
						}						
					}
				}				
			}
			return (Object[][]) scenarios.toArray(new Object[][] {});
		} catch (CucumberException e) {
			return new Object[][] { new Object[] { new CucumberExceptionWrapper(e), null } };
		}
	}

	List<CucumberFeature> getFeatures() throws IOException {
		getPropValues();
		plugins.setSerialEventBusOnEventListenerPlugins(bus);
		List<CucumberFeature> features = featureSupplier.get();
		List<CucumberFeature> modifiedFeatures = new ArrayList<>();		
		List<List<String>> allFeatures = readExcel("./src/main/resources/", "FeatureConfiguration.xlsx","Features");
		int size = allFeatures.size();
		String featureName = System.getProperty("FeatureName");
		String featureExcelSheetIn;
		String featureExcelSheetOut = System.getProperty("FeatureFromExcel");
		
		if (!isNullOrEmpty(featureExcelSheetOut)) {
			featureExcelSheetIn = featureExcelSheetOut;
		}else {
			featureExcelSheetIn = prop.getProperty("FeatureFromExcel");
		} 

		if (featureExcelSheetIn.equalsIgnoreCase("Y") && featureName == null) {
			System.out.println("<<<<<<<<<< FEATURES ARE ADDED FROM EXCELSHEET >>>>>>>>>>");
			for (CucumberFeature feature : features) {				
				for (int i = 0; i < size; i++) {
					if (allFeatures.get(i).get(1).equalsIgnoreCase("Y")) {
						if (allFeatures.get(i).get(0)
								.equalsIgnoreCase(feature.getGherkinFeature().getFeature().getName())) {
							modifiedFeatures.add(feature);
							break;
						}
					}
				}
			}
			
		} else if (featureExcelSheetIn.equalsIgnoreCase("N") && featureName == null) {
			modifiedFeatures.addAll(features);
			System.out.println("<<<<<<<<<< ALL FEATURES ARE ADDED >>>>>>>>>>");
			
		} else {
			for (CucumberFeature feature : features) {
				if (featureName.equalsIgnoreCase(feature.getGherkinFeature().getFeature().getName())) {
					modifiedFeatures.add(feature);
					System.out.println("<<<<<<<<<< FEATURE IS BEING EXECUTED >>>>>>>>>> "+ feature.getGherkinFeature().getFeature().getName());
				}
			}
		}
			 
		bus.send(new TestRunStarted(bus.getTime(), bus.getTimeMillis()));
		for (CucumberFeature feature : modifiedFeatures) {
			feature.sendTestSourceRead(bus);
		}
		return modifiedFeatures;
	}
}