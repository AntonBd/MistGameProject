package com.javarush.mistgameproject;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

public class Story {
    private String storyText = "";

    public String[] getStoryText(int storyId) {

        String path = "stories/story-"+storyId+".txt";                      //Конструирование адреса ссылки на историю
        URL resource = getClass().getClassLoader().getResource(path);       //URL папки resource

        try(BufferedReader reader = new BufferedReader(new FileReader(new File(resource.toURI())))) {
                while (reader.ready()) {
                    String line = reader.readLine();
                    storyText = storyText + line;
                }
            }
        catch (IOException | NullPointerException e) {
            storyText = "Возникла ошибка: файл с текстом не найден";
        } catch (URISyntaxException e) {
            storyText = "Неверный формат адреса ссылки на текст";
        }

        String[] inital = storyText.split("\\|");      //Деление текста по знаку |
        return Arrays.copyOf(inital, 8);           //Возвращение массива с частями текста
    }
}