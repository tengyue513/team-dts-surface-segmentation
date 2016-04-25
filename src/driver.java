
public class driver {
	public static void main(String[] args) {
		StlReader sr = new StlReader();

		sr.read("cat1.stl");
		//sr.findBinaryPatch();
		sr.updatePatchIndex();
		//sr.randomDefinePatch();
		sr.divideIntoPatch();
		sr.findPatchNum();
//		int cycle = 5;
//		for (int i =0;i<cycle;i++) {
//			sr.redefinePatchIndex();
//			sr.divideIntoPatch();
//		}
		//sr.read("plate_ascii.stl");
		//sr.bfs(Math.PI * 0.2);
//		for (Polygon p : sr.groupTable.keySet()) {
//			System.out.println(p);
//			System.out.println(sr.groupTable.get(p));
//		}
		//System.out.println(sr.groupTable.keySet().size());
		//System.out.println(sr.vertexIndexTable);
		//VrmlWriter.write(sr.groupTable, sr.vertexList, sr.vertexIndexTable, "test.wrl");
		VrmlWriter.write(sr.patchTable, sr.vertexList, sr.vertexIndexTable, "test.wrl");
		//Dijkstra.shortestDistances(sr.polygons, sr.edgeTable);
	}
}
