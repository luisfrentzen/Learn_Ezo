package edu.bluejack20_1.learn_ezo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Vector;

public class Player implements Parcelable {
    private String id;
    private String name;
    private int exp;
    private int level;
    private int dayStreak;
    private int follower;
    private int following;
    private int practiceGoal;
    private String dailyReminder;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.exp = 0;
        this.level = 1;
        this.dayStreak = 1;
        this.follower = 0;
        this.following = 0;
        this.practiceGoal = 10;
        this.dailyReminder = "05:00 PM";
    }

    public Player(){

    }
    
    public void setPracticeGoal(int practiceGoal) {
        this.practiceGoal = practiceGoal;
    }

    public void setDailyReminder(String dailyReminder) {
        this.dailyReminder = dailyReminder;
    }

    protected Player(Parcel in) {
        id = in.readString();
        name = in.readString();
        exp = in.readInt();
        level = in.readInt();
        dayStreak = in.readInt();
        follower = in.readInt();
        following = in.readInt();
        this.practiceGoal = in.readInt();
        this.dailyReminder = in.readString();
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

    public int getPracticeGoal() {
        return practiceGoal;
    }

    public String getDailyReminder() {
        return dailyReminder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(exp);
        dest.writeInt(level);
        dest.writeInt(dayStreak);
        dest.writeInt(follower);
        dest.writeInt(following);
        dest.writeInt(practiceGoal);
        dest.writeString(dailyReminder);
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
