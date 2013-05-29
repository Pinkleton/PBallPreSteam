package de.cirrus.polandball.units;

import de.cirrus.polandball.Player;
import de.cirrus.polandball.weapons.FlameThrower;

public class Germany extends Mob {

	public Germany(Player player) {
		super(1, player);
		maxHealth = health = 1750;
		speed = 100;

		weapon = new FlameThrower(this);
	}

}
