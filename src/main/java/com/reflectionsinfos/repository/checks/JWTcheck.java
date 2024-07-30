package com.reflectionsinfos.repository.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.python.api.PythonSubscriptionCheck;
import org.sonar.plugins.python.api.SubscriptionContext;
import org.sonar.plugins.python.api.tree.*;

import java.util.Base64;
import java.util.logging.Logger;

@Rule(
        key = JWTcheck.RULE_KEY,
        name = JWTcheck.NAME,
        priority = Priority.MAJOR,
        description = JWTcheck.DESCRIPTION,
        tags = {
                "bad-practice",
                "security"
        }
)
public class JWTcheck extends PythonSubscriptionCheck {
    private static final Logger LOGGER = Logger.getLogger(JWTcheck.class.getName());

    public static final String NAME = "JSON_WEB_TOKENS";
    public static final String RULE_KEY = "SC104";
    public static final String DESCRIPTION = "Finds and detect the hardcoded JWT from the source code for better code quality";

    @Override
    public void initialize(Context context) {
        context.registerSyntaxNodeConsumer(Tree.Kind.ASSIGNMENT_STMT, this::visitNODEonTREE);
    }

    private void visitNODEonTREE(SubscriptionContext context) {
        AssignmentStatement statement = (AssignmentStatement) context.syntaxNode();
        for (ExpressionList expressionList : statement.lhsExpressions()) {
            for (Expression expression : expressionList.expressions()) {
                if (expression.is(Tree.Kind.NAME)) {
                    Expression assignedValue = statement.assignedValue();
                    if (assignedValue.is(Tree.Kind.STRING_LITERAL)) {
                        StringLiteral stringLiteral = (StringLiteral) assignedValue;
                        String value = stringLiteral.trimmedQuotesValue();
                        LOGGER.info("Processing string literal: " + value);

                        if (isFormofHardcodedJWTs(value)) {
                            LOGGER.warning("Hardcoded JWT detected: " + value);
                            context.addIssue(expression, String.format("Avoid hardcoded JWT tokens for security reasons. Found: '%s'", value));
                        }
                    }
                }
            }
        }
    }

    private boolean isFormofHardcodedJWTs(String value) {
        String[] parts = value.split("\\.");
        if (parts.length == 3) {
            return isBase64Encoded(parts[0]) && isBase64Encoded(parts[1]) && isBase64Encoded(parts[2]);
        }
        return false;
    }

    private boolean isBase64Encoded(String part) {
        try {
            Base64.getUrlDecoder().decode(part);  // Use URL decoder for JWT
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
