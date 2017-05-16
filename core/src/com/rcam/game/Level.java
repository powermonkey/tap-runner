package com.rcam.game;

/**
 * Created by Rod on 4/28/2017.
 */

public class Level {
    static int pattern;
    static int levelKey;
    public int[][] currentLevel; //ground/flying, spawn count, pattern, monster type
    public int[][] levelOne; //intro patterns
    public int[][] levelTwo; //combo patterns
    public int[][] levelThree; //no formation pattern (spawnMarker was into spawn position)

    public Level(){
        pattern = 0; //set first pattern(e.g. inside curly braces of levelOne) for level
        levelKey = 3; //set first level
        levelOne = new int[][]{
                {1,1,2,1},{1,3,1,1},{2,1,1,1},
                {2,1,2,1},{2,3,1,1},{2,3,2,1},
                {2,3,3,1},{2,3,4,1},
                {2,3,5,1},{2,3,6,1},
                {2,3,7,1},{2,3,8,1}
        }; //start with level 1 (intro patterns)
        levelTwo = new int[][]{
                {1,1,2,2},{2,1,1,2},{1,1,2,2},
                {2,1,1,2},{1,1,2,2},{2,1,1,2},
                {1,3,2,2},{2,3,3,2},{1,3,2,2},
                {2,3,3,2},{1,3,2,2},{2,3,3,2},
                {1,3,2,2},{2,1,1,2},{1,3,2,2},
                {1,1,2,2},{2,3,3,2},{1,1,2,2},
                {2,3,3,2},{2,3,4,2},{2,3,3,2},
                {2,3,4,2},{2,3,3,2},{2,3,4,2},
                {2,3,5,2},{2,3,8,2},{2,3,5,2},
                {2,3,8,2},{2,3,5,2},{2,3,8,2},
                {2,3,6,2},{2,3,7,2},{2,3,6,2},
                {2,3,7,2},{2,3,6,2},{2,3,7,2}
        };
        levelThree = new int[][]{
                {1,1,2,1},{2,1,1,1},{1,1,2,1},
                {2,1,1,1},{1,1,2,1},{2,1,1,1},
                {2,3,9,2},{2,3,12,2},{2,3,9,2},
                {2,3,12,1},{2,3,9,1},{2,3,12,2},
                {2,3,10,2},{2,3,11,2},{2,3,10,2},
                {2,3,11,2},{2,3,10,1},{2,3,11,1},
                {2,3,11,2},{2,3,10,2},{2,3,11,2},
                {2,3,10,2},{2,3,11,1},{2,3,10,2},
                {2,3,9,2},{2,3,12,1},{2,3,9,2},

        };
        currentLevel = levelThree;
    }

    //TODO add level three
    public void updatePattern(){
        if(currentLevel.length - 1 == Level.pattern) {
            if(levelKey == 1) {
                currentLevel = levelTwo;
                levelKey = 2;
            }else if(levelKey == 2) {
                currentLevel = levelThree;
                levelKey = 3;
            }else if(levelKey == 3){
                currentLevel = levelOne;
                levelKey = 1;
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

    public int getLevelKey(){
        return levelKey;
    }
}
