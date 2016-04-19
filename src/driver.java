
public class driver {
	public static void main(String[] args) {
		StlReader sr = new StlReader();
		sr.read("tri.stl");
		sr.bfs();
		System.out.println(sr.groupTable.values());
		System.out.println(sr.vertexIndexTable);
	}
}
