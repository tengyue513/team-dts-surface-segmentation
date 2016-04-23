
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
import java.util.HashMap;
import java.util.Hashtable;

class VertexD implements Comparable<VertexD> {
    // public final String name;
    public final Polygon polygon;
    public ArrayList<EdgeD> adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public VertexD previous;

    // public VertexD(String argName) {
    // name = argName;
    // }
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

    private static double weight(Polygon p1, Polygon p2) {
        double delta = 0.5;
        double avgGeod = StlReader.avgGeod;
        double avgAngd = StlReader.avgAngd;
        double geod = p1.geoDistance(p2);
        double angd;
        if (p1.isConcave(p2)) {
            angd = p1.angDistance(p2, 1.0);
        } else {
            angd = p1.angDistance(p2, StlReader.eta);
        }
        return delta * geod / avgGeod + (1 - delta) * angd / avgAngd;
    }

    public static HashMap<Edge, Double> shortestDistances(
            ArrayList<Polygon> polygons,
            Hashtable<Edge, ArrayList<Integer>> edgeTable) {
        HashMap<Edge, Double> map = new HashMap<Edge, Double>();

        VertexD[] vertexDs = new VertexD[polygons.size()];
        for (int i = 0; i < vertexDs.length; i++) {
            Polygon p = polygons.get(i);
            vertexDs[i] = new VertexD(p);
        }

        for (VertexD vertexD : vertexDs) {
            vertexD.adjacencies = new ArrayList<EdgeD>();
            for (Edge e : vertexD.polygon.getEdges()) {
                ArrayList<Integer> adjacencyList = edgeTable.get(e);
                int index = adjacencyList.get(0);
                if (!vertexDs[index].polygon.equals(vertexD.polygon)) {
                    vertexD.adjacencies.add(new EdgeD(vertexDs[index],
                            weight(vertexD.polygon, vertexDs[index].polygon)));
                    // System.out.println("add " + vertexDs[index] + " weight "
                    // + index);
                } else if (adjacencyList.size() > 1) {
                    index = adjacencyList.get(1);
                    vertexD.adjacencies.add(new EdgeD(vertexDs[index],
                            weight(vertexD.polygon, vertexDs[index].polygon)));
                    // System.out.println("add " + vertexDs[index] + " weight "
                    // + index);
                }
            }
        }

        for (VertexD vertexD : vertexDs) {
            computePaths(vertexD);
            System.out.println("Compute for " + vertexD);
            for (VertexD v : vertexDs) {
                // System.out.println("Distance to " + v + ": " +
                // v.minDistance);
                // List<VertexD> path = getShortestPathTo(v);
                // System.out.println("Path: " + path);
                if (v.minDistance != Double.POSITIVE_INFINITY
                        && v.minDistance != 0.0) {
                    Edge e = new Edge(vertexD.polygon.getCom(),
                            v.polygon.getCom());
                    map.put(e, v.minDistance);
                }
            }
        }
        System.out.println(map);
        return map;
    }

    // public static void main(String[] args) {
    // StlReader sr = new StlReader();
    // sr.read("cube1.stl");
    // VertexD[] vertexDs = new VertexD[sr.polygons.size()];
    // for (int i = 0; i < vertexDs.length; i++) {
    // Polygon p = sr.polygons.get(i);
    // vertexDs[i] = new VertexD(p);
    // }
    // for (VertexD vertexD : vertexDs) {
    // vertexD.adjacencies = new ArrayList<EdgeD>();
    // for (Edge e : vertexD.polygon.getEdges()) {
    // ArrayList<Integer> adjacencyList = sr.edgeTable.get(e);
    // int index = adjacencyList.get(0);
    // if (!vertexDs[index].polygon.equals(vertexD.polygon)) {
    // vertexD.adjacencies.add(new EdgeD(vertexDs[index], index));
    // //System.out.println("add " + vertexDs[index] + " weight " + index);
    // } else {
    // index = adjacencyList.get(1);
    // vertexD.adjacencies.add(new EdgeD(vertexDs[index], index));
    // //System.out.println("add " + vertexDs[index] + " weight " + index);
    // }
    // }
    // }
    //
    // computePaths(vertexDs[0]);
    // System.out.println("Compute for " + vertexDs[0]);
    // for (VertexD v : vertexDs) {
    // System.out.println("Distance to " + v + ": " + v.minDistance);
    // List<VertexD> path = getShortestPathTo(v);
    // System.out.println("Path: " + path);
    // }
    // }
}