import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

import jdk.internal.dynalink.beans.StaticClass;

import org.omg.CORBA.PRIVATE_MEMBER;


public class StlReader {
	public static int polygonCounter = 0;
	//public static Hashtable<Integer,Polygon> polygons = new Hashtable<>();
	public static ArrayList<Polygon> polygons = new ArrayList<>();
	public static Hashtable<Edge,ArrayList<Integer>> edgeTable = new Hashtable<>();
	public static Hashtable<Polygon, Integer> groupTable = new Hashtable<>(); 
	/**.
	 * each vertex corresponds to a list of polygons
	 */
	public static Hashtable<Vertex, ArrayList<Integer>> vertexTable = new Hashtable<>();
	public static ArrayList<Vertex> vertexList;
	/**.
	 * store all the vertex in one set
	 */
	public static Hashtable<Vertex,Integer> vertexIndexTable = new Hashtable<>();
	/**.
	 * average geodistance of all polygons
	 */
	public static double avgGeod;
	/**.
	 * average angular distance of all polygons
	 */
	public static double avgAngd;
	/**.
	 * k-way value
	 */
	public static final int K_WAY = 2;
	/**.
	 * representative array of patches' face index
	 */
	public static int[] patchIndex = new int[K_WAY];
	/**.
	 * shortest path table from Dijkstra
	 */
	public static HashMap<Edge,Double> pathMap;
	/**.
	 * 
	 * @param fileName
	 */
	public static void readFile(String fileName) {
		read(fileName);
	}
	public static void read(String args) {
		//String a ="0";
		//System.out.println(Double.parseDouble(a));
		Scanner scan;
		try {
			scan = new Scanner(new File(args));
			String line;
			Polygon p;
			Vertex normal;
			Vertex[] point = new Vertex[3];
			while (scan.hasNext()) {
				line = scan.nextLine();
				// one polygon
				if (line.contains("facet normal")) { // 1st line
					String[] words = line.trim().split("\\s+");
					normal = new Vertex(Double.parseDouble(words[2]),Double.parseDouble(words[3]),
							Double.parseDouble(words[4]));
					// store normal
					//System.out.println("normal:+"+line);
					int i = 0;
					line = scan.nextLine();
					while(!line.contains("end facet")&&!line.contains("endfacet")) {
						line = scan.nextLine();
						// store vertex
						if (line.contains("vertex")) {
							words = line.trim().split("\\s+");
							point[i] = new Vertex(Double.parseDouble(words[1]),Double.parseDouble(words[2]),
									Double.parseDouble(words[3]));
							//System.out.println("vertex"+i+":"+point[i]);
							i++;
						}
					}
					p = new Polygon(point,normal);
					polygons.add(p);
					// put vertex into table
					for (i =0; i < 3; i++) {
						if (vertexTable.containsKey(point[i])) {
							ArrayList<Integer> list = vertexTable.get(point[i]);
							list.add(polygonCounter);
							vertexTable.put(point[i], list);
						} else {
							ArrayList<Integer> list = new ArrayList<Integer>();
							list.add(polygonCounter);
							vertexTable.put(point[i], list);
						}
					}
					for (Edge e : p.getEdges()) {
						if (edgeTable.containsKey(e)) {
							ArrayList<Integer> list = edgeTable.get(e);
							list.add(polygonCounter);
							edgeTable.put(e, list);
						} else {
							ArrayList<Integer> list = new ArrayList<Integer>();
							list.add(polygonCounter);
							edgeTable.put(e, list);
						}
					}
					polygonCounter++; // start from 0
				}
			}
			vertexList = new ArrayList(vertexTable.keySet());
			int value = 0;
			for (Vertex v : vertexList) {
				vertexIndexTable.put(v, value);
				value++;
			}
			
			pathMap = Dijkstra.shortestDistances(polygons, edgeTable);
			//System.out.println(polygonCounter);
			//Vertex v = new Vertex(0,0, 1);
			//	Vertex v2 = new Vertex(0.05, 0, 0.99875);
			//Edge e = new Edge(v,v2);
			//			Edge e = new 
			//			for(int i:vertexTable.get(v)) {
			//			    System.out.println(polygons.get(i));
			//			}
			//			for(int i :edgeTable.get(e)) {
			//			    System.out.println(i);
			//			    System.out.println(polygons.get(i));
			//			}
			//			System.out.println(edgeTable.get(e));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void updatePatchIndex() {
		
		for (int i=0; i < K_WAY; i++) {
			patchIndex[i] = -1;
		}
		double miniDist = Double.POSITIVE_INFINITY;
		double maxDist = 0;
		int miniIndex = 0;
		int maxIndex = 0;
		double dist;
		int index;
		for (int i=0; i < K_WAY; i++) {
			miniDist = Double.POSITIVE_INFINITY;
			maxDist = 0;
			miniIndex = 0;
			maxIndex = 0;
			if (i == 0) {
				for (int j = 0; j < polygonCounter; j++) {
					Polygon currPolygon = polygons.get(j);
					dist = 0;
					index = j;
					for (int k = 0; k < polygonCounter; k++) {
						if (k != j) {
							// sum up all dist
							Edge e = new Edge(currPolygon.getCom(), polygons.get(k).getCom());
							dist+=pathMap.get(e);
						}
					}
					if (dist < miniDist) {
						miniDist = dist;
						miniIndex = index;
					}
				}
				patchIndex[i] = miniIndex;
			} else {
				for (int j = 0; j < polygonCounter; j++) {
					dist = 0;
					index = j;
					Polygon currPolygon = polygons.get(j);
					for (int k=0;k<i;k++) {
						if (j != patchIndex[k]) {
							Edge e = new Edge(currPolygon.getCom(), polygons.get(patchIndex[k]).getCom());
							//System.out.println(e);
							dist+=pathMap.get(e);
							//System.out.println(pathMap.get(e));
						}
					}
					if (dist > maxDist) {
						maxDist = dist;
						maxIndex = index;
					}
				}
				patchIndex[i] = maxIndex;
			}
		}
		System.out.println("patch index are:"+Arrays.toString(patchIndex));
	}
	public static void avgDistCalculation() {
		double totalGeoDistance = 0;
		double totalAngDistance = 0;
		double eta = 1.0;
		for (int i = 0; i < polygonCounter; i++) {
			Polygon currPolygon = polygons.get(i);
			for (Edge e : currPolygon.getEdges()) {
				ArrayList<Integer> shareEdgeList = edgeTable.get(e);
				if (shareEdgeList.size() != 2) {
					System.out.println("incorrect shared edge "+shareEdgeList.size());
					continue;
				}
				Polygon nearPolygon = polygons.get(shareEdgeList.get(0));
				if (!nearPolygon.equals(currPolygon)) {
				} else {
					nearPolygon = polygons.get(shareEdgeList.get(1));
				}
				totalGeoDistance+=currPolygon.geoDistance(nearPolygon);
				// if convex, then eta should be less
				if (!currPolygon.isConcave(nearPolygon)) {
					eta = 0.5;
				}
				totalAngDistance+=currPolygon.angDistance(nearPolygon, eta);
			}
		}
		avgGeod = totalGeoDistance/(2*polygonCounter);
		avgAngd = totalAngDistance/(2*polygonCounter);
		System.out.println("average geometric distance is :" + avgGeod);
		System.out.println("average angular distance is :" + avgAngd);
	}
	public static void bfs(double threshold) {
		int groupCounter = -1;
		System.out.println("total polygon number: "+polygonCounter);
		groupTable = new Hashtable<Polygon, Integer>();
		for (int i = 0; i < polygonCounter; i++) {
			int ptr=0;
			// check first whether included in a group
			if (groupTable.containsKey(polygons.get(i))) {
				continue;
			}
			groupCounter++;
			Polygon startP = polygons.get(i);
			//System.out.println("grouping for "+i+"st polygon");
			LinkedList<Polygon> todoList = new LinkedList<>();
			todoList.add(startP);
			groupTable.put(startP, groupCounter);
			while (todoList.size()>ptr) {
				Polygon currPolygon = todoList.get(ptr);
				if (!groupTable.containsKey(currPolygon)) {
					groupTable.put(currPolygon, groupCounter);
				}
				for (Edge e : currPolygon.getEdges()) {
					ArrayList<Integer> shareEdgeList = edgeTable.get(e);
					if (shareEdgeList.size() != 2) {
						System.out.println("incorrect shared edge "+shareEdgeList.size());
						continue;
					}
					Polygon nearPolygon = polygons.get(shareEdgeList.get(0));
					//System.out.println("before");
					if (!nearPolygon.equals(currPolygon)) {
						if(getAngle(currPolygon, nearPolygon) < threshold) {
							if (!groupTable.containsKey(nearPolygon)) {	
								todoList.add(nearPolygon);
								groupTable.put(nearPolygon, groupCounter);
							}
						} else {
							//System.out.println("line 149 impossible");
							//System.out.println(getAngle(currPolygon, nearPolygon));
						}
						//polygons.get(shareEdgeList.get(0));
						//System.out.println("line 144 one diff");
					} else {
						nearPolygon = polygons.get(shareEdgeList.get(1));
						if (!nearPolygon.equals(currPolygon)) {
							if (getAngle(currPolygon, nearPolygon) < threshold) {
								//							System.out.println("contains");
								if (!groupTable.containsKey(nearPolygon)) {
									todoList.add(nearPolygon);
									groupTable.put(nearPolygon, groupCounter);
								}
							} else {
								//System.out.println("line 163 impossible");
								//System.out.println(getAngle(currPolygon, nearPolygon));
							}
							//todoList.add(polygons.get(shareEdgeList.get(1)));
							//System.out.println("line 156 one diff");
						} else {
							System.out.println("line 168 impossible");
						}
					}
					//System.out.println("after");
				}
				//System.out.println("prev size"+todoList.size());
				//todoList.remove();
				ptr++;
				//System.out.println(ptr);
				//System.out.println(todoList.size());
			}
			//System.out.println("group number: "+(groupCounter+1));
		}
		System.out.println("group number: "+(groupCounter+1));
	}
	private static double getModule(Vertex v) {
		return Math.sqrt(Math.pow(v.getX(), 2) + Math.pow(v.getY(), 2) + Math.pow(v.getZ(), 2));
	}
	/**.
	 * return radians
	 * @param p1
	 * @param p2
	 * @return
	 */
	private static double getAngle(Polygon p1, Polygon p2) {
		Vertex n1 = p1.getNormal();
		Vertex n2 = p2.getNormal();
		double m1 = getModule(n1);
		double m2 = getModule(n2);
		double numerator = n1.getX()*n2.getX()+n1.getY()*n2.getY()+n1.getZ()*n2.getZ(); 
		double epsilon = Math.pow(10, -5);
		if (m1*m2 == 0.0 || Math.abs(numerator/(m1*m2)) < epsilon) {
			//System.out.println(Math.toDegrees(Math.PI/2));
			return Math.PI/2;
		}
		if (Math.abs(numerator/(m1*m2)) >= 1.0) {
			return 0.0;
		}
		if (numerator < 0.0) {
			//System.out.println(Math.toDegrees(Math.PI/2+Math.acos(Math.abs(numerator/m1*m2))));
//			if (Math.PI - Math.acos(Math.abs(numerator/m1*m2)) > Math.PI/2) {
//				System.out.println(Math.PI - Math.acos(Math.abs(numerator/(m1*m2))));
//			}
		//	System.out.println("numerator:"+numerator);
			//System.out.println("m1*m2:"+(m1*m2));
			return Math.PI - Math.acos(Math.abs(numerator/(m1*m2)));
		} else {
//			if (Math.acos(numerator/m1*m2) > Math.PI/2) {
//				System.out.println(Math.acos(numerator/(m1*m2)));
//			}
			//System.out.println("numerator:"+numerator);
			//System.out.println("m1*m2:"+(m1*m2));
			return Math.acos(numerator/(m1*m2));
		}
	}
}
