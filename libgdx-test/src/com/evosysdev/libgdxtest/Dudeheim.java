package com.evosysdev.libgdxtest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Dudeheim 
{
	private Rectangle bound;
	private Vector2 pos;
	private Body body;
	
	public Dudeheim(Vector2 pos, float size)
	{
		this.pos = pos;
		this.bound = new Rectangle(pos.x, pos.y, size, size);
	}
	
	public void createBody(World world)
	{
		
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(pos.x * MainScreen.WORLD_TO_BOX, pos.y * MainScreen.WORLD_TO_BOX);
		
		body = world.createBody(bodyDef);
		
		PolygonShape box = new PolygonShape();
		box.setAsBox(bound.getWidth() / 2 * MainScreen.WORLD_TO_BOX, bound.getHeight() / 2 * MainScreen.WORLD_TO_BOX);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = box;
		fixture.density = 0.1f;
		fixture.friction = 0.0f;
		fixture.restitution = 1.5f;
		
		body.createFixture(fixture);
		
		body.setAwake(true);
		body.applyForceToCenter((float)Math.random() * 100 - 50f, (float)Math.random() * 100 - 50f);
		
		box.dispose();
	}
	
	public void update()
	{
		pos.x = body.getPosition().x * MainScreen.BOX_TO_WORLD;
		bound.x = body.getPosition().x * MainScreen.BOX_TO_WORLD;
		pos.y = body.getPosition().y * MainScreen.BOX_TO_WORLD;
		bound.y = body.getPosition().y * MainScreen.BOX_TO_WORLD;
	}
	
	public Body getBody()
	{
		return body;
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
		body.getPosition().x = amt * MainScreen.WORLD_TO_BOX;
	}
	
	public void setY(float y)
	{
		pos.y = y;
		bound.y = y;
		body.getPosition().y = y * MainScreen.WORLD_TO_BOX;
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
		return body.getFixtureList().get(0).testPoint(point.x * MainScreen.WORLD_TO_BOX, (Gdx.graphics.getHeight() - point.y) * MainScreen.WORLD_TO_BOX);
	}
}
