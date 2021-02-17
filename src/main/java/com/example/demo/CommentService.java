package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CommentService {

    private final Faker faker = Faker.instance();

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Comment addRandomComment() {
        return commentRepository.save(new Comment(faker.chuckNorris().fact()));
    }
}
