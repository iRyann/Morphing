package morphing;
/*
 * Couple class
 * @param <X> type du premier élément
 * @param <Y> type du deuxième élément
 * @version 1.0
 * @since 1.0
 * @see morphing
 * @see morphing.Couple
 * @see morphing.Couple#equals
 * @see morphing.Couple#copy
 * @see morphing.Couple#getX
 * @see morphing.Couple#getY
 * @see morphing.Couple#setX
 * @see morphing.Couple#setY
 * @see morphing.Couple#toString 
 */
public class Couple<X,Y> {
    private X x;
    private Y y;

    Couple(X x, Y y){
        this.x=x;
        this.y=y;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }

    public void setX(X x) {
        this.x = x;
    }

    public void setY(Y y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Couple{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public boolean equals(Couple<X,Y> c){
        return this.x.equals(c.x) && this.y.equals(c.y);
    }

    public Couple<X,Y> copy(){
        return new Couple<>(this.x,this.y);
    }
}
