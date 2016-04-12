package edu.stanford.rsl.tutorial.uz43ezas;

import edu.stanford.rsl.conrad.data.numeric.Grid2D;

public class BIO extends Grid2D {

	public BIO(int height, int width) {
		super(height, width);

	}
	
	public static void main(String[] args) {
		System.out.println("BIO");
		BIO object = new BIO(5,5);
		object.show();
	}
}
