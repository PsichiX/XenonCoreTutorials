package com.PsichiX.HelloWorld;

import com.PsichiX.XenonCoreDroid.XeApplication.*;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;

public class MenuState extends State
{
	private Scene _scn;
	private Stars _stars;
	private Text _title;
	private Text _play;
	
	@Override
	public void onEnter()
	{
		_scn = (Scene)getApplication().getAssets().get(R.raw.scene, Scene.class);
		
		Material mat;
		Font font;
		
		mat = (Material)getApplication().getAssets().get(R.raw.stars_material, Material.class);
		_stars = new Stars(mat,
			getApplication().getPhoton().getRenderer().getScreenWidth() *
			getApplication().getPhoton().getRenderer().getScreenHeight() /
			1000
			);
		_stars.randomize(-1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 3.0f, 1.0f, 1.5f);
		_scn.attach(_stars);
		
		font = (Font)getApplication().getAssets().get(R.raw.badaboom_font, Font.class);
		mat = (Material)getApplication().getAssets().get(R.raw.badaboom_material, Material.class);
		
		_title = new Text();
		_title.build("Xenon Core Droid\nGame tutorial",
			font, mat,
			Font.Alignment.CENTER,
			Font.Alignment.MIDDLE,
			1.0f, 1.0f
			);
		_title.setPosition(0.0f, -50.0f);
		_scn.attach(_title);
		
		_play = new Text();
		_play.build("Tap to Play",
			font, mat,
			Font.Alignment.CENTER,
			Font.Alignment.MIDDLE,
			1.0f, 1.0f
			);
		_play.setPosition(0.0f, 50.0f);
		_scn.attach(_play);
		
		//getApplication().getPhoton().getRenderer().setClearBackground(true, 1.0f, 0.0f, 0.0f, 1.0f);
	}
	
	@Override
	public void onExit()
	{
		_scn.releaseAll();
	}
	
	@Override
	public void onInput(Touches ev)
	{
		getApplication().pushState(new GameState());
	}
	
	@Override
	public void onUpdate()
	{
		float dt = getApplication().getTimer().getDeltaTime() / 1000.0f;
		//float dt = 1.0f / 30.0f;
		
		_stars.setOffset(
			_stars.getOffsetX(),
			_stars.getOffsetY() - (0.1f * dt)
			);
		_scn.update(dt);
	}
}
