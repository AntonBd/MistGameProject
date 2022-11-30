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
        String click = req.getParameter("storyId");
        int storyId = parseStoryId(click);

        session.setAttribute("storyId", storyId);

        //Создание новой истории, получение текста из истории, ссылок и текстов для кнопок, других параметров
        Story story = new Story();
        String[] storyText = story.getStoryText(storyId);

        session.setAttribute("storyText", storyText[0]);
        session.setAttribute("firstLinkId", storyText[2]);
        session.setAttribute("firstLinkText", storyText[3]);
        session.setAttribute("secondLinkId", storyText[4]);
        session.setAttribute("secondLinkText", storyText[5]);

        int newTime = calculateTimeRemains(storyText[1], (int)session.getAttribute("time"));
        session.setAttribute("time", newTime);

        //Работа с параметрами сессии. Название параметра: [6], значение: [7]
        HashMap<String, String> map = new HashMap<>();
        map.put("Здоровье", "health");
        map.put("Оружие", "weapon");
        map.put("Патроны", "bullets");
        map.put("Рюкзак", "backpack");

        if(map.containsKey(storyText[6]) && storyText[7].length() > 1) {
            int sessionParamValue = (int)session.getAttribute(map.get(storyText[6]));
            int changeValue = calculateParamChanging(storyText[7]);
            session.setAttribute(map.get(storyText[6]), (sessionParamValue + changeValue));
        }

        //Если после изменения параметр сессии стал меньше 1
        if ((int)session.getAttribute("time") <= 0 || (int)session.getAttribute("health") <= 0
            || (int)session.getAttribute("bullets") <= 0) {
            session.setAttribute("loss", true);
        }

        resp.sendRedirect("/index.jsp");
    }

    protected int parseStoryId(String click) {
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

    protected int calculateTimeRemains(String timeValue, int currentTime) {
        int time;
        int minusTime;
        try {
            minusTime = Integer.parseInt(timeValue);
        }
        catch(NumberFormatException e) {            //В случае ошибки при парсе времени отбавляется 10 минут
            minusTime = 10;
        }

        time = currentTime - minusTime;
        time = time < 1 ? 0 : time;                 //Исключение показа в игре "отрицательного" времени
        return time;
    }

    protected int calculateParamChanging(String value) {
        int changeValue;                    //Переменная для значения, на которое изменяется параметр сессиии
        try {                               //Парс всего, что идет со 2-го знака 7-ой ячейки массива
            changeValue = Integer.parseInt(value.substring(1));
        }
        catch (NumberFormatException e) {
            changeValue = 0;                //При неуспешном парсе значение переменной не меняется
        }

        if(value.indexOf("+") == 0) {                //Если первый знак значения 7-ой ячейки: "+"
            return changeValue;
        }
        else if (value.indexOf("-") == 0) {
            return -changeValue;
        }
        return 0;                                   //Если первый знак не "+" и не "-", то значение не меняется
    }
}