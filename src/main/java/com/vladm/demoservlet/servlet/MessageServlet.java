package com.vladm.demoservlet.servlet;

import com.vladm.demoservlet.model.Message;
import com.vladm.demoservlet.service.MessageService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

@WebServlet(name = "messageServlet", value = "/message")
public class MessageServlet extends HttpServlet {

    private final MessageService messageService = MessageService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream inputStream = req.getInputStream();
        String text = IOUtils.toString(inputStream);

        String userId = req.getParameter("userId");

        Message msg = messageService.save(userId, text);
        resp.getWriter().println(msg);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("messages", messageService.findAll(req.getParameter("userId")));
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("messages.jsp");

        requestDispatcher.forward(req, resp);
    }
}
