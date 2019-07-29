package application.services.impl;

import application.model.User;
import application.repos.UsersRepository;
import application.services.AuthenticationService;
import application.services.UsersService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository users;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public List<User> getAll() {
        return users.findAll();
    }

    @Override
    public User getUserFromToken(String token) {
        Claims claims;

        try {
            claims = authenticationService.decodeJWT(token);
        } catch (ExpiredJwtException e){
            //TODO - Log exception
            return null;
        }

        return users.findByUsername(claims.getId());
    }

    @Override
    public User getUserFromUsername(String username) {
        return users.findByUsername(username);
    }

    @Override
    public User addOrUpdateUser(User user) {
        String token = authenticationService.createJWT(user);
        user.setToken(token);
        users.save(user);

        return user;
    }
}
