package com.zuitt.capstone3.Repositories;

import com.zuitt.capstone3.Models.Appointment;
import com.zuitt.capstone3.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);

}
