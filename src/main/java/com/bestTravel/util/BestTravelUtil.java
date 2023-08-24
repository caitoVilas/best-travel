package com.bestTravel.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

public class BestTravelUtil {
    public static final Random random = new Random();

    public static LocalDateTime getRandomSoom(){
        var randomHour = random.nextInt(5-2)+ 2;
        var now = LocalDateTime.now();
        return now.plusHours(randomHour);
    }

    public static LocalDateTime getRandomLatter(){
        var randomHour = random.nextInt(12-6)+ 6;
        var now = LocalDateTime.now();
        return now.plusHours(randomHour);
    }

    public static void writeNotification(String text, String path)throws IOException {
        var fileWritter = new FileWriter(path, true);
        var bufferedWritter = new BufferedWriter(fileWritter);
        try(fileWritter; bufferedWritter){
            bufferedWritter.write(text);
            bufferedWritter.newLine();
        }
    }
}
