import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;

public class Picture implements Serializable {
    transient private BufferedImage image;
    private int width, height;

    public Picture() {
        this.width = this.height = 0;
        this.image = null;
    }

    public Picture(BufferedImage img) {
        this.setImage(img);
    }

    public Picture(String fn) {
        try {
            this.image = ImageIO.read(new File(fn));
            this.width = this.image.getWidth();
            this.height = this.image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Vec2<Integer> getDimensions() {
        return new Vec2<Integer>(width, height);
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void setImage(BufferedImage img) {
        if (img == null) return;
        this.image = img;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
    }

    public void resize(int w, int h) {
        if (this.image == null) return;
        Image tmp = this.image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage bf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bf.createGraphics();
        g2d.drawImage(tmp, 0, 0, w, h, null);
        g2d.dispose();
        this.image = bf;
        this.width = w;
        this.height = h;
    }

    public void scale(double scalar) {
        if (scalar <= 0) return;
        this.resize((int) (this.width * scalar), (int) (this.height * scalar));
    }

    public void drawBorder(int color) {
        Graphics2D g2d = this.image.createGraphics();

		g2d.setColor(
			new Color(
				(color >> 16) & 0xFF,
				(color >> 8) & 0xFF,
				(color) & 0xFF
			)
		);
        
		g2d.drawLine(0, 0, this.width, 0);
		g2d.drawLine(0, 0, 0, this.height);
        g2d.drawLine(this.width-1, 1, this.width-1, this.height-1);
        g2d.drawLine(1, this.height-1, this.width-1, this.height-1);

    }

    public void write(String out) {
        if (this.image == null) return;
        File output = new File(out);

        try {
            ImageIO.write(this.image, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(this.image, "png", out);
    }

    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.image = ImageIO.read(in);
    }
}
