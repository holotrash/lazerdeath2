/**
 *  Unit.java
 *  ----  
 *  Units are the closest thing there are to people in the universe of
 *  Lazerdeath2. While the people you know from your life have thoughts
 *  and feelings, Units are organized into one of two groups, Enemies or
 *  Dudes (non-gender-specific). Dudes are controlled by the player and
 *  Enemies are controlled by the EnemyAi hive-mind.
 *  
 *  Woe unto the Unit that is Dude or Enemy, for each in their own turn
 *  must fight the other, over and over, until one is killed and one is
 *  a killer. I wish the lives of the inhabitants of our universe were
 *  much different.
 *  ---------------------------------------------------------------------
 *  This file is part of the computer game Lazerdeath2 
 *  Copyright 2016, Robert Watson Craig III
 *
 *  Lazerdeath2 is free software published under the terms of the GNU
 *  General Public License version 3. You can redistribute it and/or 
 *  modify it under the terms of the GPL (version 3 or any later version).
 * 
 *  Lazerdeath2 is distributed in the hope that it will be entertaining,
 *  cool, and totally smooth for your mind to rock to, daddy, but WITHOUT 
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 *  FITNESS FOR A PARTICULAR PURPOSE; without even the suggestion of an
 *  implication that any of this code makes any sense whatsoever. It works
 *  on my computer and I don't think that's such a weird environment, but
 *  it might be. Or maybe it's your computer that's the weird one, did you
 *  ever think of that?  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Lazerdeath2.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.holotrash.lazerdeath2.Units;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.Items.ConsumableEffect;
import com.holotrash.lazerdeath2.LazerMath.Coord;

public interface Unit {
	
	public void die();
	
	public boolean isDead();
	
	public boolean isDude();
	
	public boolean hasMoved();
	
	public void takeDmg(int dmg);
	
	public int hp();
	
	public int speed();
	
	public int strength();
	
	public int dodge();
	
	public int accuracy();
	
	public int range();
	
	public Coord position();
	
	public Sprite sprite();
	
	public Sprite glamourShot();
	
	public void move(Coord destination);
	
	public String name();

	public ArrayList<String> toStringz();

	public int ap();
	
	public boolean attack(Unit unit);

	public void addEffects(ArrayList<ConsumableEffect> effects);
}
