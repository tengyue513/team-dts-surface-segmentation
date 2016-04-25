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
	public Vertex add(Vertex v2) {
		return new Vertex(vertex[0]+v2.getX(),vertex[1]+v2.getY(),vertex[2]+v2.getZ());
	}
	public Vertex divide(double f) {
		if (f!=0.0) {
			return new Vertex(vertex[0]/f,vertex[1]/f,vertex[2]/f);
		}
		System.out.println("wrong factor");
		return null;
	}
	public Vertex multiply(double f) {
		return new Vertex(vertex[0]*f,vertex[1]*f,vertex[2]*f);
	}
	public double getDist(Vertex v2) {
		return Math.sqrt(Math.pow((vertex[0] - v2.getX()), 2) 
                + Math.pow((vertex[1] - v2.getY()), 2)
                + Math.pow((vertex[2] -v2.getZ()), 2));
	}
	public double magnitude() {
		return Math.sqrt(vertex[0] * vertex[0] + vertex[1] * vertex[1]
				+ vertex[2] * vertex[2]);
	}

	public double dotProduct(Vertex other) {
		return vertex[0] * other.getX() + vertex[1] * other.getY()
				+ vertex[2] * other.getZ();
	}

	public Vertex crossProduct(Vertex other) {
		Vertex cp = new Vertex(vertex[1] * other.getZ() - vertex[2] * other.getY(),
				vertex[2] * other.getX() - vertex[0] * other.getZ(),
				vertex[0] * other.getY() - vertex[1] * other.getX());
		return cp;
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
