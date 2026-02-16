package com.mycompany.battle_quest;


import com.mycompany.battle_quest.Character;


public class Warrior extends Character {
    public Warrior(String name, int health, Weapon weapon) {
        super(name, health, weapon);
    }

    @Override
    public void attack(Character target) {
        System.out.println(name + " slashes with " + weapon.getName());
        target.takeDamage(weapon.getDamage());
    }
    
}

