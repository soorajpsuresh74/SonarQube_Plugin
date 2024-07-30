package com.reflectionsinfos.repository.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.python.api.PythonSubscriptionCheck;
import org.sonar.plugins.python.api.SubscriptionContext;
import org.sonar.plugins.python.api.tree.*;

import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Rule(
        key = SSNCheck.RULE_KEY,
        name = SSNCheck.NAME,
        priority = Priority.CRITICAL,
        description = SSNCheck.DESCRIPTION,
        tags = {
                "privacy",
                "bad-practice"
        }
)

public class SSNCheck extends PythonSubscriptionCheck {

    private static final Logger LOGGER = Logger.getLogger(SSNCheck.class.getName());
    private static final int LENGTH = 9;

    public static final String NAME = "SSN_SECRETS";
    public static final String RULE_KEY = "SC103";
    public static final String DESCRIPTION = "Find and detect the hardcoded Social Security Number from the source codes";

    private static final Pattern SSN_PATTERN = Pattern.compile("\\b\\d{3}-\\d{2}-\\d{4}\\b");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("\\b\\d{9}\\b");
    @Override
    public void initialize(Context context) {
        context.registerSyntaxNodeConsumer(Tree.Kind.ASSIGNMENT_STMT, this::visitNode);
    }
    public void visitNode(SubscriptionContext context){
        AssignmentStatement assignmentStatement = (AssignmentStatement) context.syntaxNode();

        for (ExpressionList expressionList : assignmentStatement.lhsExpressions()){
            for (Expression expression : expressionList.expressions()){
                if(expression.is(Tree.Kind.NAME)){
                    Expression assignedValue = assignmentStatement.assignedValue();
                    if (assignedValue.is(Tree.Kind.STRING_LITERAL)){
                        StringLiteral stringLiteral = (StringLiteral) assignedValue;
                        String value = stringLiteral.trimmedQuotesValue();
                        LOGGER.info("Processing string literal: " + value);
                        if (isHardcodedSSN(value) || isUnhashedNumber(value)){
                            context.addIssue(expression, String.format("Likely identified as sensitive SSN exposure. Please cross-verify. Found: '%s'", value))
                                    .secondary(assignedValue, "Hardcoded SSN secrets");
                        }
                    }
                }
            }
        }
    }

    private boolean isHardcodedSSN(String value) {
        return SSN_PATTERN.matcher(value).matches();
    }

    private boolean isUnhashedNumber(String value) {
        return NUMERIC_PATTERN.matcher(value).matches();
    }
}
