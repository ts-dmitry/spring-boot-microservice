package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Slf4j
@EnableMongoRepositories
public class SpringBootMicroserviceApplication {

    private final static Class[] autoConfigurationClasses = {
            // web
            DispatcherServletAutoConfiguration.class,
            ServletWebServerFactoryAutoConfiguration.class,
            WebMvcAutoConfiguration.class,
            // mongo
            MongoAutoConfiguration.class,
            MongoDataAutoConfiguration.class,
    };

    public static SpringApplication buildApp() {
        return new SpringApplicationBuilder(com.example.demo.SpringBootMicroserviceApplication.class)
                .sources(autoConfigurationClasses)
                .initializers((GenericApplicationContext applicationContext) -> {

                    applicationContext.registerBean(CommentService.class, CommentService::new);

                    applicationContext.registerBean(RouterFunction.class, () -> {
                        CommentService commentService = applicationContext.getBean(CommentService.class);

                        return route()
                                .GET("/comments", request -> {
                                    commentService.addRandomComment();

                                    List<String> allComments = commentService.findAll().stream()
                                            .map(Comment::getValue)
                                            .collect(Collectors.toList());

                                    return ServerResponse.ok().body(allComments);
                                })
                                .build();
                    });
                })
                .build();
    }

    public static void main(String[] args) {
        buildApp().run(args);
    }
}