package util;

import java.util.Random;

public class Randomizer {

    public static int getRandomNumber(int min, int max){
        return new Random().nextInt((max-min + 1)+min);
    }
}
