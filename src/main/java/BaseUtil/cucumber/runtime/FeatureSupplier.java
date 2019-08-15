package BaseUtil.cucumber.runtime;

import BaseUtil.cucumber.runtime.model.CucumberFeature;

import java.util.List;

public interface FeatureSupplier {
    List<CucumberFeature> get();
}
