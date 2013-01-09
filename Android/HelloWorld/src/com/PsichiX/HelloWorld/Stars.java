package com.PsichiX.HelloWorld;

import com.PsichiX.XenonCoreDroid.Framework.Graphics.*;
import com.PsichiX.XenonCoreDroid.XePhoton;
import java.util.Random;

public class Stars extends Shape
{
	private int _count = 0;
	private float[] _off = new float[2];
	
	public Stars(Material material, int count)
	{
		super();
		_count = count;
		float[] vd = new float[4 * count];
		XePhoton.VertexArray va = Graphics.renderer().createVertexArray(4, count, vd);
		setVertexArray("vPosition", va);
		setMaterial(material);
		setDrawMode(XePhoton.DrawMode.POINTS);
	}
	
	public void setOffset(float x, float y)
	{
		_off[0] = x;
		_off[1] = y;
		getMaterial().setPropertyVec("uOffset", _off);
	}
	
	public float getOffsetX()
	{
		return _off[0];
	}
	
	public float getOffsetY()
	{
		return _off[1];
	}
	
	public void randomize(
		float xmin, float xmax,
		float ymin, float ymax,
		float zmin, float zmax,
		float smin, float smax)
	{
		Random r = new Random();
		int i = 0;
		float[] vd = new float[4 * _count];
		while(i < _count * 4)
		{
			vd[i++] = ((xmax - xmin) * r.nextFloat()) + xmin;
			vd[i++] = ((ymax - ymin) * r.nextFloat()) + ymin;
			vd[i++] = ((zmax - zmin) * r.nextFloat()) + zmin;
			vd[i++] = ((smax - smin) * r.nextFloat()) + smin;
		}
		getVertexArray("vPosition").update(vd, 0);
	}
}
