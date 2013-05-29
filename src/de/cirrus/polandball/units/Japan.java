package de.cirrus.polandball.units;

import de.cirrus.polandball.Player;
import de.cirrus.polandball.weapons.Minigun;

public class Japan extends Mob {

	public Japan(Player player) {
		super(7, player);
		maxHealth = health = 100;
		speed = 77;
		health = 5;
		weapon = new Minigun(this);
	}

}
