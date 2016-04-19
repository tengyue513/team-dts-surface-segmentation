import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;


public class StlReader {
	public static int polygonCounter = 0;
	public static Hashtable<Integer,Polygon> polygons = new Hashtable<>();
	public static Hashtable<Edge,ArrayList<Integer>> edgeTable = new Hashtable<>();
	/**.
	 * each vertex corresponds to a list of polygons
	 */
	public static Hashtable<Vertex, ArrayList<Integer>> vertexTable = new Hashtable<>();
	/**.
	 * store all the vertex in one set
	 */
	public static Hashtable<Integer,Vertex> vertexIndexTable = new Hashtable<>();
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
					String[] words = line.trim().split(" ");
					normal = new Vertex(Double.parseDouble(words[2]),Double.parseDouble(words[3]),
							Double.parseDouble(words[4]));
					// store normal
					//System.out.println("normal:+"+line);
					int i = 0;
					while(!(line = scan.nextLine()).contains("end facet")) {
						// store vertex
						if (line.contains("vertex")) {
							words = line.trim().split(" ");
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
			int value = 0;
			for (Vertex v : vertexTable.keySet()) {
				vertexIndexTable.put(value, v);
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
}
