package net.decodex.loghub.backend.utils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryEngine {

    public static BooleanExpression parseQuery(String queryString) {
        List<String> tokens = parseTokens(queryString);
        return buildPredicate(tokens);
    }

    private static List<String> parseTokens(String queryString) {
        List<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("([^&|]+)|([&|])|([=!><~]+)|(\"[^\"]+\")|([^,]+)|(\\S+\\s+in\\s+\\([^)]+\\))");
        Matcher matcher = pattern.matcher(queryString);

        while (matcher.find()) {
            String token = matcher.group().trim(); // Trim whitespaces
            if (!token.isEmpty()) {
                tokens.add(token);
                System.out.println(token);
            }
        }

        return tokens;
    }

    private static BooleanExpression buildPredicate(List<String> tokens) {
        BooleanExpression predicate = null;
        String logicalOperator = null;

        for (String token : tokens) {
            switch (token) {
                case "&":
                    logicalOperator = "AND";
                    break;
                case "|":
                    logicalOperator = "OR";
                    break;
                default:
                    if (predicate == null) {
                        predicate = createPredicateFromToken(token);
                    } else {
                        if (logicalOperator.equals("AND")) {
                            predicate = predicate.and(createPredicateFromToken(token));
                        } else if (logicalOperator.equals("OR")) {
                            predicate = predicate.or(createPredicateFromToken(token));
                        }
                    }
            }
        }

        return predicate;
    }

    private static BooleanExpression createPredicateFromToken(String token) {
        String[] parts = token.split("\\s+");
        String key = parts[0];
        if (parts.length > 2 && parts[1].equalsIgnoreCase("in")) {
            String[] values = token.substring(token.indexOf("(") + 1, token.indexOf(")")).split("\\s*,\\s*");
            return Expressions.stringPath(key).in(values);
        } else {
            String operator = parts[1];
            String value = parts[2];
            return switch (operator) {
                case "=" -> {
                    if (isValueBoolean(value)) {
                        yield Expressions.booleanPath(key).eq(Boolean.valueOf(value));
                    } else if (isValueNumber(value)) {
                        yield numberExpression(key, value, "=");
                    } else {
                        yield Expressions.stringPath(key).eq(value);
                    }
                }
                case "!=" -> {
                    if (isValueBoolean(value)) {
                       yield Expressions.booleanPath(key).ne(Boolean.valueOf(value));
                    } else if (isValueNumber(value)) {
                        yield numberExpression(key, value, "!=");
                    } else {
                        yield Expressions.stringPath(key).ne(value);
                    }
                }
                case "!~" -> Expressions.stringPath(key).containsIgnoreCase(value).not();
                case "~" -> Expressions.stringPath(key).containsIgnoreCase(value);
                case ">" -> {
                    if (isValueNumber(value)) {
                        yield numberExpression(key, value, ">");
                    } else {
                        yield Expressions.stringPath(key).gt(value);
                    }
                }
                case ">=" -> {
                    if (isValueNumber(value)) {
                        yield numberExpression(key, value, ">=");
                    } else {
                        yield Expressions.stringPath(key).goe(value);
                    }
                }
                case "<" -> {
                    if (isValueNumber(value)) {
                        yield numberExpression(key, value, "<");
                    } else {
                        yield Expressions.stringPath(key).lt(value);
                    }
                }
                case "<=" -> {
                    if (isValueNumber(value)) {
                        yield numberExpression(key, value, "<=");
                    } else {
                        yield Expressions.stringPath(key).loe(value);
                    }
                }
                case "IS", "is" -> {
                    if (value.equalsIgnoreCase("null")) {
                        yield Expressions.stringPath(key).isNull();
                    } else if (value.equalsIgnoreCase("empty")) {
                        yield Expressions.stringPath(key).isEmpty();
                    } else {
                        throw new IllegalArgumentException("Invalid token: " + token);
                    }
                }
                default -> throw new IllegalArgumentException("Invalid operator: " + operator);
            };
        }
    }

    private static boolean isValueBoolean(String value) {
        return value.equals("true") || value.equals("false");
    }

    private static boolean isValueNumber(String value) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (value == null) {
            return false;
        }
        return pattern.matcher(value).matches();
    }

    private static BooleanExpression numberExpression(String key, String value, String operation) {
        if (value.contains(".")) {
            return switch (operation) {
                case "!=" -> Expressions.numberPath(Double.class, key).ne(Double.valueOf(value));
                case ">" -> Expressions.numberPath(Double.class, key).gt(Double.valueOf(value));
                case ">=" -> Expressions.numberPath(Double.class, key).goe(Double.valueOf(value));
                case "<" -> Expressions.numberPath(Double.class, key).lt(Double.valueOf(value));
                case "<=" -> Expressions.numberPath(Double.class, key).loe(Double.valueOf(value));
                default -> Expressions.numberPath(Double.class, key).eq(Double.valueOf(value));
            };
        } else {
            return switch (operation) {
                case "!=" -> Expressions.numberPath(Long.class, key).ne(Long.valueOf(value));
                case ">" -> Expressions.numberPath(Long.class, key).gt(Long.valueOf(value));
                case ">=" -> Expressions.numberPath(Long.class, key).goe(Long.valueOf(value));
                case "<" -> Expressions.numberPath(Long.class, key).lt(Long.valueOf(value));
                case "<=" -> Expressions.numberPath(Long.class, key).loe(Long.valueOf(value));
                default -> Expressions.numberPath(Long.class, key).eq(Long.valueOf(value));
            };
        }
    }
}
