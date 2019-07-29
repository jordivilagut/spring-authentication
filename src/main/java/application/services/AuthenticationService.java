package application.services;

import application.model.User;
import io.jsonwebtoken.Claims;

public interface AuthenticationService {

    public String createJWT(User user);

    public Claims decodeJWT(String jwt);
}
