package com.PsichiX.HelloWorld;

import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.Framework.Actors.*;
import com.PsichiX.XenonCoreDroid.XeUtils.*;

public class Rock extends ActorSprite implements ICollidable
{
	private CollisionManager _collMan;
	private float _range = 0.0f;
	private float _spdX = 0.0f;
	private float _spdY = 0.0f;
	private float _rot = 0.0f;
	private CommandQueue _rcv;
	
	public Rock(Material mat)
	{
		super(mat);
	}
	
	public void setSpeed(float x, float y)
	{
		_spdX = x;
		_spdY = y;
	}
	
	public void setReceiver(CommandQueue cmds)
	{
		_rcv = cmds;
	}
	
	public void setRotation(float r)
	{
		_rot = r;
	}
	
	@Override
	public void onUpdate(float dt)
	{
		Camera2D cam = (Camera2D)getScene().getCamera();
		setAngle(_alpha + _rot * dt);
		float h = cam.getViewHeight() * 0.5f + getHeight();
		setPosition(
			_x + _spdX * dt,
			_y + _spdY * dt
			);
		if(_y > h)
		{
			getManager().detach(this);
			if(_rcv != null)
				_rcv.queueCommand(this, "Miss", null);
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
