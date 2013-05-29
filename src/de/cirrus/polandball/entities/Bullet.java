package de.cirrus.polandball.entities;

import de.cirrus.polandball.Bitmap;
import de.cirrus.polandball.Team;
import de.cirrus.polandball.units.Mob;
import de.cirrus.polandball.units.Unit;
import de.cirrus.polandball.weapons.Weapon;

public class Bullet extends Entity {
	public Mob owner;
	public Weapon weapon;
	public double xo, yo, zo;
	public double xStart, yStart, zStart;
	public int dmg;


	public Bullet(Mob owner, Weapon weapon, double xa, double ya, double za, int dmg) {
		this(owner, weapon, xa, ya, za, dmg, 4);

	}

	public Bullet(Mob owner, Weapon weapon, double xa, double ya, double za, int dmg, int speed) {
		this.owner = owner;
		this.weapon = weapon;
		this.xo = this.x = owner.x;
		this.yo = this.y = owner.y;
		this.zo = this.z = owner.z + 5;
		xStart = x;
		yStart = y;
		zStart = z;
		this.xa = xa * speed;
		this.ya = ya * speed;
		this.za = za * speed;
		this.dmg = dmg;
		isCollideable = false;
	}

	public boolean blocks(Entity e) {
		if (e == owner) return false;
		if (e instanceof Bullet) return false;
		if (e instanceof Mob && ((Mob) e).team == owner.team) return false;
		return true;
	}

	public void tick() {
		xo = x;
		yo = y;
		zo = z;
		super.tick();
		attemptMove();
	}

	public void renderShadows(Bitmap b, int xp, int yp) {
		double xd = xo - x;
		double yd = yo - y;
		double zd = zo - z;

		int steps = (int) (Math.sqrt(xd * xd + yd * yd) + 1);
		for (int i = 0; i < 1; i++) {
			double zz = 0;
			b.setPixel((int) (x + xd * i / steps), (int) (y + yd * i / steps - zz), 1);
		}
	}

	public void render(Bitmap b, int xp, int yp) {
		double xd = xo - x;
		double yd = yo - y;
		double zd = zo - z;

		int steps = (int) (Math.sqrt(xd * xd + yd * yd) + 1);
		for (int i = 0; i < steps; i++) {
			if (Math.random() * steps < i) continue;
			int br = 200 - i * 200 / steps;
			int col = 0;
			if (owner.team == Team.allied) {
				col = 0xff0000ff | (0x010100 * br);
			} else {
				col = 0xffff0000 | (0x000101 * br);
			}
			b.setPixel((int) (x + xd * i / steps), (int) (y + yd * i / steps), col);
		}
	}

	public void applyHitEffect(Unit unit) {

	}

	public void collide(Entity e, double xxa, double yya, double zza) {
		if (e != null) {
			e.hitBy(this);
		}
		remove();
	}

	public int getDamage(Unit unit) {
		return getDamage(unit.x, unit.y, unit.z);
	}

	public int getDamage(double xx, double yy, double zz) {
		double xd = xStart - xx;
		double yd = yStart - yy;
		double zd = zStart - zz;
		double distanceTravelled = Math.sqrt(xd * xd + yd * yd + zd * zd) * 5;
		double dmg = this.dmg;

		if (distanceTravelled < weapon.lowRamp) {
			dmg *= weapon.highRamp / 100.0;
		} else if (distanceTravelled > weapon.farDistance) {
			dmg *= weapon.lowRamp / 100.0;
		} else if (distanceTravelled < weapon.midDistance) {
			double fraction = 1 - (distanceTravelled - weapon.nearDistance) / (weapon.midDistance - weapon.nearDistance);
			dmg *= (weapon.highRamp * fraction + 100 * (1 - fraction)) / 100.0;
		} else {
			double fraction = 1 - (distanceTravelled - weapon.midDistance) / (weapon.farDistance - weapon.midDistance);
			dmg *= (weapon.highRamp * fraction + 100 * (1 - fraction)) / 100.0;
		}
		int d = (int) (Weapon.random.nextDouble() + dmg);
		return d;
	}
}
