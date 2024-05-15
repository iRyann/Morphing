package morphing;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.squareup.gifencoder.GifEncoder;
import com.squareup.gifencoder.ImageOptions;


public class MorphingApp {

    private ImageT imgSrc;
    private ImageT imgDest;
    private ImageT[] frames;
    private int nbFrames;
    private int nbLines;

    public ImageT getImgSrc() {
        return this.imgSrc;
    }

    public void setImgSrc(ImageT imgSrc) {
        this.imgSrc = imgSrc;
    }

    public ImageT getImgDest() {
        return this.imgDest;
    }

    public void setImgDest(ImageT imgDest) {
        this.imgDest = imgDest;
    }

    public ImageT[] getFrames() {
        return this.frames;
    }

    public void setFrames(ImageT[] frames) {
        this.frames = frames;
    }

    public int getNbFrames() {
        return this.nbFrames;
    }

    public void setNbFrames(int nbFrames) {
        this.nbFrames = nbFrames;
    }

    public int getNbLines() {
        return this.nbLines;
    }

    public void setNbLines(int nbLines) {
        this.nbLines = nbLines;
    }

    /**
     * Constructeur par défaut
     */
    public MorphingApp() {
        this.imgSrc = null;
        this.imgDest = null;
        this.frames = null;
        this.nbFrames = 0;
        this.nbLines = 0;
    }

    /**
     * Créer une image intermédiaire à partir de l'image de départ et de l'image d'arrivée
     * @param t : ordre de l'image intermédiaire
     * @return ImageT
     */
    public ImageT newFrame(int t){
        ImageT frame = new ImageT(imgSrc.getWidth(), imgSrc.getHeight(), imgSrc.getFormat());

        for (int i = 0 ; i < this.getNbLines() ; i++)
        {
            Line v1 = imgSrc.getLine(i);
            Line v2 = imgDest.getLine(i);

            // Calcul des nouveaux points pour définir les nouvelles lignes de contrainte
            Point p = v1.getStart().nextPoint(v2.getStart(), t);
            Point q = v1.getStart().nextPoint(v2.getStart(), t);

            frame.addLine(new Line(p, q));
        } 

        return frame;
    }

    
    /**
     * Calcul de la transformation de l'image de départ vers l'image d'arrivée
     * @param imgSrc : image de départ
     * @param imgDest : image d'arrivée
     * @return ImageT
     */
    public void wrap(ImageT imgSrc, ImageT imgDest){
        double a = 0.5f;
        double b = 0.5f;
        double length;
        for (int x = 0 ; x < imgDest.getWidth() ; x++)
        {
            for (int y = 0 ; y < imgDest.getHeight() ; y++)
            {
                Point dsum = new Point(0, 0);
                int weightsum = 0;
                double dist = imgDest.getWidth() * imgDest.getHeight();

                for (int k = 0 ; k < this.getNbLines() ; k++){
                    
                    // Calcul de (u,v) dans imgDest
                    length = imgDest.getLine(k).norme();
                    double u, v;
                    Line l = imgDest.getLine(k);
                    u = l.hauteurRelative(new Point(x, y));
                    v = l.dist(new Point(x, y));

                    // Déduction de (x',y') dans imgSrc d'après (u,v)
                    Line lp = imgSrc.getLine(k);
                    Point pp = lp.getStart();
                    Point qp = lp.getEnd();
                    Point xpH = new Point((int)(pp.getPoint().getX() + u * l.getVector().getX()), (int)(pp.getPoint().getY() + u * l.getVector().getY()));
                    Point xp = new Point((int)(xpH.getPoint().getX() + v * lp.vectorNormalUnitaire().getX()), (int)(xpH.getPoint().getY() - v * lp.vectorNormalUnitaire().getY()));
                    
                    // Calcul du déplacement X'-X
                    Point d = new Point(x - xp.getPoint().getX(), y - xp.getPoint().getY());

                    // Calcul de la plus courte distance entre (x,y) et la ligne de contrainte
                    if (0<=u && u<=1){
                        dist = Math.abs(v);
                    } else {
                        if(u<0){
                            dist = Math.sqrt(Math.pow(x - pp.getPoint().getX(), 2) + Math.pow(y - pp.getPoint().getY(), 2));
                        } else {
                            dist = Math.sqrt(Math.pow(x - qp.getPoint().getX(), 2) + Math.pow(y - qp.getPoint().getY(), 2));
                        }
                    }
                    
                    // Calcul du poids
                    double weight = Math.pow(length / (dist + a),b);

                    // Calcul de la somme des distances et des poids
                    dsum.setPoint((int) (dsum.getPoint().getX() + d.getPoint().getX() * weight),(int) (dsum.getPoint().getY() +  d.getPoint().getY() * weight)); 
                    weightsum += weight;
                }

                // Calcul de la nouvelle position du pixel
                int xnew = (int) (x + dsum.div(weightsum).getPoint().getX() );
                int ynew = (int) (y + dsum.div(weightsum).getPoint().getY() );

                // Vérification des bornes
                if (xnew >= 0 && xnew < imgSrc.getWidth() && ynew >= 0 && ynew < imgSrc.getHeight())
                {
                    int pix = imgSrc.getImage().getRGB(xnew, ynew);
                    imgDest.getImage().setRGB(x, y, pix);
                }
            }
        }
    }

    /**
     * Interpolation de couleur
     * @param k : ordre de l'image intermédiaire (entre 0 et 1)
     * @param wrapSrc : image de départ
     * @param wrapDest : image d'arrivée
     * @return ImageT
     */
    public ImageT interpolateColor(int k, ImageT wrapSrc, ImageT wrapDest){
        ImageT img = new ImageT(wrapSrc.getWidth(), wrapSrc.getHeight(), wrapSrc.getFormat());
        for (int x = 0 ; x < wrapSrc.getWidth() ; x++)
        {
            for (int y = 0 ; y < wrapSrc.getHeight() ; y++)
            {
                int pixSrc = wrapSrc.getImage().getRGB(x, y);
                int pixDest = wrapDest.getImage().getRGB(x, y);
                int pix = (int) (pixSrc * (1 - k) + pixDest * k);
                img.getImage().setRGB(x, y, pix);
            }
        }
        return img;

    }



    /**
     * Calcul de la transformation de l'image de départ vers l'image d'arrivée
     * @param imgSrc : image de départ
     * @param imgDest : image d'arrivée
     * @return ImageT
     */
    public void calculate(){
        for (int f = 0 ; f <= this.getNbFrames() ; f++){
            int t = f/this.getNbFrames();
            ImageT wrapSrc = newFrame(t);
            ImageT wrapDest = newFrame(t);
            wrap(imgSrc, wrapSrc);
            wrap(imgDest, wrapDest);
            frames[f] = interpolateColor(t, wrapSrc, wrapDest);
        }
    }

    /**
     * Sauvegarde des images intermédiaires
     * @param path : chemin de sauvegarde
     */
    public void saveFrames(String path){
        for (int i = 0 ; i < this.getNbFrames() ; i++){
            frames[i].save(path + "/frame" + i + ".png");
        }
    }

    /**
     * Génération du gif
     */
    public void generateGif(String pathSrc, String pathTarget){
        try {
            saveFrames(pathSrc);
            OutputStream outputStream = new FileOutputStream(pathTarget+".gif");
            ImageOptions options = new ImageOptions();
            GifEncoder encod = new GifEncoder(outputStream, imgSrc.getWidth(), imgSrc.getHeight(), 0);
            int[][] pixels = new int[imgSrc.getWidth()][imgSrc.getHeight()];
            for (int i = 0 ; i < this.getNbFrames() ; i++){
                for (int x = 0 ; x < imgSrc.getWidth() ; x++){
                    for (int y = 0 ; y < imgSrc.getHeight() ; y++){
                        pixels[x][y] = frames[i].getImage().getRGB(x, y);
                    }
                }
                encod.addImage(pixels, options);
            }
            encod.finishEncoding();

            outputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Génération du gif
     */
    public void generateGif(String pathTarget){
        try {
            OutputStream outputStream = new FileOutputStream(pathTarget+".gif");
            ImageOptions options = new ImageOptions();
            GifEncoder encod = new GifEncoder(outputStream, imgSrc.getWidth(), imgSrc.getHeight(), 0);
            int[][] pixels = new int[imgSrc.getWidth()][imgSrc.getHeight()];
            for (int i = 0 ; i < this.getNbFrames() ; i++){
                for (int x = 0 ; x < imgSrc.getWidth() ; x++){
                    for (int y = 0 ; y < imgSrc.getHeight() ; y++){
                        pixels[x][y] = frames[i].getImage().getRGB(x, y);
                    }
                }
                encod.addImage(pixels, options);
            }
            encod.finishEncoding();

            outputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
