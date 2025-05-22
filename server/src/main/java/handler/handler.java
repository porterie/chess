package handler;
import com.google.gson.Gson;
import service.RegisterRequest;
import service.RegisterResult;
import service.UserService;
import spark.Request;
import spark.Response;

public class handler {

    public static Object registerHandler(Request request, Response response, UserService userService) {
        Gson serializer = new Gson();
        RegisterRequest regReq = serializer.fromJson(request.body(), RegisterRequest.class);
        RegisterResult result = userService.register(regReq);
        response.type("application/json");
        return serializer.toJson(result);
    }
}
