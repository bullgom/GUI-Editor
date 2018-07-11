import java.awt.*;
import java.io.Serializable;

public abstract class Figure implements Serializable
{
    public static final int TOPLEFT = 0;
    public static final int TOPCENTER = 1;
    public static final int TOPRIGHT = 2;
    public static final int CENTERLEFT = 3;
    public static final int CENTER = 4;
    public static final int CENTERRIGHT = 5;
    public static final int BOTTOMLEFT = 6;
    public static final int BOTTOMCENTER = 7;
    public static final int BOTTOMRIGHT = 8;

    private Color line_color;
    private float line_thickness;
    private int line_transparency;

    private Point location;
    private int base;
    private int width;
    private int height;
    private float rotate_angle;

    private boolean width_height_ratio_fixed;

    public Figure(Color line_color,
                  float line_thickness,
                  int line_transparency,
                  int width, int height,
                  int x, int y, int base,
                  float rotate_angle)
    {

        this.line_color = line_color;
        this.line_thickness = line_thickness;
        this.line_transparency = line_transparency;
        this.width = width;
        this.height = height;
        this.rotate_angle = rotate_angle;
        this.width_height_ratio_fixed = false;
        this.location = new Point(x,y);
        this.base = base;
    }



    public Color get_line_color()
    {
        return line_color;
    }

    public float get_line_thickness()
    {
        return line_thickness;
    }

    public int get_line_transparency()
    {
        return line_transparency;
    }

    public int get_width()
    {
        return width;
    }

    public int get_height()
    {
        return height;
    }

    public float get_rotate_angle()
    {
        return rotate_angle;
    }

    public boolean is_width_height_ratio_fixed()
    {
        return width_height_ratio_fixed;
    }

    public Point get_location()
    {
        return location;
    }

    public Point get_render_location()
    {
        int x, y;

        switch(base)
        {
            case TOPLEFT:
                x = location.x;
                y = location.y;
                break;
            case TOPCENTER:
                x = location.x - (int)Math.round(width / 2);
                y = location.y;
                break;
            case TOPRIGHT:
                x = location.x - width;
                y = location.y;
                break;
            case CENTERLEFT:
                x = location.x;
                y = location.y - (int)Math.round(height / 2);
                break;
            case CENTER:
                x = location.x - (int)Math.round(width / 2);
                y = location.y - (int)Math.round(height / 2);
                break;
            case CENTERRIGHT:
                x = location.x - width;
                y = location.y - (int)Math.round(height / 2);
                break;
            case BOTTOMLEFT:
                x = location.x;
                y = location.y - height;
                break;
            case BOTTOMCENTER:
                x = location.x - (int)Math.round(width / 2);
                y = location.y - height;
                break;
            case BOTTOMRIGHT:
                x = location.x - width;
                y = location.y - height;
                break;
            default:
                x = 0; y = 0;
                break;
        }

        return new Point(x, y);
    }

    public int get_base()
    {
        return base;
    }

    public void set_line_color(Color color)
    {
        this.line_color = color;
    }

    public void set_line_transparency(int transparency)
    {
        this.line_transparency = transparency;
    }

    public void set_width(int width)
    {
        if(this.width_height_ratio_fixed)
        {
            float ratio = this.height / this.width;
            this.width = width;
            this.height = (int)Math.round(width * ratio);
        }
        else { this.width = width;}
    }

    public void set_height(int height)
    {
        if(this.width_height_ratio_fixed)
        {
            float ratio = this.width / this.height;
            this.height = height;
            this.width = (int)Math.round(height * ratio);
        }
        else{ this.height = height;}
    }

    public void set_X(int x){
        location.x= x;
    }

    public void set_Y(int y){
        location.y = y;
    }

    public void set_rotate_angle(float angle)
    {
        if(angle > 360)
        {
            this.rotate_angle = angle % 360;
        }
        else this.rotate_angle = angle;
    }

    public void set_location(int x, int y)
    {
        this.location.setLocation(x, y);
    }

    public void set_location(Point location)
    {
        this.location = location;
    }

    public void set_base(int base)
    {
        this.base = base;
    }

    public void set_width_height_ratio_fixed(boolean option)
    {this.width_height_ratio_fixed = option;}

    public float get_width_height_ratio()
    {
        return width / height;
    }

    abstract public void render(Graphics2D g);

    abstract public boolean contains(Point p);
}
