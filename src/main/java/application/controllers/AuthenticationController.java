package application.controllers;

import application.model.User;
import application.services.AuthenticationService;
import application.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(path="/auth")
public class AuthenticationController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody HashMap<String, String> body) {

        User user = usersService.getUserFromUsername(body.get("username"));

        if (user == null) {
            return new ResponseEntity<>("Invalid user.", HttpStatus.UNAUTHORIZED);
        }

        if (!body.get("password").equals(user.getPassword())) {
            return new ResponseEntity<>("Invalid password.", HttpStatus.UNAUTHORIZED);
        }

        User updated = usersService.addOrUpdateUser(user);

        return new ResponseEntity<>(updated.getToken(), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User body) {

        if (body.getUsername().isEmpty() || body.getPassword().isEmpty()) {
            return new ResponseEntity<>("Empty username or password.", HttpStatus.FORBIDDEN);
        }

        if (usersService.getUserFromUsername(body.getUsername()) != null) {
            return new ResponseEntity<>("Username already registered.", HttpStatus.FORBIDDEN);
        }

        if (body.getPassword().length() <= 4) {
            return new ResponseEntity<>("Password is too short.", HttpStatus.FORBIDDEN);
        }

        User user = usersService.addOrUpdateUser(body);

        return new ResponseEntity<>(user.getToken(), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value="Authorization") String token) {
        User user = usersService.getUserFromToken(token);
        usersService.revokeToken(user);
        return new ResponseEntity<>("Logged out.", HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestHeader(value="Authorization") String token) {

        User user = usersService.getUserFromToken(token);

        if (user == null) {
            return new ResponseEntity<>("Invalid user.", HttpStatus.UNAUTHORIZED);
        }

        User updated = usersService.addOrUpdateUser(user);

        return new ResponseEntity<>(updated.getToken(), HttpStatus.OK);
    }
}
