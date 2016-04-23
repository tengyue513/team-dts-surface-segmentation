
public class driver {
	public static void main(String[] args) {
		StlReader sr = new StlReader();
		sr.read("magnolia.stl");
		sr.bfs(Math.PI * 0.25);
//		for (Polygon p : sr.groupTable.keySet()) {
//			System.out.println(p);
//			System.out.println(sr.groupTable.get(p));
//		}
		System.out.println(sr.groupTable.keySet().size());
		//System.out.println(sr.vertexIndexTable);
		VrmlWriter.write(sr.groupTable, sr.vertexList, sr.vertexIndexTable, "test.wrl");
	}
}
