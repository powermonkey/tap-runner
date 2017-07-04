package com.rcam.game;

/**
 * Created by Rod on 4/28/2017.
 */

public class Level {
    static int pattern; //the enemy pattern inside a level
    static int levelKey;
    private static float lavaMarkerOffset;
    public boolean isEndOfLevel;
    public int[][] levelHolder; // holds current level
    public int[][] levelOne; //intro patterns
    public int[][] levelTwo; //combo patterns
    public int[][] levelThree; //no formation pattern
    public int[][] levelFour; //more combo patterns

    public Level(){
        pattern = 0; //set first pattern(e.g. inside curly braces of levelOne) for level
        levelKey = 1; //set first level

        // 0 ground/flying
        // 1 spawn count
        // 2 pattern
        // 3 monster type
        // 4 height adjustment(0-none,1-ground, 2-above ground, 3-two enemies on top height level)
        // 5 distance between enemies(0-false,1-same position, 2-one enemy distance...) needs to be multiplied by spawn count if spawn count greater than 1

        levelOne = new int[][]{
                {1,1,1,1,0,5},{1,3,1,1,0,7},
                {2,1,1,1,1,5},{2,1,1,1,0,5},
                {2,3,1,1,1,7},{2,3,1,1,0,7},
                {2,3,2,1,1,7},{2,3,2,1,0,5},
                {2,3,3,1,1,7},{2,3,3,1,0,7},
                {2,3,4,1,1,7},{2,3,4,1,0,7}
        };
        levelTwo = new int[][]{
//                {0,0,0,0,0,0}
                {1,1,1,2,0,5},{2,1,1,2,1,5},{1,1,1,2,0,5},
                {2,1,2,2,1,5},{1,1,1,2,0,5},{2,1,2,2,1,5},
                {1,3,1,2,0,7},{2,3,2,2,1,6},{1,3,1,2,0,7},
                {2,3,2,2,1,6},{1,3,1,2,0,6},{2,3,2,2,1,6},
                {1,3,1,2,0,7},{2,1,1,2,1,5},{1,3,1,2,0,7},
                {1,1,1,2,0,5},{2,3,2,2,1,5},{1,1,1,2,0,5},
                {2,3,2,2,0,7},{2,3,2,2,1,7},{2,3,2,2,0,7},
                {2,3,2,2,1,7},{2,3,2,2,0,7},{2,3,2,2,1,7},
                {2,3,3,2,1,7},{2,3,4,2,0,7},{2,3,3,2,1,7},
                {2,3,4,2,0,7},{2,3,3,2,1,7},{2,3,4,2,0,7},
                {2,3,3,2,1,7},{2,3,4,2,0,7},{2,3,3,2,1,7},
                {2,3,4,2,0,7},{2,3,3,2,1,7},{2,3,4,2,0,7}
        };
        levelThree = new int[][]{
//                {0,0,0,0,0,0}
                {2,1,2,1,1,5},{1,1,1,2,0,5},{2,1,2,1,1,5},
                {1,1,1,2,0,5},{2,1,2,1,1,5},{1,1,1,2,0,5},
                {2,3,5,2,1,7},{2,3,6,1,0,7},{2,3,5,2,1,7},
                {2,3,5,2,1,7},{2,3,6,1,0,7},{2,3,5,2,1,7},
                {2,3,6,1,0,7},{2,3,5,2,1,7},{2,3,6,1,0,7},
                {2,3,6,1,1,7},{2,3,5,2,0,7},{2,3,6,1,1,7},
                {2,3,6,2,1,7},{2,3,5,1,0,7},{2,3,6,2,1,7},
                {2,3,5,1,0,7},{2,3,6,2,1,7},{2,3,5,1,0,7},
                {2,3,5,2,0,7},{2,3,6,1,1,7},{2,3,5,2,0,7},
                {2,3,6,2,1,7},{2,3,5,1,0,7},{2,3,6,2,1,10}
        };

        // /\/\, house, /\, \o , vo, _=, o/, -_, !,i,!,i
        levelFour = new int[][]{
//                {0,0,0,0,0,0}
                {2,1,1,1,1,5},{2,1,1,1,0,5},
                {2,3,1,1,0,7},{2,3,1,1,1,7},
                {1,1,1,1,0,5},{2,1,1,1,1,5},
                {1,1,1,1,0,2},{2,1,1,1,3,2},{1,1,1,1,0,2},{2,1,1,1,3,2},{1,1,1,1,0,5},
                {2,1,1,1,2,2},{2,1,1,1,3,2},{2,1,1,1,4,1},{1,1,1,1,0,2},{2,1,1,1,3,2},{2,1,1,1,2,5},
                {1,1,1,1,0,3},{2,1,1,1,3,3},{1,1,1,1,0,5},
                {2,3,4,1,2,5},{1,1,1,1,0,5},
                {2,1,1,1,4,2},{2,1,1,1,3,1},{1,1,1,1,0,2},{2,1,1,1,4,5},
                {1,2,1,1,0,4},{2,2,1,1,2,4},{2,2,1,1,4,5},
                {1,1,1,1,0,3},{2,3,3,1,2,5},
                {2,2,1,1,2,4},{1,2,1,1,0,5},
                {2,2,2,1,3,1},{1,1,1,1,0,6},
                {2,2,2,1,0,1},{2,1,2,1,4,6},
                {2,2,2,1,3,1},{1,1,1,1,0,6},
                {2,2,2,1,0,1},{2,1,2,1,4,8}

        };
        levelHolder = levelOne;
    }

    //lavaMarkerOffset > for less lava
    public void updatePattern(){
        if(levelHolder.length - 1 == Level.pattern) {
            if(levelKey == 1) {
                levelHolder = levelTwo;
                lavaMarkerOffset = 650;
                levelKey = 2;
            }else if(levelKey == 2) {
                lavaMarkerOffset = 500;
                levelHolder = levelThree;
                levelKey = 3;
            }else if(levelKey == 3){
                lavaMarkerOffset = 210;
                levelHolder = levelFour;
                levelKey = 4;
            }else{
                lavaMarkerOffset = 680;
                levelHolder = levelOne;
                levelKey = 1;
            }
            Level.pattern = 0;
            isEndOfLevel = true;
        }else{
            Level.pattern += 1;
            isEndOfLevel = false;
        }
    }

    public int getPattern(){
        return pattern;
    }

    public float getLavaMarkerOffset(){
        return lavaMarkerOffset;
    }

    public int[] getLevelPattern(int pattern){
        return levelHolder[pattern];
    }

    public int getLevelKey(){
        return levelKey;
    }
}
