
public class driver {
	public static void main(String[] args) {
		StlReader sr = new StlReader();

		sr.read("magnolia.stl");
		//sr.updatePatchIndex();
		//sr.divideIntoPatch();
		//sr.findPatchNum();
		sr.bfs(Math.PI * 0.15);
		VrmlWriter.write(sr.groupTable, sr.vertexList, sr.vertexIndexTable, "test.wrl");
		//VrmlWriter.write(sr.patchTable, sr.vertexList, sr.vertexIndexTable, "test.wrl");
	}
}
