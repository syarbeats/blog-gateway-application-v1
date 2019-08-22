package com.mitrais.cdc.bloggatewayapplicationv1.testrunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features= "features/backend", glue= "com.mitrais.cdc.bloggatewayapplicationv1.backendstepdefinition")
public class TestRunnerBackend {
}

