package edu.stanford.rsl.tutorial.uz43ezas;

import ij.ImageJ;

import java.util.ArrayList;

import edu.stanford.rsl.conrad.data.numeric.Grid2D;
import edu.stanford.rsl.conrad.data.numeric.InterpolationOperators;
import edu.stanford.rsl.conrad.data.numeric.NumericGrid;
import edu.stanford.rsl.conrad.data.numeric.NumericPointwiseOperators;
import edu.stanford.rsl.conrad.geometry.shapes.simple.Box;
import edu.stanford.rsl.conrad.geometry.shapes.simple.PointND;
import edu.stanford.rsl.conrad.geometry.shapes.simple.StraightLine;
import edu.stanford.rsl.conrad.numerics.SimpleVector;

public class ParallelBeam {
	
	public static Grid2D sinogram (Grid2D grid, double maxTheta, double deltaTheta, double maxS, double deltaS) {
		
		int maxSsteps = (int)((maxS/deltaS) +1); //Anzahl der Rays
		int maxThetasteps = (int)((maxTheta/deltaTheta) +1); //Anzahl der Aufnahmen (aus wie vielen verschiedene Winkel)
		
		// Sinogram erstellen, soll selbe Groesse haben wie Bild und auch dasselbe spacing
		// buffer (Area), width, height
		Grid2D sino = new Grid2D(new float[maxSsteps*maxThetasteps], maxSsteps, maxThetasteps);
		sino.setSpacing(deltaS, deltaTheta);
		
		// Box erstellen mit derselben Groesse wie Bild
		Box box = new Box(grid.getSize()[0]*grid.getSpacing()[0], grid.getSize()[1]*grid.getSpacing()[1], 2);

		
		// Erst ueber alle Winkel laufen, d.h. man benoetigt die Anzahl der Schritte
		for(int i = 0; i<maxThetasteps; i++) {
			// Tatsaechlichen Winkel berechnen
			double theta = deltaTheta*i;
			double cosTheta = Math.cos(theta*i);
			double sinTheta = Math.sin(theta*i);
			
			//fuer jeden Winkel werden alle Werte berechnet, fuer jeden Punkt auf S
			for (int j = 0; j<maxSsteps; j++){
				// Laenge s berechnen, d.h. Punkt durch Koordinatenachse s an dem wir uns befinden -> jetzt benoetigt man alle Punkte die auf der Geraden senkrecht zu/durch diesen Punkt verlaufen
				double s = i*deltaS - maxS/2;
				
				//Punkt an dem sich Koordinatenachse s und Senkrechte schneiden
				// Man benoetigt danach Linie, und diese wird nur mit PointND gesetzt
				PointND p1 = new PointND(s*cosTheta, s*sinTheta);
				// p2: Setzt die Richtung des Punktes p2, senkrecht zu s
				PointND p2 = new PointND((cosTheta*s - sinTheta), (sinTheta*s-cosTheta));
				
				//Linie durch beide Punkte 
				StraightLine line = new StraightLine(p1, p2);
				
				// Linie muss Box schneiden, um Anfangs- und Endpunkt zu erhalten, und es sollen alle Punkte der Geraden in einer ArrayList gespeichert werden
				ArrayList<PointND> list = box.intersect(line);
				
				PointND startpoint = list.get(0);
				PointND endpoint = list.get(list.size()-1);
				
				// Distanz von Anfangs und Endpunkt wird benoetigt, um ueber eine Schleife zu laufen und alle Werte der Punkte in Arraylist aufzuaddieren -> Wert in Sinogram
				// Abstractvector benoetigt um mit ihnen rechnen zu koennen
				
				SimpleVector start = new SimpleVector(startpoint.getAbstractVector());
				SimpleVector end = new SimpleVector(endpoint.getAbstractVector());
				// Benoetige Steigung, bzw. Vektor der in die Richtung der Geraden zeigt, um vom Startpunkt aus in die richtige Richtung zu laufen und somit Punkt zu finden
				// Benoetige auch richtige Schrittweite
				end.subtract(start);
				
				// koennen auch negativ sein -> L2 Norm, damit wir positive Distanz erhalten
				double distance = end.normL2();
				// Schrittweite berechnen, mit der auf Linie gelaufen wird (Schritt zum naechsten Pixel)
				end.divideBy(distance);
				
				// initialisiere Wert der aufsummierten Punktewerte auf Linie
				double sum = 0;
				// Integral berechnen
				for (int k = 0; k < distance; k++) {
					// Anfangspunkt
					PointND begin = new PointND(startpoint);
					// Laufe zu meinem Punkt, in oben berechneter Richtung, abhaengig von k
					begin.getAbstractVector().add(end.multipliedBy(k));
					
					// Brauche nun Value an diesem Punkt, hierfuer muss Wert an x- und y-Koordinate berechnet werden (spacing beachten)
					double x = begin.get(0)/grid.getSpacing()[0];
					double y = begin.get(1)/grid.getSpacing()[1];
					
					// Interpolation, da Ort nicht immer direkt auf einen Pixel faellt, d.h. nicht auf einen genauen Wert
					
					double value = InterpolationOperators.interpolateLinear(grid, x, y);
					// aufsummieren aller Werte ueber ganze Linie
					sum = sum + value;
					
				}
				sino.setAtIndex(j, i, (float)sum);
			}
		}
		
		return sino;
	}

	public static void main(String[] args) {
		new ImageJ();
		BIO object = new BIO(512,512);
		Grid2D sino = sinogram(object,180,1, 20,1);
		sino.show();
	}
	
}
