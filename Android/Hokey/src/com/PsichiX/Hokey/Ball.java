package com.PsichiX.Hokey;

import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.Framework.Actors.*;
import com.PsichiX.XenonCoreDroid.XeUtils.*;
import com.PsichiX.XenonCoreDroid.XeApplication.*;

public class Ball extends ActorSprite implements ICollidable
{
	private CollisionManager _collMan;
	private float _range = 0.0f;
	private float _spdX = 0.0f;
	private float _spdY = 0.0f;
	private int _score = 0;
	private CommandQueue _rcv;
	
	public Ball(Material mat)
	{
		super(mat);
	}
	
	public void setSpeed(float x, float y)
	{
		_spdX = x;
		_spdY = y;
	}
	
	public float getScore()
	{
		return _score;
	}
	
	public void setReceiver(CommandQueue cmds)
	{
		_rcv = cmds;
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
	
	@Override
	public void onUpdate(float dt)
	{
		Camera2D cam = (Camera2D)getScene().getCamera();
		float w = cam.getViewWidth() * 0.5f;
		float h = cam.getViewHeight() * 0.5f;
		float x = Math.max(-w, Math.min(w, _x + _spdX * dt));
		float y = Math.max(-h, Math.min(h, _y + _spdY * dt));
		if(x <= -w)
			_spdX = Math.abs(_spdX);
		if(x >= w)
			_spdX = -Math.abs(_spdX);
		if(y <= -h)
		{
			_spdY = Math.abs(_spdY);
			if(x > -1.0f && x < 1.0f && _rcv != null)
			{
				_rcv.queueCommand(this, "Hit", new Integer(-1));
				x = y = _spdX = _spdY = 0.0f;
			}
		}
		if(y >= h)
		{
			_spdY = -Math.abs(_spdY);
			if(x > -1.0f && x < 1.0f && _rcv != null)
			{
				_rcv.queueCommand(this, "Hit", new Integer(1));
				x = y = _spdX = _spdY = 0.0f;
			}
		}
		setPosition(x, y);
	}
	
	public void onCollision(ICollidable o)
	{
		if(o instanceof Player)
		{
			Player p = (Player)o;
			float d = MathHelper.vecLength(
				p.getPositionX() - _x,
				p.getPositionY() - _y,
				0.0f
				);
			if(d < p.getRange() + _range)
			{
				_spdX += _x - p.getPositionX();
				_spdY += _y - p.getPositionY();
			}
			//if(_rcv != null)
			//	_rcv.queueCommand(this, "Energy", new Float(_energy));
		}
	}
}
