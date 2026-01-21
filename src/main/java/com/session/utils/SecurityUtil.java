package com.session.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;
import java.util.stream.Collectors;

public class SecurityUtil {

	public static String getUserInfoMessage(Authentication authentication, HttpServletRequest request) {
		if (Objects.isNull(authentication)) {
			return "<h1 style='color:red;'>No authenticated user found. URL accessed: " + "<span style='color:blue;'>"
					+ request.getRequestURL() + "</span></h1>";
		}

		String username = authentication.getName();

		String roles = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(", "));

		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		if (Objects.nonNull(queryString)) {
			url += "?" + queryString;
		}

		return String.format("<h1>User: <span style='color:green;'>%s</span>, with roles: "
				+ "[<span style='color:orange;'>%s</span>], accessed URL: "
				+ "<span style='color:blue;'>%s</span></h1>", username, roles, url);
	}
}
