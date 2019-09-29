package com.zuitt.capstone3.Controllers;

import com.zuitt.capstone3.Models.Role;
import com.zuitt.capstone3.Models.User;
import com.zuitt.capstone3.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/")
public class AuthRegisterController {
    @Autowired
    UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostMapping("register/{user_role}")
    public User registerMember(@RequestBody User user,
                               @PathVariable String user_role) {
        String hashedPw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPw);
        Role userRole = new Role();
        userRole.setUserRole(user_role);
        user.setRole(userRole);
        return userRepository.save(user);
    }

    @GetMapping("/register/{username}")
    public Boolean checkIfUsernameExists(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

    @PostMapping("login")
    public String loginUser(@RequestBody User user) {
        User foundUser = userRepository.findByUsername(user.getUsername());
        if( foundUser == null) {
            return "Username is invalid";
        } else {
            if (BCrypt.checkpw(user.getPassword(), foundUser.getPassword())) {
                Claims claims = Jwts.claims()
                        .setSubject(foundUser.getUsername());
                return Jwts.builder()
                        .setClaims(claims)
                        .signWith(SignatureAlgorithm.HS512, secretKey)
                        .claim("user", foundUser)
                        .compact();
            }  return "Password is incorrect";
        }
    }

    @PostMapping("/testdate")
    public void testingDate() {
        Date test = new Date();
        System.out.println(test.getTime());
        System.out.println(test.getTime() + 86400000);
        System.out.println(test.getTime() + 86400000);

    }



}
