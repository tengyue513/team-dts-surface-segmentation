
public class Polygon {

    private Vertex[] vertexes = new Vertex[3];

    private Edge[] edges = new Edge[3];

    private Vertex normal;
    
    public Polygon(Vertex[] vertexes, Vertex normal) {
        int vertexNum = vertexes.length;
        
        for (int i = 0; i < vertexNum; i++) {
            this.vertexes[i] = vertexes[i];
        }
        
        for (int i = 0; i < vertexNum; i++) {
            edges[i] = new Edge(vertexes[i], vertexes[(i + 1) % vertexNum]);
        }
        
        this.normal = normal;
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
