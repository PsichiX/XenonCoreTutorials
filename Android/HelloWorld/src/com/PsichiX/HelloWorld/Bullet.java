package com.PsichiX.HelloWorld;

import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.Framework.Actors.*;
import com.PsichiX.XenonCoreDroid.XeUtils.*;

public class Bullet extends ActorSprite implements ICollidable
{
	private CollisionManager _collMan;
	private float _range = 0.0f;
	private float _spdX = 0.0f;
	private float _spdY = 0.0f;
	private CommandQueue _rcv;
	
	public Bullet(Material mat)
	{
		super(mat);
	}
	
	public void setSpeed(float x, float y)
	{
		_spdX = x;
		_spdY = y;
		if(y != 0.0f && x != 0.0f)
			setAngle((float)Math.toDegrees(Math.atan2(y, x)));
	}
	
	public void setReceiver(CommandQueue cmds)
	{
		_rcv = cmds;
	}
	
	@Override
	public void onUpdate(float dt)
	{
		Camera2D cam = (Camera2D)getScene().getCamera();
		float w = cam.getViewWidth() * 0.5f;
		float h = cam.getViewHeight() * 0.5f;
		setPosition(
			_x + _spdX * dt,
			_y + _spdY * dt
			);
		if(_x < -w || _x > w || _y < -h || _y > h)
			getManager().detach(this);
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
		if(o instanceof Rock)
		{
			Rock r = (Rock)o;
			if(r.getManager() != null)
				r.getManager().detach(r);
			if(getManager() != null)
				getManager().detach(this);
			if(_rcv != null)
				_rcv.queueCommand(this, "Rock", null);
		}
	}
}
