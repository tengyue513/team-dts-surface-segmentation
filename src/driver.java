
public class driver {
	public static void main(String[] args) {
		StlReader sr = new StlReader();
		sr.read("tri.stl");
		System.out.println(sr.vertexIndexTable);
	}
	
}
