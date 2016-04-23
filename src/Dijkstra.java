/* The authors of this work have released all rights to it and placed it
in the public domain under the Creative Commons CC0 1.0 waiver
(http://creativecommons.org/publicdomain/zero/1.0/).

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Retrieved from: http://en.literateprograms.org/Dijkstra's_algorithm_(Java)?oldid=15444
*/

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class VertexD implements Comparable<VertexD> {
    //public final String name;
    public final Polygon polygon;
    public ArrayList<EdgeD> adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public VertexD previous;

//    public VertexD(String argName) {
//        name = argName;
//    }
    public VertexD(Polygon polygon) {
        this.polygon = polygon;
    }

    public String toString() {
        return polygon.toString();
    }

    public int compareTo(VertexD other) {
        return Double.compare(minDistance, other.minDistance);
    }
}

class EdgeD {
    public final VertexD target;
    public final double weight;

    public EdgeD(VertexD argTarget, double argWeight) {
        target = argTarget;
        weight = argWeight;
    }
}

public class Dijkstra {
    public static void computePaths(VertexD source) {
        source.minDistance = 0.;
        PriorityQueue<VertexD> vertexQueue = new PriorityQueue<VertexD>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            VertexD u = vertexQueue.poll();

            // Visit each edge exiting u
            for (EdgeD e : u.adjacencies) {
                VertexD v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);

                    v.minDistance = distanceThroughU;
                    v.previous = u;
                    vertexQueue.add(v);
                }
            }
        }
    }

    public static List<VertexD> getShortestPathTo(VertexD target) {
        List<VertexD> path = new ArrayList<VertexD>();
        for (VertexD vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);

        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        StlReader sr = new StlReader();
        sr.read("cube1.stl");
        VertexD[] vertexDs = new VertexD[sr.polygons.size()];
        for (int i = 0; i < vertexDs.length; i++) {
            Polygon p = sr.polygons.get(i);
            vertexDs[i] = new VertexD(p);
        }
        for (VertexD vertexD : vertexDs) {
            vertexD.adjacencies = new ArrayList<EdgeD>();
            for (Edge e : vertexD.polygon.getEdges()) {
                ArrayList<Integer> adjacencyList = sr.edgeTable.get(e);
                int index = adjacencyList.get(0);
                if (!vertexDs[index].polygon.equals(vertexD.polygon)) {
                    vertexD.adjacencies.add(new EdgeD(vertexDs[index], index));
                    //System.out.println("add " + vertexDs[index] + " weight " + index);
                } else {
                    index = adjacencyList.get(1);
                    vertexD.adjacencies.add(new EdgeD(vertexDs[index], index));
                    //System.out.println("add " + vertexDs[index] + " weight " + index);
                }
            }
        }

        computePaths(vertexDs[0]);
        System.out.println("Compute for " + vertexDs[0]);
        for (VertexD v : vertexDs) {
            System.out.println("Distance to " + v + ": " + v.minDistance);
            List<VertexD> path = getShortestPathTo(v);
            System.out.println("Path: " + path);
        }
    }
}