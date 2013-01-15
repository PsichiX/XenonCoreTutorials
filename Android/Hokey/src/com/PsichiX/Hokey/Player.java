package com.PsichiX.Hokey;

import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.Framework.Actors.*;
import com.PsichiX.XenonCoreDroid.XeUtils.*;
import com.PsichiX.XenonCoreDroid.XeApplication.*;

public class Player extends ActorSprite implements ICollidable
{
	public enum Type
	{
		NONE,
		UP,
		DOWN
	}
	
	private CollisionManager _collMan;
	private float _range = 0.0f;
	private int _score = 0;
	private CommandQueue _rcv;
	private int _grab = -1;
	private Type _type = Type.NONE;
	
	public Player(Material mat, Type t)
	{
		super(mat);
		_type = t;
	}
	
	public void reset()
	{
		_grab = -1;
		Camera2D cam = (Camera2D)getScene().getCamera();
		float h = cam.getViewHeight() * 0.5f;
		float y = 0.0f;
		if(_type == Type.UP)
			y = -h + 0.5f;
		else if(_type == Type.DOWN)
			y = h - 0.5f;
		setPosition(0.0f, y);
	}
	
	public int getScore()
	{
		return _score;
	}
	
	public void addScore(int v)
	{
		_score += v;
	}
	
	public void setReceiver(CommandQueue cmds)
	{
		_rcv = cmds;
	}
	
	@Override
	public void onInput(Touches ev)
	{
		Touch t = ev.getTouchByState(Touch.State.DOWN);
		if(t != null)
		{
			Camera2D cam = (Camera2D)getScene().getCamera();
			float[] loc = cam.convertLocationScreenToWorld(t.getX(), t.getY(), -1.0f);
			if(MathHelper.vecLength(loc[0] - _x, loc[1] - _y, 0.0f) < _range)
				_grab = t.getId();
		}
		t = ev.getTouchByState(Touch.State.UP);
		if(t != null && _grab == t.getId())
			_grab = -1;
		t = ev.getTouch(_grab);
		if(t != null && t.getState() == Touch.State.IDLE)
		{
			Camera2D cam = (Camera2D)getScene().getCamera();
			float[] loc = cam.convertLocationScreenToWorld(t.getX(), t.getY(), -1.0f);
			float w = cam.getViewWidth() * 0.5f;
			float h = cam.getViewHeight() * 0.5f;
			float hu = -h;
			float hd = h;
			if(_type == Type.UP)
				hd = -0.5f;
			else if(_type == Type.DOWN)
				hu = 0.5f;
			setPosition(
				Math.max(-w, Math.min(w, loc[0])),
				Math.max(hu, Math.min(hd, loc[1]))
				);
		}
	}
	
	@Override
	public void onDetach(ActorsManager m)
	{
		super.onDetach(m);
		if(getCollisionManager() != null)
			getCollisionManager().detach(this);
	}
	
	public void onAttach(CollisionManager m)
	{
		_collMan = m;
	}
	
	public void onDetach(CollisionManager m)
	{
		_collMan = null;
	}
	
	public CollisionManager getCollisionManager()
	{
		return _collMan;
	}
	
	public float getRange()
	{
		return _range;
	}
	
	public void setRange(float val)
	{
		_range = val;
	}
	
	public void onCollision(ICollidable o)
	{
	}
}
