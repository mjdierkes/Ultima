package com.masondierkes;

import java.awt.Font;
import java.util.*;

/**
 * The class that describes a monster in the Ultima game
 * 
 * @author Keith Vertanen
 * @author Michele Van Dyne - added commenting
 * 
 * @author YOUR NAME HERE!!
 */
public class Monster implements Runnable
{
	public enum MonsterType {INVALID, SKELETON, ORC, BAT, SLIME};

	private MonsterType type = MonsterType.INVALID;	// type of monster
	private int 		x;							// x location of monster
	private int 		y;							// y location of monster
	private int 		sleepMs;					// delay between times monster moves
	private int 		hp;							// hit points - damage sustained
	private int 		damage;						// damage monster causes
	private World 		world;						// the world the monster moves about in
	private Stats timer = null; 					// elapsed time for showing damage;

	/**
	 * Construct a new monster
	 * 
	 * @param world		- the world the monster moves about in
	 * @param code		- the string code that distinguishes types of monsters
	 * @param x			- the x position of the monster
	 * @param y			- the y position of the monster
	 * @param hp		- hit points - damage sustained by the monster
	 * @param damage	- damage the monster causes
	 * @param sleepMs	- delay between time monster moves
	 */
	public Monster(World world, String code, int x, int y, int hp, int damage, int sleepMs)
	{
		this.world		= world;
		this.x 			= x;
		this.y 			= y;
		this.hp			= hp;
		this.damage		= damage;
		this.sleepMs 	= sleepMs;

		if      (code.toUpperCase().equals("SK")) type = MonsterType.SKELETON;
		else if (code.toUpperCase().equals("OR")) type = MonsterType.ORC;
		else if (code.toUpperCase().equals("BA")) type = MonsterType.BAT;
		else if (code.toUpperCase().equals("SL")) type = MonsterType.SLIME;
	}

	/**
	 * The avatar has attacked a monster!
	 * 
	 * @param points	- number of hit points to be subtracted from monster
	 */
	public void incurDamage(int points)
	{
		hp -= points;
		if (timer == null)
			timer = new Stats();
		timer.reset();
	}

	/**
	 * Draw this monster at its current location
	 */
	public void draw()
	{
		double drawX = (x + 0.5) * Tile.SIZE;
		double drawY = (y + 0.5) * Tile.SIZE;
		switch (type)
		{
		case SKELETON:	StdDraw.picture(drawX, drawY, "skeleton.gif", Tile.SIZE, Tile.SIZE); 	break;
		case ORC:		StdDraw.picture(drawX, drawY, "orc.gif", Tile.SIZE, Tile.SIZE); 		break;
		case BAT:		StdDraw.picture(drawX, drawY, "bat.gif", Tile.SIZE, Tile.SIZE);     	break;
		case SLIME:		StdDraw.picture(drawX, drawY, "slime.gif", Tile.SIZE, Tile.SIZE); 	break;
		default: 														break;
		}

		if ((timer != null) && (timer.elapsedTime() < World.DISPLAY_DAMAGE_SEC))
		{
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setFont(new Font("SansSerif", Font.BOLD, 12));
			StdDraw.text(drawX, drawY, "" + hp);
		}		
	}

	/**
	 * Get the number of hit points the monster has ramaining
	 * 
	 * @return the number of hit points
	 */
	public int getHitPoints()
	{
		return hp;
	}

	/**
	 * Get the amount of damage a monster causes
	 * 
	 * @return amount of damage monster causes
	 */
	public int getDamage()
	{
		return damage;
	}

	/**
	 * Get the x position of the monster
	 * 
	 * @return x position
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Get the y position of the monster
	 * 
	 * @return y position
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Set the new location of the monster
	 * 
	 * @param x the new x location
	 * @param y the new y location
	 */
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Thread that moves the monster around periodically
	 */
	public void run() {

		try {
			while (hp > 0) {
				Direction direction = Direction.random();
				switch (direction){
					case north:
						world.monsterMove(x, y + 1, this);
						break;
					case east:
						world.monsterMove(x + 1, y, this);
						break;
					case south:
						world.monsterMove(x, y - 1, this);
						break;
					case west:
						world.monsterMove(x - 1, y, this);
						break;
				}
				Thread.sleep(sleepMs);
			}
		} catch (InterruptedException e){
			System.out.println(e);
		}

	}

	private enum Direction {
		north, east, south, west;

		private static final List<Direction> VALUES =
				Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static Direction random()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}

}

