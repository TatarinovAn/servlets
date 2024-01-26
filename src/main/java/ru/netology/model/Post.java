package ru.netology.model;

import java.util.concurrent.atomic.AtomicLong;

public class Post {
    private AtomicLong id;
    private String content;

    public Post() {
    }

    public Post(long id, String content) {
        this.id.set(id);
        this.content = content;
    }

    public long getId() {
        return id.get();
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
