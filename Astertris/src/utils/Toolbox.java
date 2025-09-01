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
	
	private int clamp(int value) {
	    return Math.max(0, Math.min(255, value));
	}
	
	// weightage - 0 (color1), 1 (color2)
	public Color blendColors(Color color1, Color color2, double weightage) {
		weightage = Math.max(0.0, Math.min(1.0, weightage));
		
		double r1 = color1.getRed(),g1 = color1.getGreen(), b1 = color1.getBlue();
		double r2 = color2.getRed(),g2 = color2.getGreen(), b2 = color2.getBlue();
		
//		System.out.println(r1 + " " + g1 + " " + b1);
//		System.out.println(r2 + " " + g2 + " " + b2);
		
		int r = clamp((int)Math.round(r1*(1-weightage) + r2*weightage));
		int g = clamp((int)Math.round(g1*(1-weightage) + g2*weightage));
		int b = clamp((int)Math.round(b1*(1-weightage) + b2*weightage));
//		System.out.println(r + " " + g + " " + b);
//		System.out.println(r1*(1-weightage) + r2*weightage + " " + g1*(1-weightage) + g2*weightage + " " + b1*(1-weightage) + b2*weightage);
		return new Color(r,g,b);
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
