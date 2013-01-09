package com.PsichiX.HelloWorld;

import com.PsichiX.XenonCoreDroid.XeApplication.*;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.Framework.Actors.*;
import com.PsichiX.XenonCoreDroid.XeUtils.*;
import com.PsichiX.XenonCoreDroid.XeSense;
import java.util.Random;

public class GameState extends State implements CommandQueue.Delegate
{
	private Camera2D _cam;
	private Scene _scn;
	private ShapeComparator.DescZ _sorter = new ShapeComparator.DescZ();
	private ActorsManager _actors = new ActorsManager();
	private CollisionManager _colls = new CollisionManager();
	private CommandQueue _cmds = new CommandQueue();
	private Stars _stars;
	private Ship _ship;
	private Font _font;
	private Material _fontMat;
	private Text _energyText;
	private Text _scoreText;
	private float _energy = 0.0f;
	private int _score = 0;
	private float _respawn = 1.2f;
	private float _spawnAccel = 0.0f;
	
	@Override
	public void onEnter()
	{
		_cmds.setDelegate(this);
		
		_scn = (Scene)getApplication().getAssets().get(R.raw.scene, Scene.class);
		_cam = (Camera2D)_scn.getCamera();
		
		Material mat;
		Image img;
		
		mat = (Material)getApplication().getAssets().get(R.raw.stars_material, Material.class);
		_stars = new Stars(mat,
			getApplication().getPhoton().getRenderer().getScreenWidth() *
			getApplication().getPhoton().getRenderer().getScreenHeight() /
			1000
			);
		_stars.randomize(-1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 3.0f, 1.0f, 1.5f);
		_scn.attach(_stars);
		
		mat = (Material)getApplication().getAssets().get(R.raw.ship_material, Material.class);
		img = (Image)getApplication().getAssets().get(R.drawable.ship, Image.class);
		_ship = new Ship(mat);
		_ship.setSizeFromImage(img);
		_ship.setOffsetFromSize(0.5f, 0.5f);
		_ship.setRange(16.0f);
		_ship.setReceiver(_cmds);
		_scn.attach(_ship);
		_actors.attach(_ship);
		_colls.attach(_ship);
		
		_font = (Font)getApplication().getAssets().get(R.raw.badaboom_font, Font.class);
		_fontMat = (Material)getApplication().getAssets().get(R.raw.badaboom_material, Material.class);
		_energyText = new Text();
		_energy = _ship.getEnergy();
		_energyText.setPosition(
			-_cam.getViewWidth() * 0.5f + 5.0f,
			_cam.getViewHeight() * 0.5f,
			-1.0f);
		setEnergy(_energy);
		_scn.attach(_energyText);
		_scoreText = new Text();
		_scoreText.setPosition(
			_cam.getViewWidth() * 0.5f - 5.0f,
			_cam.getViewHeight() * 0.5f,
			-1.0f);
		setScore(_score);
		_scn.attach(_scoreText);
		
		getApplication().getAssets().get(R.raw.bullet_material, Material.class);
		getApplication().getAssets().get(R.raw.rock_material, Material.class);
	}
	
	@Override
	public void onExit()
	{
		_scn.detachAll();
		_actors.detachAll();
		_colls.detachAll();
	}
	
	@Override
	public void onInput(Touches ev)
	{
		_actors.onInput(ev);
	}
	
	@Override
	public void onSensor(XeSense.EventData ev)
	{
		_actors.onSensor(ev);
	}

	@Override
	public void onUpdate()
	{
		getApplication().getSense().setCoordsOrientation(-1);
		
		//float dt = getApplication().getTimer().getDeltaTime() / 1000.0f;
		float dt = 1.0f / 30.0f;
		
		_spawnAccel += dt;
		if(_spawnAccel >= _respawn)
		{
			_spawnAccel = 0.0f;
			Random rand = new Random();
			Material mat = (Material)getApplication().getAssets().get(R.raw.rock_material, Material.class);
			Image img = (Image)getApplication().getAssets().get(R.drawable.rock, Image.class);
			Rock r = new Rock(mat);
			r.setReceiver(_cmds);
			r.setSizeFromImage(img, 0.5f + rand.nextFloat() * 0.5f);
			r.setOffsetFromSize(0.5f, 0.5f);
			r.setSpeed(0.0f, 10.0f + rand.nextFloat() * 30.0f);
			r.setRotation((rand.nextFloat() * 360.0f) - 180.0f);
			float x = (rand.nextFloat() - 0.5f) * _cam.getViewWidth();
			float y = (-_cam.getViewHeight() * 0.5f) - r.getHeight();
			r.setPosition(x, y);
			r.setRange(r.getHeight() * 0.5f);
			_scn.attach(r);
			_actors.attach(r);
			_colls.attach(r);
		}
		_respawn -= dt * 0.01f;
		_respawn = Math.max(_respawn, 0.3f);
		
		_cmds.run();
		_actors.onUpdate(dt);
		_colls.test();
		_stars.setOffset(
			_stars.getOffsetX() - (_ship.getMoveX() * dt * 0.2f),
			_stars.getOffsetY() - (_ship.getMoveY() * dt * 0.1f) - (0.2f * dt)
			);
		_scn.sort(_sorter);
		_scn.update(dt);
	}
	
	public void onCommand(Object sender, String cmd, Object data)
	{
		if(cmd.equals("Energy"))
		{
			_energy = (Float)data;
			setEnergy(_energy);
			if(_energy <= 0.0f)
				getApplication().popState();
		}
		else if(cmd.equals("Rock"))
		{
			setScore(_score++);
		}
		else if(cmd.equals("Miss"))
		{
			setScore(_score--);
		}
	}
	
	private void setEnergy(float e)
	{
		_energyText.build("" + (int)e + "%",
			_font, _fontMat,
			Font.Alignment.LEFT,
			Font.Alignment.BOTTOM,
			0.5f, 0.5f);
	}
	
	private void setScore(int s)
	{
		_scoreText.build("Score: " + (int)s,
			_font, _fontMat,
			Font.Alignment.RIGHT,
			Font.Alignment.BOTTOM,
			0.5f, 0.5f);
	}
}
