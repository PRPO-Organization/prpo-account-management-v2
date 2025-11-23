package com.skupina1.accountmanagement.security;

import com.skupina1.accountmanagement.util.JwtUtil;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
@PreMatching
public class JwtFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException{
        String path = ctx.getUriInfo().getPath();

        // allow public endpoints
        if (path.startsWith("users/login") || path.startsWith("users/register")) {
            return;
        }

        String auth = ctx.getHeaderString("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String token = auth.substring(7);

        if (!JwtUtil.validateToken(token)) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
