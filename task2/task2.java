import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class task2 {
    private static final double EPSILON = 10e-6;

    public static void main(String[] args) {
        Polygon2D polygon = new Polygon2D(loadPointsFromFile(args[0]));
        List<Point2D> points = loadPointsFromFile(args[1]);

        for (Point2D point : points) {
            int r = whereIsPoint(polygon, point);
            System.out.println(r);
        }
    }

    public static List<Point2D> loadPointsFromFile(String filePath) {
        List<Point2D> points = new ArrayList<>();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] coords = line.split(" ");
                float x = Float.parseFloat(coords[0]);
                float y = Float.parseFloat(coords[1]);
                points.add(new Point2D(x, y));
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return points;
    }

    public static int whereIsPoint(Polygon2D polygon, Point2D p) {
        if (polygon.isPointOnVertex(p)) {
            return 0;
        } else if (polygon.isPointOnEdge(p)) {
            return 1;
        } else if (polygon.isPointInside(p)) {
            return 2;
        }

        return 3;
    }

    static class Point2D {
        public float x;
        public float y;

        public Point2D(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Point2D point2D = (Point2D) o;

            return Float.compare(point2D.x, x) == 0 &&
                    Float.compare(point2D.y, y) == 0;
        }
    }

    static class Line2D {
        public Point2D vertex1;
        public Point2D vertex2;

        public Line2D(Point2D vertex1, Point2D vertex2) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
        }

        public boolean contains(Point2D p) {
            float a = vertex1.y - vertex2.y;
            float b = vertex2.x - vertex1.x;
            float c = vertex1.x * vertex2.y - vertex2.x * vertex1.y;

            if (Math.abs(a * p.x + b * p.y + c) > EPSILON) {
                return false;
            }

            return p.x <= Math.max(vertex1.x, vertex2.x) &&
                    p.x >= Math.min(vertex1.x, vertex2.x) &&
                    p.y <= Math.max(vertex1.y, vertex2.y) &&
                    p.y >= Math.min(vertex1.y, vertex2.y);
        }
    }

    static class Polygon2D {
        public List<Point2D> vertexes;

        public Polygon2D(List<Point2D> vertexes) {
            this.vertexes = vertexes;
        }

        private List<Line2D> getEdges() {
            List<Line2D> edges = new ArrayList<>();
            for (int i = 0; i < vertexes.size(); i++) {
                Point2D vertex1 = vertexes.get(i);
                Point2D vertex2 = vertexes.get((i + 1) % vertexes.size());

                edges.add(new Line2D(vertex1, vertex2));
            }
            return edges;
        }

        public boolean isPointOnVertex(Point2D p) {
            for (Point2D v : vertexes) {
                if (v.equals(p)) {
                    return true;
                }
            }

            return false;
        }

        public boolean isPointOnEdge(Point2D p) {
            for (Line2D edge : getEdges()) {
                if (edge.contains(p)) {
                    return true;
                }
            }

            return false;
        }

        public boolean isPointInside(Point2D p) {
            for (Line2D edge : getEdges()) {
                if (orientation(edge.vertex1, edge.vertex2, p) == Orientation.COLLINEAR) {
                    return false;
                } else if (orientation(edge.vertex1, edge.vertex2, p) == Orientation.COUNTERCLOCKWISE) {
                    return false;
                }
            }

            return true;
        }

        private Orientation orientation(Point2D p1, Point2D p2, Point2D p3) {
            float result = (p2.y - p1.y) * (p3.x - p2.x) - (p3.y - p2.y) * (p2.x - p1.x);

            if (Math.abs(result) < EPSILON) {
                return Orientation.COLLINEAR;
            }

            return result > 0 ? Orientation.CLOCKWISE : Orientation.COUNTERCLOCKWISE;
        }
    }

    enum Orientation {
        CLOCKWISE,
        COUNTERCLOCKWISE,
        COLLINEAR
    }
}
