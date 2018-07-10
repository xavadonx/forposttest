package ua.forposttest;

import java.io.Serializable;

public class Fighter implements Serializable {
    public int id;
    public float position_lon;
    public float position_lat;
    public int health;
    public int type;
    public String team;
    public int clips;
    public int ammo;
}
