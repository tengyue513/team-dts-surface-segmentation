
public class driver {
	public static void main(String[] args) {
		StlReader sr = new StlReader();
		sr.read("plate_ascii.stl");
		sr.bfs(Math.PI * 0.25);
		System.out.println(sr.groupTable.values());
		System.out.println(sr.vertexIndexTable);
		VrmlWriter.write(sr.groupTable, sr.vertexList, sr.vertexIndexTable, "test.wrl");
		Dijkstra.shortestDistances(sr.polygons, sr.edgeTable);
	}
}
