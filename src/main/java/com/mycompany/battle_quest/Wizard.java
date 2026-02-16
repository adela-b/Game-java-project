package com.mycompany.battle_quest;


import com.mycompany.battle_quest.Character;

public class Wizard extends Character {
    public Wizard(String name, int health, Weapon weapon) {
        super(name, health, weapon);
    }

    @Override
    public void attack(Character target) {
        System.out.println(name + " casts a spell with " + weapon.getName());
        target.takeDamage(weapon.getDamage());
    }
}
