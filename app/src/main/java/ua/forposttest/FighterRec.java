package ua.forposttest;

import java.util.List;

public class FighterRec {
    public List<Fighter> fighters;
    public long timeBetweenSteps;

    public FighterRec(List<Fighter> fighters, long timeBetweenSteps) {
        this.fighters = fighters;
        this.timeBetweenSteps = timeBetweenSteps;
    }

    public List<Fighter> getFighters() {
        return fighters;
    }

    public long getTimeBetweenSteps() {
        return timeBetweenSteps;
    }

    public void setFighters(List<Fighter> fighters) {
        this.fighters = fighters;
    }

    public void setTimeBetweenSteps(long timeBetweenSteps) {
        this.timeBetweenSteps = timeBetweenSteps;
    }
}
