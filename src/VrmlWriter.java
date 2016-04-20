import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class VrmlWriter {

    public static void write(Hashtable<Polygon, Integer> groupTable,
            ArrayList<Vertex> vertexList,
            Hashtable<Vertex, Integer> vertexIndexTable, String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("#VRML V2.0 utf8\n");
            writer.write("Shape{\n");
            writer.write("geometry IndexedFaceSet{\n");
            writer.write("coord Coordinate{\n");
            writer.write("point[");

            Iterator<Vertex> itr = vertexList.iterator();
            if (itr.hasNext()) {
                Vertex v = itr.next();
                writer.write(v.getX() + " ");
                writer.write(v.getY() + " ");
                writer.write(v.getZ() + "");
            }
            while (itr.hasNext()) {
                Vertex v = itr.next();
                writer.write(",\n");
                writer.write(v.getX() + " ");
                writer.write(v.getY() + " ");
                writer.write(v.getZ() + "");
            }

            writer.write("]\n");
            writer.write("}\n");
            writer.write("coordIndex[");

            Iterator<Polygon> itrP = groupTable.keySet().iterator();
            if (itrP.hasNext()) {
                Polygon p = itrP.next();
                Vertex[] vertices = p.getVertexes();
                writer.write(vertexIndexTable.get(vertices[0]) + " ");
                writer.write(vertexIndexTable.get(vertices[1]) + " ");
                writer.write(vertexIndexTable.get(vertices[2]) + " ");
                writer.write("-1");
            }
            int count = 1;
            while (itrP.hasNext()) {
                Polygon p = itrP.next();
                Vertex[] vertices = p.getVertexes();
                writer.write(",\n");
                writer.write(vertexIndexTable.get(vertices[0]) + " ");
                writer.write(vertexIndexTable.get(vertices[1]) + " ");
                writer.write(vertexIndexTable.get(vertices[2]) + " ");
                writer.write("-1");
                count++;
            }
            System.out.println("drawed polygon:"+count);
            writer.write("]\n");
            writer.write("color Color{\n");
            writer.write("color[");

            List<Integer> groups = new ArrayList<Integer>(groupTable.values());
            Collections.sort(groups);
            Integer max = groups.get(groups.size() - 1);

            double[] rgb = mapToRGB(0.0);
            writer.write(rgb[0] + " ");
            writer.write(rgb[1] + " ");
            writer.write(rgb[2] + "");
            for (int i = 1; i <= max; i++) {
                rgb = mapToRGB((double) i / max);
                writer.write(",\n");
                writer.write(rgb[0] + " ");
                writer.write(rgb[1] + " ");
                writer.write(rgb[2] + "");
            }

            writer.write("]\n");
            writer.write("}\n");
            writer.write("colorIndex[");

            itrP = groupTable.keySet().iterator();
            if (itrP.hasNext()) {
                Polygon p = itrP.next();
                Vertex[] vertices = p.getVertexes();
                writer.write(groupTable.get(p) + " ");
                writer.write(groupTable.get(p) + " ");
                writer.write(groupTable.get(p) + " ");
                writer.write("-1");
            }
            count=1;
            while (itrP.hasNext()) {
                Polygon p = itrP.next();
                Vertex[] vertices = p.getVertexes();
                writer.write(",\n");
                writer.write(groupTable.get(p) + " ");
                writer.write(groupTable.get(p) + " ");
                writer.write(groupTable.get(p) + " ");
                writer.write("-1");
                count++;
            }
            System.out.println("corlor count: "+count);
            writer.write("]\n");
            writer.write("}\n");
            writer.write("}\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double[] mapToRGB(double x) {
        double[] rgb = { 0.0, 0.0, 0.0 };
        if (x <= 0.125) {
            rgb[0] = 0.5 + 4 * x;
        } else if (x <= 0.375) {
            rgb[0] = 1;
            rgb[1] = 4 * (x - 0.125);
        } else if (x <= 0.625) {
            rgb[0] = 1 - 4 * (x - 0.375);
            rgb[1] = 1;
            rgb[2] = 4 * (x - 0.375);
        } else if (x < 0.875) {
            rgb[1] = 1 - 4 * (x - 0.625);
            rgb[2] = 1;
        } else {
            rgb[2] = 1 - 4 * (x - 0.875);
        }

        return rgb;
    }
}
