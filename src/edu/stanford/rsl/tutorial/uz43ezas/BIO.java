package edu.stanford.rsl.tutorial.uz43ezas;

import ij.ImageJ;
import edu.stanford.rsl.conrad.data.numeric.Grid2D;

public class BIO extends Grid2D {

	public BIO(int height, int width) {
		super(height, width);
		//this.geometricObjects();
		this.circlefilled(250,250,200,2);
		//this.circle(250,250,300,3);
		this.ellipse(150,170,25,2,1,1);
		this.ellipse(350,170,25,2,1,1);
		
		this.ellipse(250,350,25,5,1,1);
	}
	
	public void square (int x, int y, int w, int val) {
		for (int i = x; i <=(w+x); i++) {
			for (int j = y; j<(w+y); j++) {
				this.setAtIndex(x, y, val);
			}
		}
	}
	
	public void ellipse(int x, int y, int radius, int a, int b, int val) {
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j<this.getWidth(); j++) {
				int wert = (i-x)*(i-x)/(a*a) + (j-y)*(j-y)/(b*b);
				if (wert <= (radius*radius)) {
					this.setAtIndex(i, j, val);
				}
			}
		}
	}
	
	
	public void circlefilled (int x, int y, int radius, int val) {
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j<this.getWidth(); j++) {
				int wert = (i-x)*(i-x) + (j-y)*(j-y);
				if (wert <= (radius*radius)) {
					this.setAtIndex(i, j, val);
				}
			}
		}
	}
	
	public void circle (int x, int y, int radius, int val) {
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j<this.getWidth(); j++) {
				int wert = (i-x)*(i-x) + (j-y)*(j-y);
				if (wert == (radius*radius)) {
					this.setAtIndex(i, j, val);
				}
			}
		}
	}
	
	public void geometricObjects(){
		
		for (int i = 10; i<=240; i++) {
			for (int j = 50; j<=150; j++) {
				this.setAtIndex(i, j, 1);
			}
		}
		for (int i = 260; i<=490; i++) {
			for (int j = 50; j<=150; j++) {
				this.setAtIndex(i, j, 5);
			}
		}
		
		for (int i = 10; i<=490; i++) {
			for (int j = 400; j<=450; j++) {
				this.setAtIndex(i, j, 10);
			}
		}
	}
	
	public static void main(String[] args) {
		new ImageJ();
		System.out.println("BIO");
		BIO object = new BIO(512,512);
		object.show();
	}
}
