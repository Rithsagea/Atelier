package com.tempera.test;

import java.util.Random;

import com.rithsagea.util.rand.Dice;
import com.rithsagea.util.rand.Die;

public class DiceTest {
	public static void main(String[] args) {
		
		Random rand = new Random();
		long seed = 29l;
		Die die1 = new Die(4, 20);
		Die die2 = new Die(8, 6);
		
		rand.setSeed(seed);
		System.out.println(die1);
		System.out.println(die1.roll(rand));
		
		System.out.println(die2);
		System.out.println(die2.roll(rand));
		
		Dice dice = new Dice();
		dice.addDie(die1);
		dice.addDie(die2);
		
		rand.setSeed(seed);
		System.out.println(dice);
		System.out.println(dice.roll(rand));
		rand.setSeed(seed);
		System.out.println(dice.roll(rand).stream().mapToInt(Integer::intValue).sum());
	}
}
