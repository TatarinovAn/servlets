package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;
    private Gson gson = new Gson();

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) {
        senderResponse(response, service.all());
    }

    public void getById(long id, HttpServletResponse response) {
        senderResponse(response, service.getById(id));
    }

    public void save(Reader body, HttpServletResponse response) {

        final var data = gson.fromJson(body, Post.class);
        service.save(data);
        senderResponse(response, data);

    }

    public void removeById(long id, HttpServletResponse response) {

        service.removeById(id);
        try {
            response.getWriter().print("Delete ok");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void senderResponse(HttpServletResponse response, T data) {
        response.setContentType(APPLICATION_JSON);
        try {
            response.getWriter().print(gson.toJson(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
