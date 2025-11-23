package com.skupina1.accountmanagement.resource;

import com.skupina1.accountmanagement.model.UpdateUserRequest;
import com.skupina1.accountmanagement.model.User;
import com.skupina1.accountmanagement.security.Public;
import com.skupina1.accountmanagement.service.UserService;
import com.skupina1.accountmanagement.util.JwtUtil;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.management.relation.Role;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserService service = new UserService();

    @POST
    @Path("/register")
    @Public
    public Response register(User u) {
        if (service.register(u)) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\":\"User registered\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\":\"Email already exists\"}").build();
    }

    @POST
    @Path("/login")
    @Public
    public Response login(User u) {
        String token = service.login(u.getEmail(), u.getPassword());
        if (token == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\":\"Invalid login\"}").build();
        }
        return Response.ok("{\"token\":\"" + token + "\"}").build();
    }

    // ADMIN ONLY
    @DELETE
    @Path("/{email}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("email") String email) {
        boolean ok = service.delete(email);

        if (ok)
            return Response.ok("{\"message\":\"User deleted\"}").build();

        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\":\"User not found\"}")
                .build();
    }

    @GET
    @RolesAllowed("ADMIN")
    public Response getUsersByRole(@QueryParam("role") String role){
        List<User> users = service.getUsersByRole(role);
        if(!users.isEmpty())
            return Response.ok(users).build();

        return Response.status((Response.Status.BAD_REQUEST))
                .entity("{\"error\":\"Improper role\"}")
                .build();
    }

    @PUT
    @Path("/me")
    public Response updateSelf(UpdateUserRequest req, @Context ContainerRequestContext ctx){
        String email = (String) ctx.getProperty("email");
        boolean success = service.updateUser(email, req);

        if(success)
            return Response.ok("{\"message\":\"User updated successfully\"}").build();

        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"User not found\"}")
                .build();
    }

    @GET
    @Path("/me")
    public Response getSelf(@Context ContainerRequestContext ctx){
        String email = (String) ctx.getProperty("email");
        User user = service.findUserByEmail(email);

        if(user == null)
            return Response.status((Response.Status.NOT_FOUND))
                    .entity("{\"error\":\"User not found\"}")
                    .build();

        user.setPassword(null);
        return Response.ok(user).build();

    }

    @PUT
    @Path("/admin/update")
    @RolesAllowed("ADMIN")
    public Response updateUserByEmail(UpdateUserRequest req, @QueryParam("email") String email){
        if(email.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Email param required\"}")
                    .build();

        if(service.findUserByEmail(email).getRole().equals("ADMIN"))
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Updating ADMIN users is not permitted\"}")
                    .build();

        boolean success = service.updateUser(email, req);

        if(success)
            return Response.ok("{\"message\":\"User updated successfully\"}").build();

        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"User not found\"}")
                .build();
    }
}
