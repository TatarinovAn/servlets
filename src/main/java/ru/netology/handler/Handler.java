package ru.netology.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface Handler {
    public void handler(String path, HttpServletRequest req, HttpServletResponse resp);
}
