package com.reflectionsinfos.repository;

import com.reflectionsinfos.repository.checks.APIkeyCheck;
import com.reflectionsinfos.repository.checks.AWSkeyCheck;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.plugins.python.Python;
import org.sonar.plugins.python.api.PythonCustomRuleRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HardcodedpluginRepository implements RulesDefinition, PythonCustomRuleRepository {
    public static final String NAME = "HardCoded Secrets";
    public static final String KEY = "SECRETS_secrets";
    public static final String LANGUAGE = Python.KEY;
    public static final String RESOURCE_BASE_PATH = "/templates";
    @Override
    public void define(Context context) {
    NewRepository repository = context.createRepository(repositoryKey(),LANGUAGE).setName(NAME);
    new RulesDefinitionAnnotationLoader().load(repository, checkClasses().toArray(new Class[]{}));

        Map <String, String> remediationCost = new HashMap<>();
        remediationCost.put(APIkeyCheck.RULE_KEY,"2min");
        remediationCost.put(AWSkeyCheck.RULE_KEY,"3min");

        repository.rules().forEach(rule -> {
            String cost = remediationCost.get(rule.key());
            if (cost != null){
                rule.setDebtRemediationFunction(
                        rule.debtRemediationFunctions().constantPerIssue(cost)
                );
            }
            addHtmlDescription(rule, rule.key());
        });
        repository.done();
        System.out.println("LoAd SuCcEsS fOr - HARDCODED PLUGIN");
    }

    @Override
    public String repositoryKey() {
        return KEY;
    }

    @Override
    public List<Class> checkClasses() {
        return List.of(APIkeyCheck.class, AWSkeyCheck.class);
    }
    public static void addHtmlDescription(NewRule rule, String metadataKey) {
        URL resource = HardcodedpluginRepository.class.getResource(RESOURCE_BASE_PATH+ "/" + metadataKey + ".html");
        if(resource != null) {
            rule.setHtmlDescription(readResource(resource));
            System.out.println("HTML description added for the rule: " + metadataKey);
        }else {
            System.err.println("Resource not found: " + RESOURCE_BASE_PATH + "/" + metadataKey + ".html");
        }
    }
    private static String readResource(URL resource){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8))){
            return reader.lines().collect(Collectors.joining("\n"));
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
