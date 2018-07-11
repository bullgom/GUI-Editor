import java.awt.*;
import java.awt.geom.Line2D;

public class Line extends Figure {
    private java.awt.geom.Line2D line;
    private Point endPoint;
    public Line(Color line_color, float line_thickness, int width, int height, int x, int y)
    {
        super(line_color,
                line_thickness,
                255,
                width, height,
                x, y,
                TOPLEFT, 0);
        endPoint = new Point(x,y);
        line = new Line2D.Double();
        line.setLine(x,y,endPoint.x,endPoint.y);
    }

    public void setEndPoint(Point point){
        endPoint = point;
        line.setLine(get_render_location(),endPoint);
    }

    public void setEndPoint(int x, int y){
        endPoint.x = x;
        endPoint.y= y;
        line.setLine(get_render_location(),endPoint);
    }

    public void render(Graphics2D g)
    {
        g.setStroke(new BasicStroke(get_line_thickness()));

        Color line_color = new Color(
                get_line_color().getRed(),
                get_line_color().getGreen(),
                get_line_color().getBlue(),
                get_line_transparency());
        g.setColor(line_color);

        g.drawLine(get_location().x,get_location().y,endPoint.x,endPoint.y);
    }

    public boolean contains(Point point){
        return line.contains(point);
    }
}
