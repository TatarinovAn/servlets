package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.handler.Handler;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainServlet extends HttpServlet {
    private final Map<String, Map<String, Handler>> handlers = new ConcurrentHashMap<>();
    private final static String methodGet = "GET";
    private final static String methodPost = "POST";
    private final static String methodDelete = "DELETE";
    private final static String pathApiPosts = "/api/posts";
    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);


        addHandler(methodGet, pathApiPosts, (((path, req, resp) -> {
            controller.all(resp);
        })));
        addHandler(methodGet, pathApiPosts + "/", ((path, req, resp) -> {
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            controller.getById(id, resp);
        }));
        addHandler(methodPost, pathApiPosts, ((path, req, resp) -> {
            try {
                controller.save(req.getReader(), resp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        addHandler(methodDelete, pathApiPosts + "/", ((path, req, resp) -> {
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            controller.removeById(id, resp);
        }));

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();

            // primitive routing
            if (method.equals(methodGet) && path.equals(pathApiPosts)) {
                controller.all(resp);
                return;
            }
            if (method.equals(methodGet) && path.matches(pathApiPosts + "/\\d+")) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
                controller.getById(id, resp);
                return;
            }
            if (method.equals(methodPost) && path.equals(pathApiPosts)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(methodDelete) && path.matches(pathApiPosts + "/\\d+")) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    public void addHandler(String method, String path, Handler handler) {
        Map<String, Handler> map = new ConcurrentHashMap<>();
        if (handlers.containsKey(method)) {
            map = handlers.get(method);
        }
        map.put(path, handler);
        handlers.put(method, map);
    }
}

