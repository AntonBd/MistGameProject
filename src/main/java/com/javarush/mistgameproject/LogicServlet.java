package com.javarush.mistgameproject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "LogicServlet", value = "/logic")
public class LogicServlet extends HttpServlet {
    private final int STORIES_COUNT = 38;                                           //Количество историй в игре

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //Получение текущей сессии
        HttpSession session = req.getSession();

        //Получение индекса выбранной страницы и установка его значения в параметр сессии
        int storyId = getSelectedIndex(req);
        session.setAttribute("storyId", storyId);

        //Создание новой истории, получение текста из истории, ссылок и текстов для кнопок, других параметров
        Story story = new Story();
        String[] storyText = story.getStoryText(storyId);

        session.setAttribute("storyText", storyText[0]);
        session.setAttribute("firstLinkId", storyText[2]);
        session.setAttribute("firstLinkText", storyText[3]);
        session.setAttribute("secondLinkId", storyText[4]);
        session.setAttribute("secondLinkText", storyText[5]);

        //Работа с параметром сессии: время [1]
        int time = (int)session.getAttribute("time");
        int minusTime;
        try {
            minusTime = Integer.parseInt(storyText[1]);
        }
        catch(NumberFormatException e) {            //В случае ошибки при парсе времени отбавляется 10 минут
            minusTime = 10;
        }

        time = time - minusTime;
        if (time < 1) {
            time = 0;
        }
        session.setAttribute("time", time);

        //Работа с параметрами сессии: Здоровье, Оружие, Патроны. Название параметра: [6], значение: [7]
        HashMap<String, String> map = new HashMap<>();
        map.put("Здоровье", "health");
        map.put("Оружие", "weapon");
        map.put("Патроны", "bullets");
        map.put("Рюкзак", "backpack");

        if(map.containsKey(storyText[6]) && storyText[7].length() > 1) {
            int changeValue;                //Переменная для значения, на которое изменяется параметр сессиии
            try {                           //Парс всего, что идет со 2-го знака 7-ой ячейки массива
                changeValue = Integer.parseInt(storyText[7].substring(1));
            }                               //При неуспешном парсе значение переменной не меняется
            catch (NumberFormatException e) {
                changeValue = 0;
            }

            int sessionParamValue = (int)session.getAttribute(map.get(storyText[6]));
            if(storyText[7].indexOf("+") == 0) {                //Если первый знак значения 7-ой ячейки: "+"
                session.setAttribute(map.get(storyText[6]), (sessionParamValue + changeValue));
            }
            else if(storyText[7].indexOf("-") == 0) {
                session.setAttribute(map.get(storyText[6]), (sessionParamValue - changeValue));
            }
        }

        //Если после изменения параметр сессии стал меньше 1
        if ((int)session.getAttribute("time") <= 0 || (int)session.getAttribute("health") <= 0
            || (int)session.getAttribute("bullets") <= 0) {
            session.setAttribute("loss", true);
        }

        resp.sendRedirect("/index.jsp");
    }

    private int getSelectedIndex(HttpServletRequest request) {
        String click = request.getParameter("storyId");
        int value;

        try {
            value = Integer.parseInt(click);
            if (value < 0 || value > STORIES_COUNT) {
                value = 404;
            }
        }
        catch (NumberFormatException e) {
            value = 404;                            //При неуспешном парсе значения - показ страницы об ошибке
        }

        return value;
    }
}