package com.zuitt.capstone3.Repositories;

import com.zuitt.capstone3.Models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUser_Id(String id);
}
