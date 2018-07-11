import java.awt.*;

public abstract class Fillable_Figure extends Figure{
    private Color fill_color;
    private int fill_transparency;

    Fillable_Figure(Color fill_color,int fill_transparency,
                    Color line_color,
                    float line_thickness,
                    int line_transparency,
                    int width, int height,
                    int x, int y, int base,
                    float rotate_angle)
    {
        super(line_color,line_thickness,
                line_transparency,
                width, height,
                x, y, base,
                rotate_angle);
        this.fill_color = fill_color;
        this.fill_transparency = fill_transparency;
    }

    public Color get_fill_color()
    {
        return fill_color;
    }

    public int get_fill_transparency()
    {

        return fill_transparency;
    }

    public void set_fill_color(Color color)
    {
        this.fill_color = color;
    }

    public void set_fill_transparency(int transparency)
    {
        this.fill_transparency = transparency;
    }
}
