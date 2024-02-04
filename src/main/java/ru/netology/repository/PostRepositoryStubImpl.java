package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class PostRepositoryStubImpl implements PostRepository {
    Map<Long, Post> stockPosts = new ConcurrentHashMap<>();


    public List<Post> all() {
        return new ArrayList<>(stockPosts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(stockPosts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            if (!stockPosts.isEmpty()) {
                post.setId(Collections.max(stockPosts.keySet()) + 1);
            }
            stockPosts.put(post.getId(), post);

        } else {
            if (stockPosts.containsKey(post.getId())) {
                stockPosts.put(post.getId(), post);
            } else {
                return null;
            }

        }
        return post;
    }

    public void removeById(long id) {
        stockPosts.remove(id);
    }
}
