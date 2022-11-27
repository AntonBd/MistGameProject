package com.javarush.mistgameproject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "InitServlet", value = "/start")
public class InitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        //Создание новой сессии
        HttpSession session = req.getSession(true);

        Story story = new Story();

        //Создание переменных параметров сессии
        int storyId = 0;
        String[] storyText = story.getStoryText(storyId);

        int time = 300;
        int health = 3;
        int weapon = 0;
        int bullets = 1;
        int backpack = 0;

        //Добавление параметров сессии
        session.setAttribute("storyText", storyText[0]);
        session.setAttribute("firstLinkId", storyText[2]);
        session.setAttribute("firstLinkText", storyText[3]);
        session.setAttribute("secondLinkId", "empty");             //Для отображения одной кнопки в первой истории

        session.setAttribute("storyId", storyId);
        session.setAttribute("time", time);
        session.setAttribute("health", health);
        session.setAttribute("weapon", weapon);
        session.setAttribute("bullets", bullets);
        session.setAttribute("backpack", backpack);
        session.setAttribute("loss", false);

        //Перенаправление на главную страницу игры
        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
