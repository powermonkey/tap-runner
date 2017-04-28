package com.rcam.game;

/**
 * Created by Rod on 4/28/2017.
 */

public class Level {
    static int pattern;
    static int levelKey;
    public int[][] currentLevel; //ground/flying, spawn count, pattern
    public int[][] levelOne; //intro patterns
    public int[][] levelTwo; //combo patterns
    public int[][] levelThree;

    public Level(){
        pattern = 0; //set first pattern for level
        levelKey = 1; //set first level
        levelOne = new int[][]{{1,1,2},{1,3,2},{2,1,1},{2,1,2},{2,3,1},{2,3,2},{2,3,3},{2,3,4},{2,3,5},{2,3,6},{2,3,7},{2,3,8}}; //start with level 1 (intro patterns)
        levelTwo = new int[][]{
                {1,1,2},{2,1,1},{1,1,2},
                {2,1,1},{1,1,2},{2,1,1},
                {1,3,2},{2,3,3},{1,3,2},
                {2,3,3},{1,3,2},{2,3,3},
                {1,3,2},{2,1,1},{1,3,2},
                {1,1,2},{2,3,3},{1,1,2},
                {2,3,3},{2,3,4},{2,3,3},
                {2,3,4},{2,3,3},{2,3,4},
                {2,3,5},{2,3,8},{2,3,5},
                {2,3,8},{2,3,5},{2,3,8},
                {2,3,6},{2,3,7},{2,3,6},
                {2,3,7},{2,3,6},{2,3,7}
        };
        currentLevel = levelOne;
    }

    public void updatePattern(){
        if(currentLevel.length - 1 == Level.pattern) {
            if(levelKey == 1) {
                currentLevel = levelTwo;
                levelKey = 2;
            }else if(levelKey == 2){
                currentLevel = levelThree;
                levelKey = 3;
            }else{
                currentLevel = levelOne;
                levelKey = 1;
            }
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
