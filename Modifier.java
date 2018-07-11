import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class Modifier extends Figure {
    private Point pressPoint;
    private final int defaultCircleSize = 12;
    private Circle pressedCircle = null;
    public MouseMotionAdapter mouseDragHandler;
    public MouseAdapter mouseHandler;

    private static final int helper_line_thickness = 1;
    private Figure current;
    private Rectangle rectangle;
    private Circle TLC = new Circle(Color.WHITE,Color.BLACK,1, defaultCircleSize,defaultCircleSize,0,0);
    private Circle TCC = new Circle(Color.WHITE,Color.BLACK,1, defaultCircleSize,defaultCircleSize,0,0);
    private Circle TRC = new Circle(Color.WHITE,Color.BLACK,1, defaultCircleSize,defaultCircleSize,0,0);
    private Circle CLC = new Circle(Color.WHITE,Color.BLACK,1, defaultCircleSize,defaultCircleSize,0,0);
    private Circle CRC = new Circle(Color.WHITE,Color.BLACK,1, defaultCircleSize,defaultCircleSize,0,0);
    private Circle BLC = new Circle(Color.WHITE,Color.BLACK,1, defaultCircleSize,defaultCircleSize,0,0);
    private Circle BCC = new Circle(Color.WHITE,Color.BLACK,1, defaultCircleSize,defaultCircleSize,0,0);
    private Circle BRC = new Circle(Color.WHITE,Color.BLACK,1, defaultCircleSize,defaultCircleSize,0,0);
    private ArrayList<Circle> circles;
    private Point lastPoint;

    public Modifier(){
        super(Color.BLACK,
                1,255,
                0,0,
                0,0,TOPLEFT,0);
        this.rectangle = new Rectangle(0,0,0,0);
        circles = new ArrayList<>();
        circles.add(TLC);
        circles.add(TCC);
        circles.add(TRC);
        circles.add(CLC);
        circles.add(CRC);
        circles.add(BLC);
        circles.add(BCC);
        circles.add(BRC);
        mouseHandler = new mouseHandler();
        mouseDragHandler = new mouseDragHandler();
    }

    public Modifier(Figure current)
    {
        super(Color.BLACK,
                1,
                255,
                0,0,
                0,0,
                TOPLEFT,0);

        this.rectangle = new Rectangle(current.get_render_location().x,
                current.get_location().y,
                current.get_width(),
                current.get_height());
        mouseHandler = new mouseHandler();
        mouseDragHandler = new mouseDragHandler();
        this.current = current;
        circles = new ArrayList<>();

        //circles.add(TLC);
        circles.add(TCC);
        //circles.add(TRC);
        circles.add(CLC);
        circles.add(CRC);
        //circles.add(BLC);
        circles.add(BCC);
        //circles.add(BRC);
    }

    public void set_current(Figure current)
    {
        this.current = current;
    }

    public Figure get_current()
    {
        return current;
    }

    public void render(Graphics2D g)
    {
        if(current != null) {
            g.setStroke(new BasicStroke(Modifier.helper_line_thickness));

            if (get_current() instanceof Fillable_Figure) {
                g.setColor(get_line_color());
                rectangle.setBounds(current.get_render_location().x,
                        current.get_render_location().y,
                        current.get_width(),
                        current.get_height());
                g.draw(rectangle);
                updateCircles();
                for(Circle circle: circles){
                    circle.render(g);
                }

            } else {
                TLC.set_location(current.get_render_location().x - get_width() / 2,
                        current.get_render_location().y - get_height() / 2);
                TLC.render(g);

                BRC.set_location(current.get_render_location().x + current.get_width() - get_width() / 2,
                        current.get_render_location().y + current.get_height() - get_height() / 2);
                BRC.render(g);
            }
        }
    }

    public void updateCircles(){
        TLC.set_location(current.get_render_location().x - TLC.get_width() /2,
                current.get_render_location().y - TLC.get_height()/2);

        TCC.set_location(current.get_render_location().x + current.get_width() / 2 - TCC.get_width()/2,
                current.get_render_location().y - TCC.get_height() / 2);

        TRC.set_location(current.get_render_location().x +current.get_width() - TRC.get_width()/2,
                current.get_render_location().y - TRC.get_height() / 2);

        CLC.set_location(current.get_render_location().x - CLC.get_width() / 2,
                current.get_render_location().y + current.get_height() / 2 - CLC.get_height() / 2);

        CRC.set_location(current.get_render_location().x + current.get_width() - CRC.get_width() / 2,
                current.get_render_location().y + current.get_height() / 2 - CRC.get_height());

        BLC.set_location(current.get_render_location().x - BLC.get_width() / 2,
                current.get_render_location().y + current.get_height() - BLC.get_height() / 2);

        BCC.set_location(current.get_render_location().x + current.get_width() / 2 - BCC.get_width() / 2,
                current.get_render_location().y + current.get_height() - BCC.get_height() / 2);

        BRC.set_location(current.get_render_location().x + current.get_width() - BRC.get_width() / 2,
                current.get_render_location().y + current.get_height() - BRC.get_height() / 2);
    }

    public boolean contains(Point point){
        return false;
    }

    public class mouseHandler extends MouseAdapter{

        public mouseHandler(){}

        public void mousePressed(MouseEvent e){
            pressPoint = e.getPoint();
            pressedCircle = null;
            for(Circle circle: circles){
                if(circle.contains(pressPoint)){
                    pressedCircle = circle;
                }
            }
        }
    }



    public boolean circleContains(Point point){
        for(Circle circle: circles ) {
            if (circle.contains(point)) {
                return true;
            }
        }
        return false;
    }

    public class mouseDragHandler extends MouseMotionAdapter{
        private int dx;
        private int dy;
        public mouseDragHandler(){}

        public void mouseDragged(MouseEvent e) {
            if (pressedCircle != null) {
                lastPoint = current.get_location();
                dx = pressPoint.x - e.getX();
                dy = pressPoint.y - e.getY();
                pressPoint = e.getPoint();
                switch (circles.indexOf(pressedCircle)){
                    case 1://TCC
                        current.set_height(current.get_height() + dy);
                        current.set_X(lastPoint.x);
                        current.set_Y(e.getY());
                        break;
                    case 3://CLC
                       current.set_X(e.getX());
                        current.set_width(current.get_width() + dx);
                        break;
                }
            }
        }
    }
}
