package edu.bluejack20_1.learn_ezo;

import java.util.Vector;

public class Player {
    private String id;
    private int exp;
    private int level;
    private int dayStreak;
    private int follower;
    private int following;

    public Player(String id, int exp, int level, int dayStreak, int follower, int following) {
        this.id = id;
        this.exp = exp;
        this.level = level;
        this.dayStreak = dayStreak;
        this.follower = follower;
        this.following = following;
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
}
