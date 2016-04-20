
public class Polygon {

    private Vertex[] vertexes = new Vertex[3];

    private Edge[] edges = new Edge[3];

    private Vertex normal;
    
    public Polygon(Vertex[] vertexes, Vertex normal) {
        int vertexNum = vertexes.length;
        
        if (needRevert(vertexes, normal)) {
            for (int i = 0; i < vertexNum; i++) {
                this.vertexes[i] = vertexes[vertexNum - i - 1];
            }
        } else {
            for (int i = 0; i < vertexNum; i++) {
                this.vertexes[i] = vertexes[i];
            }
        }
        
        for (int i = 0; i < vertexNum; i++) {
            edges[i] = new Edge(vertexes[i], vertexes[(i + 1) % vertexNum]);
        }
        
        this.normal = normal;
    }
    
    private boolean needRevert(Vertex[] vertexes, Vertex normal) {
        double x1 = vertexes[1].getX() - vertexes[0].getX();
        double y1 = vertexes[1].getY() - vertexes[0].getY();
        double z1 = vertexes[1].getZ() - vertexes[0].getZ();
        double x2 = vertexes[2].getX() - vertexes[1].getX();
        double y2 = vertexes[2].getY() - vertexes[1].getY();
        double z2 = vertexes[2].getZ() - vertexes[1].getZ();
        
        
        if (((y1 * z2 - z1 * y2) * normal.getX() +
                (z1 * x2 - x1 * z2) * normal.getY() +
                (x1 * y2 - y1 * x2) * normal.getZ()) < 0) {
            return true;
        }
        
        return false;
    }
    
    public Vertex[] getVertexes() {
        return vertexes;
    }

    public void setVertexes(Vertex[] vertexes) {
        this.vertexes = vertexes;
    }

    public Edge[] getEdges() {
        return edges;
    }

    public void setEdges(Edge[] edges) {
        this.edges = edges;
    }

    public Vertex getNormal() {
        return normal;
    }

    public void setNormal(Vertex normal) {
        this.normal = normal;
    }
    
    @Override
    public String toString() {
        return vertexes[0].toString() + " " + vertexes[1].toString() + " " + vertexes[2].toString();
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
            Polygon other = (Polygon) object;
            if (toString().equals(other.toString())) {
                result = true;
            }
        }
        return result;
    }
}
