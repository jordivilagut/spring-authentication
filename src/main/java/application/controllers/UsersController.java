package application.controllers;

import application.model.User;
import application.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/all")
    public List<User> getUsers() {
        return usersService.getAll();
    }
}
