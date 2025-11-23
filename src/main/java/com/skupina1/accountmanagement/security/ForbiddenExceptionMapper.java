package com.skupina1.accountmanagement.security;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    @Override
    public Response toResponse(ForbiddenException e){
        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"error\":\"Access denied: Improper permissions for this action.\"}")
                .build();
    }
}
