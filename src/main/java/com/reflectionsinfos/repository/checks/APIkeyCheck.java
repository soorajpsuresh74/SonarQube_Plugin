package com.reflectionsinfos.repository.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.python.api.PythonSubscriptionCheck;
import org.sonar.plugins.python.api.SubscriptionContext;
import org.sonar.plugins.python.api.tree.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Rule(
        key = APIkeyCheck.RULE_KEY,
        name = APIkeyCheck.NAME,
        priority = Priority.MAJOR,
        description = APIkeyCheck.DESCRIPTION,
        tags = {
                "bad-practice",
                "security"
        }
)
public class APIkeyCheck extends PythonSubscriptionCheck {
    public static final String NAME = "API_CHECK";
    public static final String RULE_KEY = "SC101";
    public static final String DESCRIPTION = "Finds hardcoded API keys in the Python codebase";

    @Override
    public void initialize(Context context) {
        context.registerSyntaxNodeConsumer(Tree.Kind.ASSIGNMENT_STMT, this::visitNode);
    }

    public void visitNode(SubscriptionContext context) {
        AssignmentStatement assignmentStatement = (AssignmentStatement) context.syntaxNode();
        for (ExpressionList expressionList : assignmentStatement.lhsExpressions()) {
            for (Expression expression : expressionList.expressions()) {
                if (expression.is(Tree.Kind.NAME)) {
                    Expression assignedValue = assignmentStatement.assignedValue();
                    if (assignedValue.is(Tree.Kind.STRING_LITERAL)) {
                        StringLiteral stringLiteral = (StringLiteral) assignedValue;
                        if (isHardCodedApiKey(stringLiteral) || isContainsApiKey(stringLiteral)) {
                            context.addIssue(expression, String.format("Likely identified as sensitive key exposure. Please cross-verify. Found: '%s'", stringLiteral.trimmedQuotesValue()))
                                    .secondary(assignmentStatement.assignedValue(), "Hardcoded value");
                        }
                    }
                }
            }
        }
    }

    private boolean isContainsApiKey(StringLiteral assignedValue) {
        String value = assignedValue.trimmedQuotesValue();
        return value != null && (value.toLowerCase().contains("api") || value.toLowerCase().contains("key"));
    }

    private boolean isHardCodedApiKey(StringLiteral assignedValue) {
        String value = assignedValue.trimmedQuotesValue();
        String patternString = "([A-Za-z0-9_\\-]{16}|[A-Za-z0-9_\\-]{32}|[A-Za-z0-9_\\-]{64})";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
