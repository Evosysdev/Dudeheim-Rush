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
	private Stage stage;
	private Skin skin;
	private TweenManager tweens;
	
	private int width = Gdx.graphics.getWidth();
	private int height = Gdx.graphics.getHeight();
	private float theta;
	private float time;
	
	public MainScreen()
	{
		skin = new Skin();
		stage = new Stage();
		tweens = new TweenManager();
		
		Tween.registerAccessor(Dudeheim.class, new DudeheimAccessor());
		
		Gdx.input.setInputProcessor(stage);
		
		Pixmap dudepix = new Pixmap(8, 8, Format.RGBA8888);
		dudepix.setColor(Color.CYAN);
		dudepix.fill();
		dudepix.setColor(Color.BLACK);
		dudepix.drawRectangle(0, 0, 8, 8);
		dudepix.drawLine(2, 2, 2, 3);
		dudepix.drawLine(5, 2, 5, 3);
		dudepix.drawLine(2, 5, 5, 5);
		dudeheimTex = new Texture(dudepix);
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/Amble-Regular.ttf"));
		font = generator.generateFont(14);
		generator.dispose();
		
		Pixmap pixmap = new Pixmap(64, 32, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		pixmap.setColor(Color.WHITE);
		pixmap.fillRectangle(0, 0, 64, 32);
		pixmap.setColor(Color.BLACK);
		pixmap.fillRectangle(5, 5, 54, 22);
		skin.add("button", new Texture(pixmap));
		skin.add("default", new BitmapFont());
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("button", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("button", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("button", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("button", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);
		
		// Create a table that fills the screen. Everything else will go inside this table.
		Table table = new Table();
		table.setFillParent(true);
		table.debug();
		stage.addActor(table);
		
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
	}
	
	private void addDudes()
	{
		for (int i = 0; i < 100; i++)
		{
			Dudeheim heim = new Dudeheim(new Vector2((float)Math.floor(Math.random() * Gdx.graphics.getWidth()),
					  (float)Math.floor(Math.random() * (Gdx.graphics.getHeight() - 70))));
			
			dudes.add(heim);
			Tween.to(heim, DudeheimAccessor.POSITION_XY, 1).target(0, 0).ease(Bounce.OUT).start(tweens);
		}
	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 0.5f, 0, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		//stage.draw();
		//Table.drawDebug(stage);
		
		
		camera.update();
		tweens.update(delta);
		
		//theta += delta * (100 + (100 - dudes.size()) * 8);
		
		
		for (Dudeheim dude : dudes)
		{
			/*
			dude.addX(delta * (100 + (100 - dudes.size()) * 8));
			
			if (dude.getPos().x >= Gdx.graphics.getWidth() + 70)
			{
				dude.setX(-70);
			}*/
			
			sprites.setProjectionMatrix(camera.combined);
			sprites.begin();
			font.setColor(0, 0, 0, 1);
			font.setScale(3);
			font.draw(sprites, Integer.toString(dudes.size()), 50, 50);
			sprites.draw(dudeheimTex, dude.getPos().x, dude.getPos().y, 4, 4,
					dudeheimTex.getWidth(), dudeheimTex.getHeight(), 8, 8, theta, 0, 0, 8, 8, false, false);
			sprites.end();
			
		}
		
		if (Gdx.input.isButtonPressed(Buttons.LEFT))
		{
			for (int i = 0; i < dudes.size(); i++)
			{
				if (dudes.get(i).isIn(new Vector2(Gdx.input.getX(), Gdx.input.getY())))
				{
					dudes.remove(i);
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
