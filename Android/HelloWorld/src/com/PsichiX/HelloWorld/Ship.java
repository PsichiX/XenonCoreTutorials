package com.PsichiX.HelloWorld;

import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.Framework.Actors.*;
import com.PsichiX.XenonCoreDroid.XeUtils.*;
import com.PsichiX.XenonCoreDroid.XeSense;
import com.PsichiX.XenonCoreDroid.XeApplication.*;

public class Ship extends ActorSprite implements ICollidable
{
	private CollisionManager _collMan;
	private float _range = 0.0f;
	private float _movX = 0.0f;
	private float _movY = 0.0f;
	private float _spdX = 100.0f;
	private float _spdY = 100.0f;
	private float _energy = 100.0f;
	private CommandQueue _rcv;
	
	public Ship(Material mat)
	{
		super(mat);
	}
	
	public void setSpeed(float x, float y)
	{
		_spdX = x;
		_spdY = y;
	}
	
	public float getMoveX()
	{
		return _movX;
	}
	
	public float getMoveY()
	{
		return _movY;
	}
	
	public float getEnergy()
	{
		return _energy;
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
			Math.max(-w, Math.min(w, _x + _movX * _spdX * dt)),
			Math.max(-h, Math.min(h, _y + _movY * _spdY * dt))
			);
	}
	
	@Override
	public void onInput(Touches ev)
	{
		Touch t = ev.getTouchByState(Touch.State.DOWN);
		if(t != null)
		{
			Material mat = (Material)MainActivity.app.getAssets().get(R.raw.bullet_material, Material.class);
			Image img = (Image)MainActivity.app.getAssets().get(R.drawable.bullet, Image.class);
			Bullet b = new Bullet(mat);
			b.setSizeFromImage(img, 0.2f);
			b.setOffsetFromSize(0.5f, 0.5f);
			b.setPosition(_x, _y);
			b.setSpeed(0.0f, -100.0f);
			b.setRange(b.getHeight() * 0.5f);
			b.setReceiver(_rcv);
			getScene().attach(b);
			getManager().attach(b);
			getCollisionManager().attach(b);
		}
	}
	
	@Override
	public void onSensor(XeSense.EventData ev)
	{
		if(ev.type == XeSense.Type.GRAVITY)
		{
			float[] v = MathHelper.vecNormalize(ev.values[0], ev.values[1], ev.values[2]);
			ev.owner.remapCoords(v, 1, 0);
			_movX = Math.max(-0.2f, Math.min(0.2f, v[0])) * 5.0f;
			_movY = Math.max(-0.2f, Math.min(0.2f, v[1])) * 5.0f;
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
		if(o instanceof Rock)
		{
			Rock r = (Rock)o;
			if(r.getManager() != null)
				r.getManager().detach(r);
			_energy -= 25.0f;
			if(_rcv != null)
				_rcv.queueCommand(this, "Energy", new Float(_energy));
		}
	}
}
