import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Icon implements Serializable {
    private String title, description;
    private Picture img;
    private Configuration configuration;
    private Vec2<Integer> dimensions = new Vec2<Integer>(100, 100), pos = new Vec2<Integer>(50, 50);

    public Icon(Configuration c) {
        this.setConfiguration(c);
    }

    public Icon(Configuration c, Picture p) {
        this(c);
        this.img = p;
    }

    public Icon(String title, Configuration c, Picture p) {
        this(c, p);
        this.title = title;
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return this.description;
    }

    public void setConfiguration(Configuration cfg) {
        this.configuration = cfg;
        this.generateImage();
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    private void generateImage() {
        if (this.configuration == null) return;
        this.img = configuration.generateImage();
        this.dimensions = new Vec2<Integer>(this.img.getDimensions().getX(), this.img.getDimensions().getY());
    }

    public void setIcon(Picture p) {
        this.img = p;
        this.dimensions = new Vec2<Integer>(this.img.getDimensions().getX(), this.img.getDimensions().getY());
    }

    public Picture getIcon() {
        return this.img;
    }

    public BufferedImage getImage() {
        return this.img.getImage();
    }

    public Vec2<Integer> getPos() {
        return this.pos;
    }

    public void setPos(Vec2<Integer> p) {
        if (p != null) {
            this.pos = p;
        }
    }

    public Vec2<Integer> getDimensions() {
        return this.dimensions;
    }

    public void resize(int w, int h) {
        this.img.resize(w, h);
        this.dimensions = new Vec2<Integer>(this.img.getDimensions().getX(), this.img.getDimensions().getY());
    }

    public void scale(double scalar) {
        if (scalar <= 0) return;
        this.resize((int) (this.dimensions.getX() * scalar), (int) (this.dimensions.getY() * scalar));
    }

    public boolean intersects(Vec2<Integer> p) {
        return (
            (p.getX() >= this.pos.getX() && p.getX() <= this.pos.getX() + this.dimensions.getX()) && 
            (p.getY() >= this.pos.getY() && p.getY() <= this.pos.getY() + this.dimensions.getY())
        );
    }
}
