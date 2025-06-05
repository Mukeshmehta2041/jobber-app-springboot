package com.jobber.common.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter to validate the API Gateway token for incoming requests.
 * Ensures that requests coming through the API Gateway contain a valid token.
 */
@Component
public class ApiGatewayTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiGatewayTokenFilter.class);

    /**
     * Secret token configured for API Gateway validation.
     */
    @Value("${api.gateway.secret}")
    private String apiGatewaySecret;

    /**
     * Header name used to pass the API Gateway token.
     */
    private static final String HEADER_NAME = "X-Api-Gateway-Token";

    /**
     * Filters incoming requests to validate the API Gateway token.
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain to pass the request further.
     * @throws ServletException If an error occurs during filtering.
     * @throws IOException      If an I/O error occurs during filtering.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve the token from the request header
        String token = request.getHeader(HEADER_NAME);

        logger.info("token:{}", token);

        // Validate the presence of the token
        if (token == null) {
            logger.warn("Missing {} header in request to {}", HEADER_NAME, request.getRequestURI());
            unauthorized(response, "Unauthorized: Missing API Gateway Token");
            return;
        }

        // Validate the token against the configured secret
        if (!token.equals(apiGatewaySecret)) {
            logger.warn("Invalid API Gateway Token for request to {}. Received token: {}", request.getRequestURI(), token);
            unauthorized(response, "Unauthorized: Invalid API Gateway Token");
            return;
        }

        // Log successful validation
        logger.info("Valid API Gateway Token received for request to {}", request.getRequestURI());

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Sends an unauthorized response with the given message.
     *
     * @param response The HTTP response.
     * @param message  The message to include in the response body.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(message);
    }
}