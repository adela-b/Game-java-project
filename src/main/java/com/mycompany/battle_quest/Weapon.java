package com.mycompany.battle_quest;

import java.util.Random;

public class Weapon {
    private String name;
    private int damage;

    public Weapon(String name) {
        Random random = new Random();
        this.name = name;
        this.damage = 10 + random.nextInt(11); // random 10–20
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }
}

