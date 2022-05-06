package models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.*;

public class Environment {
    public EnvironmentTemplate test1;
    public EnvironmentTemplate test2;

    public Environment() {
    }

    public EnvironmentTemplate getTest1() {
        return test1;
    }
    public EnvironmentTemplate getTest2() {
        return test2;
    }

    public List<EnvironmentTemplate> getListOfEnvironments() {
        List<EnvironmentTemplate> environments = new ArrayList<>();
        environments.add(getTest1());
        environments.add(getTest2());
        return environments;
    }
}

