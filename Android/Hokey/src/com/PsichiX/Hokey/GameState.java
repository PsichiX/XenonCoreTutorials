package com.PsichiX.Hokey;

import com.PsichiX.XenonCoreDroid.XeApplication.*;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.Framework.Actors.*;
import com.PsichiX.XenonCoreDroid.XeUtils.*;

public class GameState extends State implements CommandQueue.Delegate
{
	private Camera2D _cam;
	private Scene _scn;
	private ActorsManager _actors = new ActorsManager(this);
	private CollisionManager _colls = new CollisionManager();
	private CommandQueue _cmds = new CommandQueue();
	private Player[] _players = new Player[2];
	private Ball _ball;
	private Font _font;
	private Material _fontMat;
	private Text _scoreText;
	
	@Override
	public void onEnter()
	{
		_cmds.setDelegate(this);
		
		_scn = (Scene)getApplication().getAssets().get(R.raw.scene, Scene.class);
		_cam = (Camera2D)_scn.getCamera();
		
		Material mat;
		Player pl;
		Sprite t;
		
		mat = (Material)getApplication().getAssets().get(R.raw.box_material, Material.class);
		
		t = new Sprite(mat);
		t.setSize(_cam.getViewWidth(), 0.025f);
		t.setOffsetFromSize(0.5f, 0.5f);
		_scn.attach(t);
		
		t = new Sprite(mat);
		t.setPosition(0.0f, _cam.getViewHeight() * 0.5f);
		t.setSize(2.0f, 0.1f);
		t.setOffsetFromSize(0.5f, 0.5f);
		_scn.attach(t);
		
		t = new Sprite(mat);
		t.setPosition(0.0f, -_cam.getViewHeight() * 0.5f);
		t.setSize(2.0f, 0.1f);
		t.setOffsetFromSize(0.5f, 0.5f);
		_scn.attach(t);
		
		mat = (Material)getApplication().getAssets().get(R.raw.ball_material, Material.class);
		
		pl = new Player(mat, Player.Type.DOWN);
		pl.setSize(1.0f, 1.0f);
		pl.setOffsetFromSize(0.5f, 0.5f);
		pl.setTextureOffset(-0.5f, -0.5f);
		pl.setRange(0.5f);
		pl.setReceiver(_cmds);
		_scn.attach(pl);
		_actors.attach(pl);
		_colls.attach(pl);
		pl.reset();
		_players[0] = pl;
		
		pl = new Player(mat, Player.Type.UP);
		pl.setSize(1.0f, 1.0f);
		pl.setOffsetFromSize(0.5f, 0.5f);
		pl.setTextureOffset(-0.5f, -0.5f);
		pl.setRange(0.5f);
		pl.setReceiver(_cmds);
		_scn.attach(pl);
		_actors.attach(pl);
		_colls.attach(pl);
		pl.reset();
		_players[1] = pl;
		
		Ball b = new Ball(mat);
		b.setSize(0.5f, 0.5f);
		b.setOffsetFromSize(0.5f, 0.5f);
		b.setTextureOffset(-0.5f, -0.5f);
		b.setRange(0.25f);
		b.setReceiver(_cmds);
		_scn.attach(b);
		_actors.attach(b);
		_colls.attach(b);
		_ball = b;
		
		_font = (Font)getApplication().getAssets().get(R.raw.badaboom_font, Font.class);
		_fontMat = (Material)getApplication().getAssets().get(R.raw.badaboom_material, Material.class);
		
		_scoreText = new Text();
		_scoreText.setPosition(
			_cam.getViewWidth() * 0.5f,
			0.0f,
			-1.0f);
		_scoreText.setAngle(90.0f);
		setScore(0, 0);
		_scn.attach(_scoreText);
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
	public void onUpdate()
	{
		//float dt = getApplication().getTimer().getDeltaTime() / 1000.0f;
		float dt = 1.0f / 30.0f;
		
		_cmds.run();
		_actors.onUpdate(dt);
		_colls.test();
		_scn.update(dt);
	}
	
	public void onCommand(Object sender, String cmd, Object data)
	{
		if(cmd.equals("Hit"))
		{
			Integer val = (Integer)data;
			if(val == 1)
				_players[1].addScore(1);
			if(val == -1)
				_players[0].addScore(1);
			_players[0].reset();
			_players[1].reset();
			setScore(_players[0].getScore(), _players[1].getScore());
		}
	}
	
	private void setScore(int sd, int su)
	{
		_scoreText.build("" + su + " : " + sd,
			_font, _fontMat,
			Font.Alignment.CENTER,
			Font.Alignment.TOP,
			0.01f, 0.01f);
	}
}
