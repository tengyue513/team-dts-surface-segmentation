import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;


public class StlReader {
	public static int polygonCounter = 0;
	public static Hashtable<Integer,Polygon> polygons = new Hashtable<>();
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
					polygons.put(polygonCounter, p);
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
	public static void bfs(double threshold) {
		int groupCounter = -1;
		System.out.println("total polygon number: "+polygonCounter);
		for (int i = 0; i < polygonCounter; i++) {
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
			while (!todoList.isEmpty()) {
				int size = todoList.size();
				for (int j = 0; j < size; j++) {
					Polygon currPolygon = todoList.getFirst();
					for (Edge e : currPolygon.getEdges()) {
						ArrayList<Integer> shareEdgeList = edgeTable.get(e);
						if (shareEdgeList.size() != 2) {
							System.out.println("incorrect shared edge "+shareEdgeList.size());
						}
						//System.out.println("correct shared edge "+shareEdgeList.size());
						Polygon nearPolygon = polygons.get(shareEdgeList.get(0));
						if (!nearPolygon.equals(currPolygon)) {
							if(getAngle(currPolygon, nearPolygon) <= threshold) {
								if (groupTable.containsKey(nearPolygon)) {
									continue;
								}
								todoList.add(nearPolygon);
								groupTable.put(nearPolygon, groupCounter);
							}
							//polygons.get(shareEdgeList.get(0));
							//System.out.println("one diff");
						}
						nearPolygon = polygons.get(shareEdgeList.get(1));
						if (!nearPolygon.equals(currPolygon)) {
							if (getAngle(currPolygon, nearPolygon) <= threshold) {
								if (groupTable.containsKey(nearPolygon)) {
									continue;
								}
								todoList.add(nearPolygon);
								groupTable.put(nearPolygon, groupCounter);
							}
							//todoList.add(polygons.get(shareEdgeList.get(1)));
							//System.out.println("one diff");
						}
					}
					//System.out.println("prev size"+todoList.size());
					todoList.remove();
					//System.out.println(todoList.size());
				}
			}
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
		if (m1*m2 == 0) {
			//System.out.println(Math.toDegrees(Math.PI/2));
			return Math.PI/2;
		}
		if (numerator < 0) {
			//System.out.println(Math.toDegrees(Math.PI/2+Math.acos(Math.abs(numerator/m1*m2))));
			return Math.PI - Math.acos(Math.abs(numerator/m1*m2));
		} else {
			//System.out.println(Math.toDegrees(Math.acos(numerator/m1*m2)));
			return Math.acos(numerator/m1*m2);
		}
	}
}
