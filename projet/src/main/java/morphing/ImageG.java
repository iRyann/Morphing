package morphing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
 * ImageG
 * @version 1.0
 * @since 1.0
 * @see morphing
 * @see morphing.ImageG
 * @see morphing.ImageG#getPixel
 * @see morphing.ImageG#setPixel
 * @see morphing.ImageG#save
 * @see morphing.ImageG#getImage
 * @see morphing.ImageG#ImageG 
 * 
 */
public class ImageG {
    private BufferedImage image;
    private int width;
    private int height;
    private String format;

    public ImageG(String path) {
        try {
            image = ImageIO.read(new File
                    (path));
            width = image.getWidth();
            height = image.getHeight();
            format = path.substring(path.lastIndexOf('.') + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageG(int width, int height, String format) {
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.format = format;
    }
    
    public void setPixel(int x, int y, Color color) {
        image.setRGB(x, y, color.getRGB());
    }

    public Color getPixel(int x, int y) {
        return new Color(image.getRGB(x, y));
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void save(String path) {
        try {
            ImageIO.write(image, format, new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
    }


    public String getFormat() {
        return this.format;
    }

}
