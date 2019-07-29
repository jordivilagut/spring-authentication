package application.services;

import application.model.User;

import java.util.List;

public interface UsersService {
    public List<User> getAll();

    public User getUserFromToken(String token);

    public User getUserFromUsername(String username);

    public User addOrUpdateUser(User user);
}
