
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
        
        if (((y1 * z2 - z1 * y2) * normal.getX() < 0) ||
                ((z1 * x2 - x1 * z2) * normal.getY() < 0) ||
                ((x1 * y2 - y1 * x2) * normal.getZ() < 0)) {
            System.out.println(y1 * z2 + z1 * y2);
            System.out.println(z1 * x2 + x1 * z2);
            System.out.println(x1 * y2 + y1 * x2);
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
        return "Vertexes: " + vertexes[0].print() + " " + vertexes[1].print() + " " + vertexes[2].print() 
                + " Normal: " + normal.toString();
    }
}
