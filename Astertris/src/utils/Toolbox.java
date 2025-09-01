package utils;

import java.awt.Color;

public class Toolbox {
	
//	COLOR METHODS
	public Color getColorFromRGB(int r, int g, int b) {
		return new Color(r,g,b);
	}
	
	private float hueToRGB(float p, float q, float t) {
		if (t<0) t++;
		if (t>1) t--;
		if (t<1f/6f) return p+(q-p)*6*t;
		if (t<1f/2f) return q;
		if (t<2f/3f) return p+(q-p)*(2f/3f - t) * 6;
		return p;
	}
	
	private int clamp(float val) {
		return Math.min(255, Math.max(0, Math.round(val*255)));
	}
	
	public Color getColorFromHSL(int h, int s, int l) {
		float sat = s/100f;
		float light = l/100f;
		float hue = h%360;
		hue /= 360f;
		
		float r, g, b;
		
		if (sat==0) r=g=b=light;
		else {
			float q = light<0.5f 
					? light * (1+sat)
				    : light + sat - light*sat;
			float p = 2*light-q;
			
			r = hueToRGB(p,q,hue+1f/3f);
			g = hueToRGB(p,q,hue);
			b = hueToRGB(p,q,hue-1f/3f);
		}
		return new Color(clamp(r),clamp(g),clamp(b));
	}
	
}
