package com.skupina1.accountmanagement.security;

import com.skupina1.accountmanagement.util.JwtUtil;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.lang.reflect.Method;
import java.security.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext){

        //skip decorated with @Public
        Method method = resourceInfo.getResourceMethod();
        if(method.isAnnotationPresent(Public.class) || resourceInfo.getResourceClass().isAnnotationPresent(Public.class))
            return;

        try {
            String authHeader = requestContext.getHeaderString("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return; // request will be treated as unauthenticated
            }

            String token = authHeader.substring("Bearer ".length());

            if (!JwtUtil.validateToken(token)) {
                return;
            }

            String email = JwtUtil.getEmailFromToken(token);
            String role  = JwtUtil.getRoleFromToken(token);

            requestContext.setProperty("email", email); // attaching just in case
            requestContext.setProperty("role", role);   // same here

            // Wrap the authenticated user in a SecurityContext
            SecurityContext original = requestContext.getSecurityContext();

            SecurityContext sc = new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> email;
                }

                @Override
                public boolean isUserInRole(String r) {
                    return role != null && role.equalsIgnoreCase(r);
                }

                @Override
                public boolean isSecure() {
                    return original.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            };

            requestContext.setSecurityContext(sc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
