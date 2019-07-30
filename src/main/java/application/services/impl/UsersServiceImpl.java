package application.services.impl;

import application.model.User;
import application.repos.UsersRepository;
import application.services.AuthenticationService;
import application.services.UsersService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    private Logger LOG = LoggerFactory.getLogger(UsersService.class);

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
            LOG.info("User token expired.", e);
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

    @Override
    public void revokeToken(User user) {
        user.setToken("");
        users.save(user);
    }
}
