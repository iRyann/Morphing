package morphing;

public class Point {
    private Couple<Integer, Integer> point;

    public Point(int x, int y) {
        point = new Couple<>(x, y);
    }

    public Couple<Integer, Integer> getPoint() {
        return this.point;
    }

    public void setPoint(Couple<Integer, Integer> point) {
        this.point = point;
    }

    public void setPoint(int x, int y) {
        this.point = new Couple<>(x, y);
    }

    public Point div(int d) {
        return new Point(this.point.getX() / d, this.point.getY() / d);
    }

    public Point add(Point p) {
        return new Point(this.point.getX() + p.getPoint().getX(), this.point.getY() + p.getPoint().getY());
    }

    public Point sub(Point p) {
        return new Point(this.point.getX() - p.getPoint().getX(), this.point.getY() - p.getPoint().getY());
    }

    public Point mul(int d) {
        return new Point(this.point.getX() * d, this.point.getY() * d);
    }

    public Point nextPoint(Point p, int t)
    {
        int x = this.getPoint().getX() * (1-t) + p.getPoint().getX() * t;
        int y = this.getPoint().getY() * (1-t) + p.getPoint().getY() * t;

        return new Point(x, y);
    }
}
