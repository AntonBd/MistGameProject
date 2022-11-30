package com.javarush.mistgameproject;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StoryTest {
    Story story = new Story();
    String[] storyTest;
    String[] comparisonArray;
    Throwable exception;

    @Test
    void countsOfArraySellsTest() throws IOException {
        storyTest = story.getStoryText(100);
        int value = 8;
        assertEquals(value, storyTest.length);
    }

    @Test
    void fullTextReaderTest() throws IOException {
        storyTest = story.getStoryText(100);
        comparisonArray = new String[] {"Тестовый текст", "20", "4", "Ссылка первая", "5", "Ссылка вторая",
                                                 "Предмет", "+5"};
        //При сохранении файла в UTF-8 добавляется BOM marker, что вызывает ошибку при тестировании
        //Перед сравнением массивов необходимо удалить этот маркер из 0-ой ячейки:
        storyTest[0] = storyTest[0].replace("\uFEFF", "");
        assertArrayEquals(comparisonArray, storyTest);
    }

    @Test
    void middleTextReaderTest() throws IOException {
        storyTest = story.getStoryText(101);
        comparisonArray = new String[] {"Тестовый текст", "20", "4", "Ссылка первая", "5", "Ссылка вторая", null, null};
        storyTest[0] = storyTest[0].replace("\uFEFF", "");
        assertArrayEquals(comparisonArray, storyTest);
    }

    @Test
    void shortTextReaderTest() throws IOException {
        storyTest = story.getStoryText(102);
        comparisonArray = new String[] {"Тестовый текст", "20", "4", "Ссылка первая", null, null, null, null};
        storyTest[0] = storyTest[0].replace("\uFEFF", "");
        assertArrayEquals(comparisonArray, storyTest);
    }

    @Test
    void bitTextReaderTest() throws IOException {
        storyTest = story.getStoryText(103);
        comparisonArray = new String[] {"Тестовый текст", "20", null, null, null, null, null, null};
        storyTest[0] = storyTest[0].replace("\uFEFF", "");
        assertArrayEquals(comparisonArray, storyTest);
    }

    @Test
    void exceptionTest() {
        exception = assertThrows(IOException.class,
                () -> storyTest = story.getStoryText(110));
        assertEquals("Файл с текстом не найден либо передано пустое значение", exception.getMessage());
    }
}
