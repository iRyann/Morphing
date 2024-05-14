package morphing;
public class Line {
    private Couple<Point,Point> line;
    private Couple<Double,Double> vector;

    public Line(Couple<Point,Point> line) {
        this.line = line;
        double x = line.getY().getPoint().getX()-line.getX().getPoint().getX();
        double y = line.getX().getPoint().getY()-line.getX().getPoint().getY();
        vector = new Couple<Double,Double>(x, y);
    }

    public Line(Point start, Point end) {
        this.line = new Couple<Point,Point>(start, end);
        double x = end.getPoint().getX()-start.getPoint().getX();
        double y = end.getPoint().getY()-start.getPoint().getY();
        vector = new Couple<Double,Double>(x, y);
    }

    public Couple<Point,Point> getLine() {
        return this.line;
    }

    public void setLine(Couple<Point,Point> line) {
        this.line = line;
    }

    public Point getStart() {
        return this.line.getX();
    }

    public Point getEnd() {
        return this.line.getY();
    }

    public void setStart(Point start) {
        this.line.setX(start);
    }

    public void setEnd(Point end) {
        this.line.setY(end);
    }

    public Couple<Double,Double> getVector() {
        return this.vector;
    }

    public double norme() {
        return Math.sqrt(Math.pow(this.vector.getX(), 2) + Math.pow(this.vector.getY(), 2));
    }

    public Couple<Double,Double> vectorNormal(){
        // Rotation du vecteur de pi/2
        double x = -this.vector.getY();
        double y = this.vector.getX();
        return new Couple<Double,Double>(x, y);
    }
    public double scalaire(Line y){
        return this.vector.getX()*y.getVector().getX()+this.vector.getY()*y.getVector().getY();
    }

    public double hauteurRelative(Point x){

        // Calcul de [PX] et [PQ]
        Couple<Double,Double> s1 = new Couple<Double,Double>((double)x.getPoint().getX()-this.getStart().getPoint().getX(), (double)x.getPoint().getY()-this.getStart().getPoint().getY());
        Couple<Double,Double> s2 = getVector();

        // Numerateur
        Double num = s1.getX()*s2.getX()+s1.getY()*s2.getY();

        // Dénominateur
        Double den = this.norme() * this.norme();

        return num/den;

    }

    public double dist(Point x){
        // Calcul de [XP] et perpendiculaire
        Couple<Double,Double> s1 = new Couple<Double,Double>((double)x.getPoint().getX()-this.getStart().getPoint().getX(), (double)x.getPoint().getY()-this.getStart().getPoint().getY());
        Couple<Double,Double> s2 = this.vectorNormal();

        // Numerateur
        double num = s1.getX()*s2.getX()+s1.getY()*s2.getY();

        // Dénominateur
        double den = this.norme();

        return Math.abs(num/den);
    }






    


}
