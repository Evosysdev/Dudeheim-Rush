package com.evosysdev.libgdxtest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Dudeheim 
{
	private Rectangle bound;
	private Vector2 pos;
	
	public Dudeheim(Vector2 pos)
	{
		this.pos = pos;
		this.bound = new Rectangle(pos.x, pos.y, 64, 64);
	}
	
	public void addX(float amt)
	{
		pos.x += amt;
		bound.x += amt;
	}
	
	public void setX(float amt)
	{
		pos.x = amt;
		bound.x = amt;
	}
	
	public void setY(float y)
	{
		pos.y = y;
		bound.y = y;
	}
	
	public float getX()
	{
		return pos.x;
	}
	
	public float getY()
	{
		return pos.y;
	}
	
	public Vector2 getPos()
	{
		return pos;
	}
	
	public boolean isIn(Vector2 point)
	{
		return bound.contains(point.x, Gdx.graphics.getHeight() - point.y);
	}
}
