package com.evosysdev.libgdxtest;

import java.awt.Point;
import java.util.ArrayList;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Circ;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainScreen implements Screen
{
	private ArrayList<Dudeheim> dudes;
	private SpriteBatch sprites;
	private Texture dudeheimTex;
	private ShapeRenderer shapes;
	private OrthographicCamera camera;
	private BitmapFont font;
	private Sound bleep;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	
	private int width = Gdx.graphics.getWidth();
	private int height = Gdx.graphics.getHeight();
	private float theta;
	private float time;
	
	public static final float WORLD_TO_BOX = 0.01f;
	public static final float BOX_TO_WORLD = 100;
	
	public MainScreen()
	{
		world = new World(new Vector2(0.0f, 0.0f), true);
		
		debugRenderer = new Box2DDebugRenderer();
		
		Tween.registerAccessor(Dudeheim.class, new DudeheimAccessor());
		
		Pixmap dudepix = new Pixmap(8, 8, Format.RGBA8888);
		dudepix.setColor(Color.CYAN);
		dudepix.fill();
		dudepix.setColor(Color.BLACK);
		dudepix.drawRectangle(0, 0, 8, 8);
		dudepix.drawLine(2, 2, 2, 3);
		dudepix.drawLine(5, 2, 5, 3);
		dudepix.drawLine(2, 5, 5, 5);
		dudeheimTex = new Texture(dudepix);
		
		font = new BitmapFont();
		
		// Create a table that fills the screen. Everything else will go inside this table.
		Table table = new Table();
		table.setFillParent(true);
		table.debug();
		
		//dudeheimTex = new Texture(Gdx.files.internal("data/dudeheim.png"));
		sprites = new SpriteBatch();
		shapes = new ShapeRenderer();
		
		theta = 0;
		
		time = 0;
		
		dudes = new ArrayList<Dudeheim>();
		
		bleep = Gdx.audio.newSound(Gdx.files.internal("data/bleep.wav"));
		
		addDudes();

		camera = new OrthographicCamera(width, height);
		camera.position.set(width / 2, height / 2, 0);
		
		Vector2[] vecs = new Vector2[4];
		
		vecs[0] = new Vector2(0, 0);
		vecs[1] = new Vector2(camera.viewportWidth * WORLD_TO_BOX, 0);
		vecs[2] = new Vector2(camera.viewportWidth * WORLD_TO_BOX, camera.viewportHeight * WORLD_TO_BOX);
		vecs[3] = new Vector2(0, camera.viewportHeight * WORLD_TO_BOX);
		
		BodyDef groundBodyDef = new BodyDef(); 
		groundBodyDef.position.set(new Vector2(0, 0));  

		Body groundBody = world.createBody(groundBodyDef);  

		ChainShape border = new ChainShape();  
		border.createLoop(vecs);
		groundBody.createFixture(border, 0.0f); 
		border.dispose(); 
	}
	
	private void addDudes()
	{
		for (int i = 0; i < 200; i++)
		{
			Dudeheim heim = new Dudeheim(new Vector2((float)Math.floor(Math.random() * (Gdx.graphics.getWidth() - 50) + 25),
					  (float)Math.floor(Math.random() * (Gdx.graphics.getHeight() - 50) + 25)), 32);
			
			dudes.add(heim);
			heim.createBody(world);
		}
	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 0.5f, 0, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		world.step(delta, 6, 2);
		
		camera.update();
		
		
		for (Dudeheim dude : dudes)
		{
			dude.update();
			
			if (dude.getX() < 0)
			{
				dude.setX(Gdx.graphics.getWidth() - 10);
			}
			else if (dude.getX() > Gdx.graphics.getWidth())
			{
				dude.setX(10);
			}
				
			sprites.setProjectionMatrix(camera.combined);
			sprites.begin();
			font.setColor(0, 0, 0, 1);
			font.setScale(3);
			font.draw(sprites, Integer.toString(dudes.size()), 50, 50);
			sprites.draw(dudeheimTex, dude.getX() - 4, dude.getY() - 4, 4, 4, 8, 8, 4, 4,
					(float)Math.toDegrees(dude.getBody().getAngle()), 0, 0, 8, 8, false, false);
			sprites.end();
			
		}
		
		if (Gdx.input.isButtonPressed(Buttons.LEFT))
		{
			for (int i = 0; i < dudes.size(); i++)
			{
				if (dudes.get(i).isIn(new Vector2(Gdx.input.getX(), Gdx.input.getY())))
				{
					world.destroyBody(dudes.remove(i).getBody());
					bleep.play(0.5f);
				}
			}
			
			/*
			shapes.begin(ShapeType.Circle);
			shapes.circle(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 50);
			shapes.end();
			*/
			
		}
		
		if (dudes.size() == 0)
		{
			sprites.begin();
			font.setColor(0, 0, 0, 1);
			font.setScale(3);
			font.draw(sprites, "Good work", Gdx.graphics.getWidth() / 2 - font.getBounds("Good work").width / 2, 
					Gdx.graphics.getHeight() / 2 + font.getBounds("Good work").height / 2);
			sprites.end();
			
			time += delta;
			
			if (time > 5)
			{
				time = 0;
				addDudes();
			}
		}
		
		
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
