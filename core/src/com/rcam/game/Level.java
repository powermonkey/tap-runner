package com.rcam.game;

/**
 * Created by Rod on 4/28/2017.
 */

public class Level {
    static int pattern; //the enemy pattern inside a level
    static int levelKey;
    public int[][] levelHolder; // holds current level
    public int[][] levelOne; //intro patterns
    public int[][] levelTwo; //combo patterns
    public int[][] levelThree; //no formation pattern
    public int[][] levelFour; //more combo patterns

    public Level(){
        pattern = 0; //set first pattern(e.g. inside curly braces of levelOne) for level
        levelKey = 4; //set first level

        // 0 ground/flying
        // 1 spawn count
        // 2 pattern
        // 3 monster type
        // 4 height adjustment(0-none,1-ground, 2-above ground, 3-two enemies on top height level)
        // 5 distance between enemies(0-false,1-same position, 2-one enemy distance...) needs to be multiplied by spawn count if spawn count greater than 1

        levelOne = new int[][]{
                {1,1,1,1,0,0},{1,3,1,1,0,0},
                {2,1,1,1,1,0},{2,1,1,1,0,0},
                {2,3,1,1,1,0},{2,3,1,1,0,0},
                {2,3,2,1,1,0},{2,3,2,1,0,0},
                {2,3,3,1,1,0},{2,3,3,1,0,0},
                {2,3,4,1,1,0},{2,3,4,1,0,0}
        };
        levelTwo = new int[][]{
                {1,1,1,2,0,0},{2,1,1,2,1,0},{1,1,1,2,0,0},
                {2,1,2,2,1,0},{1,1,1,2,0,0},{2,1,2,2,1,0},
                {1,3,1,2,0,0},{2,3,2,2,1,0},{1,3,1,2,0,0},
                {2,3,2,2,1,0},{1,3,1,2,0,0},{2,3,2,2,1,0},
                {1,3,1,2,0,0},{2,1,1,2,1,0},{1,3,1,2,0,0},
                {1,1,1,2,0,0},{2,3,2,2,1,0},{1,1,1,2,0,0},
                {2,3,2,2,0,0},{2,3,2,2,1,0},{2,3,2,2,0,0},
                {2,3,2,2,1,0},{2,3,2,2,0,0},{2,3,2,2,1,0},
                {2,3,3,2,1,0},{2,3,4,2,0,0},{2,3,3,2,1,0},
                {2,3,4,2,0,0},{2,3,3,2,1,0},{2,3,4,2,0,0},
                {2,3,3,2,1,0},{2,3,4,2,0,0},{2,3,3,2,1,0},
                {2,3,4,2,0,0},{2,3,3,2,1,0},{2,3,4,2,0,0},

        };
        levelThree = new int[][]{
                {2,1,2,1,1,0},{1,1,1,2,0,0},{2,1,2,1,1,0},
                {1,1,1,2,0,0},{2,1,2,1,1,0},{1,1,1,2,0,0},
                {2,3,5,2,1,0},{2,3,6,1,0,0},{2,3,5,2,1,0},
                {2,3,6,1,0,0},{2,3,5,2,1,0},{2,3,6,1,0,0},
                {2,3,5,2,1,0},{2,3,6,1,0,0},{2,3,5,2,1,0},
                {2,3,6,1,1,0},{2,3,5,2,0,0},{2,3,6,1,1,0},
                {2,3,6,2,1,0},{2,3,5,1,0,0},{2,3,6,2,1,0},
                {2,3,5,1,0,0},{2,3,6,2,1,0},{2,3,5,1,0,0},
                {2,3,5,2,0,0},{2,3,6,1,1,0},{2,3,5,2,0,0},

        };
        levelFour = new int[][]{
                {1,10,1,1,0,2},{0,0,0,0,0,0},{2,1,1,1,2,0},
//                {1,5,1,1,0,2},{1,5,1,1,0,2},{1,5,1,1,0,2},{1,5,1,1,0,2},
//                {1,5,1,1,0,2},{1,5,1,1,0,2},{1,5,1,1,0,2},{1,5,1,1,0,2},{1,5,1,1,0,2},{1,5,1,1,0,2},
//                {1,1,1,1,0,3},{1,1,1,1,0,3},{1,1,1,1,0,3},{1,1,1,1,0,3},{1,1,1,1,0,3},{1,1,1,1,0,3},{1,1,1,1,0,3},
//                {2,1,1,1,2,2},{2,1,1,1,3,2},{2,1,1,1,4,1},{1,1,1,1,0,2},{2,1,1,1,3,2},{2,1,1,1,2,0},{0,0,0,0,0,0},
//                {2,1,1,1,2,1},{1,5,1,1,0,2},{2,1,1,1,3,2},{2,1,1,1,4,2},{2,1,1,1,3,2},{2,1,1,1,2,2},
//                {2,1,1,1,2,1},{1,5,1,1,0,2},{2,1,1,1,3,2},{2,1,1,1,4,2},{2,1,1,1,3,2},{2,1,1,1,2,2},
//                {2,1,1,1,2,1},{1,5,1,1,0,2},{2,1,1,1,3,2},{2,1,1,1,4,2},{2,1,1,1,3,2},{2,1,1,1,2,2},
//                {2,1,1,1,2,1},{1,5,1,1,0,2},{2,1,1,1,3,2},{2,1,1,1,4,2},{2,1,1,1,3,2},{2,1,1,1,2,0},{0,0,0,0,0,0},
//                {2,1,1,1,2,2},{2,1,1,1,3,1},{1,1,1,1,0,2},{2,1,1,1,2,0},{0,0,0,0,0,0},
//                {2,1,1,1,2,2},{2,1,1,1,3,1},{1,1,1,1,0,2},{2,1,1,1,2,0},{0,0,0,0,0,0},
//                {2,1,1,1,2,2},{2,1,1,1,3,1},{1,1,1,1,0,2},{2,1,1,1,2,0},{0,0,0,0,0,0},
//                {2,1,1,1,2,2},{2,1,1,1,3,1},{1,1,1,1,0,2},{2,1,1,1,2,0},{0,0,0,0,0,0},
//                {2,3,7,2,0},{2,3,8,2,0},
//                {2,3,4,1,0},{1,3,2,1,0},{2,3,5,1,0},{0,0,0,0,0},
        };
        levelHolder = levelFour;
    }

    //TODO add level three
    public void updatePattern(){
        if(levelHolder.length - 1 == Level.pattern) {
            if(levelKey == 1) {
                levelHolder = levelTwo;
                levelKey = 2;
            }else if(levelKey == 2) {
                levelHolder = levelThree;
                levelKey = 3;
            }else if(levelKey == 3){
                levelHolder = levelFour;
                levelKey = 4;
            }else{
                levelHolder = levelOne;
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

    public int[] getLevelPattern(int pattern){
        return levelHolder[pattern];
    }

    public int getLevelKey(){
        return levelKey;
    }
}
