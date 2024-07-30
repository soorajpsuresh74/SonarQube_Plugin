package com.reflectionsinfos.repository.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.python.api.PythonSubscriptionCheck;
import org.sonar.plugins.python.api.SubscriptionContext;
import org.sonar.plugins.python.api.tree.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;

@Rule(
        key = AWSkeyCheck.RULE_KEY,
        name = AWSkeyCheck.NAME,
        priority = Priority.MAJOR,
        description = AWSkeyCheck.DESCRIPTION,
        tags = {
                "security",
                "bad-practice"
        }
)
public class AWSkeyCheck extends PythonSubscriptionCheck {
    private static final Logger LOGGER = Logger.getLogger(AWSkeyCheck.class.getName());
    private static final int MIN_LENGTH = 16;
    private static final int MAX_LENGTH = 128;

    public static final String NAME = "AWS_SECRETS";
    public static final String RULE_KEY = "SC102";
    public static final String DESCRIPTION = "Find and detect the hardcoded Amazon Web Services tokens with regular expression matching";

    private static final String AWS_KEY_PATTERN = "(AKIA|ASIA)[0-9A-Z]{16,}";

    @Override
    public void initialize(Context context) {
        context.registerSyntaxNodeConsumer(Tree.Kind.ASSIGNMENT_STMT, this::visitNode);
    }

    private void visitNode(SubscriptionContext context) {
        AssignmentStatement assignmentStatement = (AssignmentStatement) context.syntaxNode();

        for (ExpressionList expressionList : assignmentStatement.lhsExpressions()) {
            for (Expression expression : expressionList.expressions()) {
                if (expression.is(Tree.Kind.NAME)) {
                    Expression assignedValue = assignmentStatement.assignedValue();
                    if (assignedValue.is(Tree.Kind.STRING_LITERAL)) {
                        StringLiteral stringLiteral = (StringLiteral) assignedValue;
                        String value = stringLiteral.trimmedQuotesValue();
                        LOGGER.info("Processing string literal: " + value);
                        if (isHardCodedAWSKey(value)) {
                            context.addIssue(expression, String.format("Likely identified as sensitive key exposure. Please cross-verify. Found: '%s'", value))
                                    .secondary(assignedValue, "Hardcoded AWS secrets");
                        }
                    }
                }
            }
        }
    }

    private boolean isHardCodedAWSKey(String value) {
        if (value.length() > MIN_LENGTH && value.length() <= MAX_LENGTH) {
            Pattern pattern = Pattern.compile(AWS_KEY_PATTERN);
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        }
        return false;
    }
}
