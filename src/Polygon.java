
public class Polygon {

    private Vertex[] vertexes = new Vertex[3];

    private Edge[] edges = new Edge[3];

    private Vertex normal;
    
    private Vertex com;
    
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
        
        double[] comArray = new double[3];
        for (int i = 0; i < vertexNum; i++) {
            comArray[0] += vertexes[i].getX();
            comArray[1] += vertexes[i].getY();
            comArray[2] += vertexes[i].getZ();
        }
        comArray[0] /= 3;
        comArray[1] /= 3;
        comArray[2] /= 3;
        
        setCom(new Vertex(comArray));
        
        for (int i = 0; i < vertexNum; i++) {
            edges[i] = new Edge(vertexes[i], vertexes[(i + 1) % vertexNum]);
        }
        
        this.normal = normal;
    }
    private boolean needRevert(Vertex[] vertexes, Vertex normal) {
        Vertex vec1 = new Vertex(vertexes[1].getX() - vertexes[0].getX(),
                vertexes[1].getY() - vertexes[0].getY(),
                vertexes[1].getZ() - vertexes[0].getZ());
        Vertex vec2 = new Vertex(vertexes[2].getX() - vertexes[1].getX(),
                vertexes[2].getY() - vertexes[1].getY(),
                vertexes[2].getZ() - vertexes[1].getZ());
        Vertex cp = vec1.crossProduct(vec2);

        if (cp.dotProduct(normal) < 0) {
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

    public Vertex getCom() {
        return com;
    }
    
    public double geoDistance(Polygon adjacent) {
        Vertex adjCom = adjacent.getCom();
        return Math.sqrt(Math.pow((com.getX() - adjCom.getX()), 2) 
                + Math.pow((com.getY() - adjCom.getY()), 2)
                + Math.pow((com.getZ() - adjCom.getZ()), 2));
    }
    
    public double angDistance(Polygon adjacent, double eta) {
        Vertex adjNorm = adjacent.getNormal();
        double cosAlpha = normal.dotProduct(adjNorm)
                / (normal.magnitude() * adjNorm.magnitude());

        return eta * (1 - cosAlpha);
    }
    
    public boolean isConcave(Polygon adjacent) {
        Vertex adjCom = adjacent.getCom();
        Vertex vec = new Vertex(adjCom.getX() - com.getX(), 
                adjCom.getY() - com.getY(),
                adjCom.getZ() - com.getZ());

        if (normal.dotProduct(vec) > 0) {
            return true;
        }        
        return false;
    }

    public void setCom(Vertex com) {
        this.com = com;
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
