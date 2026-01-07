package com.skupina1.accountmanagement;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.jsonb.JsonBindingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.skupina1.accountmanagement.security.AuthFilter;
import com.skupina1.accountmanagement.security.CorsFilter;
import com.skupina1.accountmanagement.security.ForbiddenExceptionMapper;

import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("api")
public class App {
    public static void main(String[] args) throws Exception {
        ResourceConfig rc = new ResourceConfig()
                .packages("com.skupina1.accountmanagement.resource")
                .register(AuthFilter.class)
                .register(CorsFilter.class)
                .register(RolesAllowedDynamicFeature.class)
                .register(ForbiddenExceptionMapper.class)
                .register(JsonBindingFeature.class);

        HttpServer server = org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
                .createHttpServer(java.net.URI.create("http://0.0.0.0:8080/"), rc);

        System.out.println("Account service running on http://localhost:8080/");
        Thread.currentThread().join();
    }
}
