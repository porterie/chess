package handler;
import com.google.gson.Gson;
import service.*;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class handler {

    public static Object registerHandler(Request request, Response response, UserService userService) {
        Gson serializer = new Gson();
        RegisterRequest regReq = serializer.fromJson(request.body(), RegisterRequest.class);

        RegisterResult result = userService.register(regReq);
        response.type("application/json");
        if(result==null){
            response.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        }else if(result.username()==null){
            response.status(403);
            return "{ \"message\": \"Error: already taken\" }";
        }else if(result.username().equals(regReq.username()) && result.authToken()!=null){
            response.status(200);
            return serializer.toJson(result);
        }else{
            response.status(500);
            return "{ \"message\": \"Error: idk man.\" }";
        }
    }
    public static Object loginHandler(Request request, Response response, UserService userService){
        Gson serializer = new Gson();
        LoginRequest logReq = serializer.fromJson(request.body(), LoginRequest.class);

        LoginResult result = userService.login(logReq);
        response.type("application/json");
        if(result==null){
            response.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        }else if(result.authToken()==null){
            response.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }else if(Objects.equals(result.username(), logReq.username())){
            response.status(200);
            return serializer.toJson(result);
        }else{
            response.status(500);
            return "{ \"message\": \"Error: idk man.\" }";
        }
    }

    public static Object logoutHandler(Request request, Response response, UserService userService) {
        Gson serializer = new Gson();
        LogoutRequest logOReq = new LogoutRequest(request.headers("Authorization"));

        LogoutResult result = userService.logout(logOReq);
        response.type("application/json");
        if(!result.userFound()){
            response.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }else {
            response.status(200);
            return "{}";
        }/*else{
            response.status(500);
            return "{ \"message\": \"Error: idk man.\" }";
        }*/
    }
}
