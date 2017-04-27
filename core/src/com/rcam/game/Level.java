package com.rcam.game;

/**
 * Created by Rod on 4/28/2017.
 */

public class Level {
    static int pattern;
    public int[][] currentLevel;
    public int[][] levelOne; //ground/flying, spawn count, pattern
    public int[][] levelTwo;
    public int[][] levelThree;

    public Level(){
        pattern = 0; //set first pattern for level
        currentLevel = new int[][]{{1,1,2},{1,3,2},{2,1,1},{2,1,2},{2,3,1},{2,3,2},{2,3,3},{2,3,4},{2,3,5},{2,3,6},{2,3,7},{2,3,8}}; //start with level 1 (intro patterns)
    }

    public void updatePattern(){
        if(currentLevel.length - 1 == Level.pattern) {
            Level.pattern = 0;
        }else{
            Level.pattern += 1;
        }
    }

    public int getPattern(){
        return pattern;
    }

    public int[] getLevelPattern(int levelPattern){
        return currentLevel[levelPattern];
    }
}
