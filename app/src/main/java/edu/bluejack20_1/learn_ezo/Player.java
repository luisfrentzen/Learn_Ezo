package edu.bluejack20_1.learn_ezo;

import java.util.Vector;

public class Player {
    private String id;
    private String name;
    private int exp;
    private int level;
    private int dayStreak;
    private int follower;
    private int following;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.exp = 0;
        this.level = 1;
        this.dayStreak = 1;
        this.follower = 0;
        this.following = 0;
    }

    public String getId() {
        return id;
    }

    public int getExp() {
        return exp;
    }

    public int getLevel() {
        return level;
    }

    public int getDayStreak() {
        return dayStreak;
    }

    public int getFollower() {
        return follower;
    }

    public int getFollowing() {
        return following;
    }

    public String getName() {
        return name;
    }
}
