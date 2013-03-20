package com.PsichiX.TileMaps;

import com.PsichiX.XenonCoreDroid.XeApplication.State;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Camera2D;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.Scene;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.TileMap;
import com.PsichiX.XenonCoreDroid.Framework.Graphics.TileMapGenerator;

public class GameState extends State
{
	private Camera2D _cam;
	private Scene _scn;
	private TileMapGenerator _mapGen;
	private TileMap _map;
	
	@Override
	public void onEnter()
	{
		_scn = (Scene)getApplication().getAssets().get(R.raw.scene, Scene.class);
		_cam = (Camera2D)_scn.getCamera();
		_cam.setViewPosition(_cam.getViewWidth() * 0.5f, _cam.getViewHeight() * 0.5f);
		
		// create tilemap object
		_map = new TileMap();
		// load map generator
		_mapGen = (TileMapGenerator)getApplication().getAssets().get(R.raw.map, TileMapGenerator.class);
		// apply patterns for map
		_mapGen.applyPatterns("map");
		// build tilemap compatible with map generator
		_mapGen.buildCompatibleTileMap("map", _map, _cam.getViewWidth(), _cam.getViewHeight(), 1.0f, 1.0f);
		// apply map generator tiles to tilemap 
		_mapGen.applyTiles("map", _map);
		_scn.attach(_map);
	}
	
	@Override
	public void onExit()
	{
		_scn.releaseAll();
	}
	
	@Override
	public void onUpdate()
	{
		float dt = getApplication().getTimer().getDeltaTime() *0.001f;
		_scn.update(dt);
	}
}
