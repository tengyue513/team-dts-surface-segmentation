
public class Edge {

    private static final int PointNum = 2;
    private Vertex[] edge = new Vertex[PointNum];
    
    public Edge(Vertex[] edge){
        setEdges(edge);
    }
    
    public Edge(Vertex vertex, Vertex vertex2) {
        edge[0] = vertex;
        edge[1] = vertex2;
    }
    

    private boolean ensurePointNum(Vertex[] edge) {
        if (edge.length == PointNum) {
            return true;
        }
        return false;
    }

    public Vertex[] getEdge() {
        return edge;
    }

    public void setEdges(Vertex[] edge) {
        if (ensurePointNum(edge)) {
            this.edge = edge;
        }
    }
    
    @Override
    public String toString() {
        String vertexString1 = edge[0].toString();
        String vertexString2 = edge[1].toString();
        String result;
        if (vertexString1.compareTo(vertexString2) < 0) {
            result = vertexString1 + vertexString2;
        } else {
            result = vertexString2 + vertexString1;
        }
        return result;
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            Edge other = (Edge) object;
            if (toString().equals(other.toString())) {
                result = true;
            }
        }
        return result;
    }
}
