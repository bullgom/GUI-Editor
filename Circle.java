import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Circle extends Fillable_Figure {
    private Ellipse2D circle;
    public Circle(Color fill_color, Color line_color, float line_thickness, int width, int height, int x, int y)
    {
        super(fill_color,
                255,
                line_color,
                line_thickness,
                255,
                width, height,
                x, y, Figure.TOPLEFT,0);

        circle = new Ellipse2D.Float(x,y,width,height);
    }

    public void set_width(int width){
        super.set_width(width);
        circle.setFrame(get_location().x,get_location().y,get_width(),get_height());
    }

    public void set_height(int height){
        super.set_height(height);
        circle.setFrame(get_location().x,get_location().y,get_width(),get_height());
    }

    public void set_location(int x, int y){
        super.set_location(x, y);
        circle.setFrame(get_location().x, get_location().y, get_width(), get_height());
    }

    public void set_location(Point p){
        super.set_location(p);
        circle.setFrame(p.x, p.y,get_width(),get_height());
    }

    public void render(Graphics2D g) {
        g.rotate(Math.toRadians(get_rotate_angle()),
                get_render_location().x - get_width() / 2,
                get_render_location().y - get_height() / 2);

        Color color = new Color(
                get_fill_color().getRed(),
                get_fill_color().getGreen(),
                get_fill_color().getBlue(),
                get_fill_transparency());
        g.setColor(color);
        g.fillOval(get_location().x,
                get_location().y,
                get_width(),
                get_height());


        g.setStroke(new BasicStroke(get_line_thickness()));

        Color line_color = new Color(
                get_line_color().getRed(),
                get_line_color().getGreen(),
                get_line_color().getBlue(),
                get_line_transparency());
        g.setColor(line_color);
        g.drawOval(get_location().x,
                get_location().y,
                get_width(),
                get_height());
    }

    @Override
    public boolean contains(Point p) {
        return circle.contains(p);
    }
}