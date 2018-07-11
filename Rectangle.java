import java.awt.*;

public class Rectangle extends Fillable_Figure {
    java.awt.Rectangle rectangle;
    public Rectangle(Color fill_color, Color line_color, float line_thickness, int width, int height, int x, int y)
    {
        super(fill_color,
                255,
                line_color,
                line_thickness,
                255,
                width, height,
                x, y, Figure.TOPLEFT,0);
        rectangle = new java.awt.Rectangle(x, y, width, height);
    }

    public void set_width(int width){
        super.set_width(width);
        rectangle.setSize(get_width(),get_height());
    }

    public void set_height(int height){
        super.set_height(height);
        rectangle.setSize(get_width(),get_height());
    }

    public void set_location(int x, int y){
        super.set_location(x, y);
        rectangle.setLocation(x,y);
    }

    public void set_location(Point p){
        super.set_location(p);
        rectangle.setLocation(p);
    }

    public void render(Graphics2D g)
    {
        g.rotate(Math.toRadians(get_rotate_angle()),
                get_render_location().x - get_width()/2,
                get_render_location().y - get_height()/2);

        Color color = new Color(
                get_fill_color().getRed(),
                get_fill_color().getGreen(),
                get_fill_color().getBlue(),
                get_fill_transparency());
        g.setColor(color);

        g.fillRect(get_render_location().x,
                get_render_location().y,
                get_width(),
                get_height());

        g.setStroke(new BasicStroke(get_line_thickness()));

        Color line_color = new Color(
                get_line_color().getRed(),
                get_line_color().getGreen(),
                get_line_color().getBlue(),
                get_line_transparency());
        g.setColor(line_color);

        g.drawRect(get_render_location().x,
                get_render_location().y,
                get_width(),
                get_height());

    }

    @Override
    public boolean contains(Point point) {
        return rectangle.contains(point);
    }
}
