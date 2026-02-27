package com.session.utils;

import com.session.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class SecurityUtil {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtil.class);

    private static final LinkedHashMap<String, String> URL_ACCESS_RULES = new LinkedHashMap<>();

    static {
        URL_ACCESS_RULES.put("/", "permitAll");
        URL_ACCESS_RULES.put("/home", "permitAll");
        URL_ACCESS_RULES.put("/common", "authenticated");
        URL_ACCESS_RULES.put("/sa", Role.SUPER_ADMIN.name());
        URL_ACCESS_RULES.put("/ad", Role.ADMIN.name());
        URL_ACCESS_RULES.put("/op", Role.OPERATOR.name());
        URL_ACCESS_RULES.put("/test", Role.SUPER_ADMIN.name() + "," + Role.ADMIN.name() + "," + Role.OPERATOR.name());
    }

    public static String getUserInfoMessage(Authentication authentication,
                                            HttpServletRequest request) {
        log.info("Info :: method called");

        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (Objects.nonNull(queryString)) {
            url += "?" + queryString;
        }

        String urlListHtml = buildAccessibleUrlList(authentication, URL_ACCESS_RULES);

        if (Objects.isNull(authentication) || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return "<h1 style='color:red;'>No authenticated user found. URL accessed: "
                    + "<span style='color:blue;'>" + url + "</span>"
                    + " | <a href='/login'>Login</a></h1>"
                    + urlListHtml;
        }

        String username = authentication.getName();
        String roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        return String.format(
                "<h1>User: <span style='color:green;'>%s</span>, with roles: "
                        + "[<span style='color:orange;'>%s</span>], accessed URL: "
                        + "<span style='color:blue;'>%s</span>"
                        + " | <a href='/logout'>Logout</a></h1>%s",
                username, roles, url, urlListHtml);
    }

    private static String buildAccessibleUrlList(Authentication authentication,
                                                 LinkedHashMap<String, String> urlAccessRules) {
        boolean isAuthenticated = Objects.nonNull(authentication)
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);

        Collection<? extends GrantedAuthority> authorities =
                isAuthenticated ? authentication.getAuthorities() : Collections.emptyList();

        String links = urlAccessRules.entrySet().stream()
                .filter(entry -> isAccessible(entry.getValue(), isAuthenticated, authorities))
                .map(entry -> {
                    String pattern = entry.getKey();
                    String rule = entry.getValue();
                    String badge = getBadgeHtml(rule);
                    return "<li>" + badge + " <a href='" + pattern + "'>" + pattern + "</a></li>";
                })
                .collect(Collectors.joining());

        return "<h3>Accessible URLs:</h3><ul style='list-style:none;padding:0;'>" + links + "</ul>";
    }

    private static boolean isAccessible(String rule, boolean isAuthenticated,
                                        Collection<? extends GrantedAuthority> authorities) {
        return switch (rule) {
            case "permitAll" -> true;
            case "denyAll" -> false;
            case "authenticated" -> isAuthenticated;
            default -> isAuthenticated && Arrays.stream(rule.split(","))
                    .map(String::trim)
                    .anyMatch(r -> authorities.stream()
                            .anyMatch(a -> a.getAuthority().equals(r)));
        };

    }

    private static String getBadgeHtml(String rule) {
        return switch (rule) {
            case "permitAll" ->
                    "<span style='background:green;color:white;padding:1px 6px;border-radius:4px;font-size:12px;'>PUBLIC</span>";
            case "authenticated" ->
                    "<span style='background:dodgerblue;color:white;padding:1px 6px;border-radius:4px;font-size:12px;'>AUTH</span>";
            case "denyAll" ->
                    "<span style='background:red;color:white;padding:1px 6px;border-radius:4px;font-size:12px;'>DENIED</span>";
            default ->
                    "<span style='background:orange;color:white;padding:1px 6px;border-radius:4px;font-size:12px;'>" + rule + "</span>";
        };
    }
}
