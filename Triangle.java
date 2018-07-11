import java.awt.*;

public class Triangle extends Fillable_Figure {
    private int top;
    private Polygon polygon;

    public Triangle(Color fill_color, Color line_color, float line_thickness, int width, int height, int x, int y)
    {
        super(fill_color,
                255,
                line_color,
                line_thickness,
                255,
                width, height,
                x, y, Figure.TOPLEFT,0);
        polygon = new Polygon();
        int[] x_points = {get_render_location().x,
                get_render_location().x + get_width()/2,
                get_render_location().x + get_width()};
        polygon.xpoints = x_points;
        int[] y_points = {get_render_location().y + get_height(),
                get_render_location().y,
                get_render_location().y + get_height()};

        polygon.ypoints = y_points;
        polygon.npoints = 3;
        top = 50;

    }

    public void set_top_ratio(int ratio)
    {
        if(ratio > 100) this.top = 100;
        else if(ratio < 0) this.top = 0;
        else this.top = ratio;
    }

    public int get_top_ratio()
    {
        return top;
    }

    public void render(Graphics2D g) {

        g.rotate(Math.toRadians(get_rotate_angle()),
                get_render_location().x - get_width() / 2, get_render_location().y - get_height() / 2);

        int[] x_points = {get_render_location().x,
                get_render_location().x + get_width()/2,
        get_render_location().x + get_width()};

        int[] y_points = {get_render_location().y + get_height(),
                get_render_location().y,
                get_render_location().y + get_height()};

        Color color = new Color(
                get_fill_color().getRed(),
                get_fill_color().getGreen(),
                get_fill_color().getBlue(),
                get_fill_transparency());
        g.setColor(color);
        g.fillPolygon(polygon);


        g.setStroke(new BasicStroke(get_line_thickness()));

        Color line_color = new Color(
                get_line_color().getRed(),
                get_line_color().getGreen(),
                get_line_color().getBlue(),
                get_line_transparency());
        g.setColor(line_color);
        g.drawPolygon(polygon);
    }

    public void set_width(int width){
        super.set_width(width);

        int[] x_points = {get_render_location().x,
                get_render_location().x + get_width()/2,
                get_render_location().x + get_width()};
        polygon.xpoints = x_points;
    }

    public void set_height(int height){
        super.set_height(height);

        int[] y_points = {get_render_location().y + get_height(),
                get_render_location().y,
                get_render_location().y + get_height()};

        polygon.ypoints = y_points;
    }

    public void set_location(int x, int y){
        super.set_location(x, y);

        int[] x_points = {get_render_location().x,
                get_render_location().x + get_width()/2,
                get_render_location().x + get_width()};

        polygon.xpoints = x_points;

        int[] y_points = {get_render_location().y + get_height(),
                get_render_location().y,
                get_render_location().y + get_height()};

        polygon.ypoints = y_points;
    }

    public void set_location(Point p){
        super.set_location(p);

        int[] x_points = {get_location().x,
                get_location().x + get_width()/2,
                get_location().x + get_width()};

        polygon.xpoints = x_points;

        int[] y_points = {get_location().y + get_height(),
                get_location().y,
                get_location().y + get_height()};

        polygon.ypoints = y_points;
    }

    public boolean contains(Point p){
        int x1 = polygon.xpoints[0];
        int x2 = polygon.xpoints[1];
        int x3 = polygon.xpoints[2];

        int y1 = polygon.ypoints[0];
        int y2 = polygon.ypoints[1];
        int y3 = polygon.ypoints[2];
        /* Calculate area of triangle ABC */
        double A = area (x1, y1, x2, y2, x3, y3);

        /* Calculate area of triangle PBC */
        double A1 = area (p.x, p.y, x2, y2, x3, y3);

        /* Calculate area of triangle PAC */
        double A2 = area (x1, y1, p.x, p.y, x3, y3);

        /* Calculate area of triangle PAB */
        double A3 = area (x1, y1, x2, y2, p.x, p.y);

        /* Check if sum of A1, A2 and A3 is same as A */
        return (A == A1 + A2 + A3);
    }

    public boolean contains(int x, int y){
        return polygon.contains(x, y);
    }

    private double area(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        return Math.abs((x1*(y2-y3) + x2*(y3-y1)+ x3*(y1-y2))/2.0);
    }
}

