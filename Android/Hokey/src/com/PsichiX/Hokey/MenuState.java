package com.PsichiX.Hokey;

import com.PsichiX.XenonCoreDroid.XeApplication.*;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.Framework.Actors.*;
import com.PsichiX.XenonCoreDroid.XeUtils.*;

public class MenuState extends State
{
	private Camera2D _cam;
	private Scene _scn;
	private Font _font;
	private Material _fontMat;
	private Text _title;
	
	@Override
	public void onEnter()
	{
		_scn = (Scene)getApplication().getAssets().get(R.raw.scene, Scene.class);
		_cam = (Camera2D)_scn.getCamera();
		
		_font = (Font)getApplication().getAssets().get(R.raw.badaboom_font, Font.class);
		_fontMat = (Material)getApplication().getAssets().get(R.raw.badaboom_material, Material.class);
		
		_title = new Text();
		_title.build("Black & White\nHokey\n\nTap to play!",
			_font, _fontMat,
			Font.Alignment.CENTER,
			Font.Alignment.MIDDLE,
			0.02f, 0.02f);
		_scn.attach(_title);
	}
	
	@Override
	public void onExit()
	{
		_scn.detachAll();
	}
	
	@Override
	public void onInput(Touches ev)
	{
		getApplication().pushState(new GameState());
	}
	
	@Override
	public void onUpdate()
	{
		//float dt = getApplication().getTimer().getDeltaTime() / 1000.0f;
		float dt = 1.0f / 30.0f;
		
		_scn.update(dt);
	}
}
