import java.util.Arrays;

public class Vertex {

    private static final int CoordNum = 3;

    private double[] vertex = new double[CoordNum];
    
    public Vertex(double[] vertex) {
        setVertex(vertex);
    }
    
    public Vertex(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    private boolean ensureCoordNum(double[] vertex) {
        if (vertex.length == CoordNum) {
            return true;
        }
        return false;
    }
    
    public double[] getVertex() {
        return vertex;
    }

    public void setVertex(double[] vertex) {
        if (ensureCoordNum(vertex)) {
            this.vertex = vertex;
        }
    }
    
    public double getX() {
        return vertex[0];
    }
    
    public void setX(double x) {
        vertex[0] = x;
    }
    
    public double getY() {
        return vertex[1];
    }
    
    public void setY(double y) {
        vertex[1] = y;
    }
    
    public double getZ() {
        return vertex[2];
    }
    
    public void setZ(double z) {
        vertex[2] = z;
    }
    
    public String print() {
        return "x: " + getX() + ", y: " + getY() + ", z: " + getZ();
    }
    
    @Override
    public String toString() {
        return Arrays.toString(vertex);
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
            Vertex other = (Vertex) object;
            if (toString().equals(other.toString())) {
                result = true;
            }
        }
        return result;
    }
}
