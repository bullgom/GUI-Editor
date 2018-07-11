import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.*;
import javax.swing.*;

/**
 *  This class allows you to move a Component by using a mouse. The Component
 *  moved can be a high level Window (ie. Window, Frame, Dialog) in which case
 *  the Window is moved within the desktop. Or the Component can belong to a
 *  Container in which case the Component is moved within the Container.
 *
 *  When moving a Window, the listener can be added to a child Component of
 *  the Window. In this case attempting to move the child will result in the
 *  Window moving. For example, you might create a custom "Title Bar" for an
 *  undecorated Window and moving of the Window is accomplished by moving the
 *  title bar only. Multiple components can be registered as "window movers".
 *
 *  Components can be registered when the class is created. Additional
 *  components can be added at any time using the registerComponent() method.
 */
public class ComponentMover extends MouseAdapter
{
    private Insets dragInsets = new Insets(0, 0, 0, 0);
    private Dimension snapSize = new Dimension(1, 1);
    private Insets edgeInsets = new Insets(0, 0, 0, 0);
    private boolean changeCursor = true;
    private boolean autoLayout = false;

    private Class destinationClass;
    private Component destinationComponent;
    private Component destination;
    private Component source;

    private Point pressed;
    private Point location;

    private Cursor originalCursor;
    private boolean autoscrolls;
    private boolean potentialDrag;


    /**
     *  Constructor for moving individual components. The components must be
     *  regisetered using the registerComponent() method.
     */
    public ComponentMover()
    {
    }

    public ComponentMover(Class destinationClass, Component... components)
    {
        this.destinationClass = destinationClass;
        registerComponent( components );
    }

    public ComponentMover(Component destinationComponent, Component... components)
    {
        this.destinationComponent = destinationComponent;
        registerComponent( components );
    }

    public boolean isAutoLayout()
    {
        return autoLayout;
    }

    public void setAutoLayout(boolean autoLayout)
    {
        this.autoLayout = autoLayout;
    }

    public boolean isChangeCursor()
    {
        return changeCursor;
    }

    public void setChangeCursor(boolean changeCursor)
    {
        this.changeCursor = changeCursor;
    }

    public Insets getDragInsets()
    {
        return dragInsets;
    }

    public void setDragInsets(Insets dragInsets)
    {
        this.dragInsets = dragInsets;
    }

    public Insets getEdgeInsets()
    {
        return edgeInsets;
    }

    public void setEdgeInsets(Insets edgeInsets)
    {
        this.edgeInsets = edgeInsets;
    }

    public void deregisterComponent(Component... components)
    {
        for (Component component : components)
            component.removeMouseListener( this );
    }

    public void registerComponent(Component... components)
    {
        for (Component component : components)
            component.addMouseListener( this );
    }

    public Dimension getSnapSize()
    {
        return snapSize;
    }

    public void setSnapSize(Dimension snapSize)
    {
        if(snapSize.width < 1 || snapSize.height < 1)
            throw new IllegalArgumentException("Snap sizes must be greater than 0");

        this.snapSize = snapSize;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        source = e.getComponent();
        int width = source.getSize().width - dragInsets.left - dragInsets.right;
        int height = source.getSize().height - dragInsets.top - dragInsets.bottom;

        Rectangle rectangle = new Rectangle(dragInsets.left,dragInsets.top,
                width,height);

        if(rectangle.contains(e.getPoint()))
            setUpForDragging(e);
    }

    private void setUpForDragging(MouseEvent e) {
        source.addMouseMotionListener(this);
        potentialDrag = true;

        if (destinationComponent != null) {
            destination = destinationComponent;
        } else if (destinationClass == null) {
            destination = source;
        }
        else{
            destination = SwingUtilities.getAncestorOfClass(destinationClass, source);
        }

        pressed = e.getLocationOnScreen();
        location = destination.getLocation();

        if(changeCursor)
        {
            originalCursor = source.getCursor();
            source.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }

        if(destination instanceof JComponent)
        {
            JComponent jComponent = (JComponent)destination;
            autoscrolls = jComponent.getAutoscrolls();
            jComponent.setAutoscrolls(false);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        Point dragged = e.getLocationOnScreen();
        int dragX = getDragDistance(dragged.x, pressed.x, snapSize.width);
        int dragY = getDragDistance(dragged.y, pressed.y, snapSize.height);

        int locationX = location.x + dragX;
        int locationY = location.y + dragY;

        while(locationX < edgeInsets.left)
            locationX += snapSize.width;

        while(locationY < edgeInsets.top)
            locationY += snapSize.height;

        Dimension dimension = getBoundingSize(destination);

        while(locationX + destination.getSize().width + edgeInsets.right > dimension.width)
            locationX -= snapSize.width;

        while(locationY + destination.getSize().height + edgeInsets.bottom > dimension.height)
            locationY -= snapSize.height;

        destination.setLocation(locationX, locationY);
    }

    private int getDragDistance(int larger, int smaller, int snapSize)
    {
        int halfway = snapSize / 2;
        int drag = larger - smaller;
        drag += (drag < 0 ) ? -halfway : halfway;
        drag = (drag / snapSize) * snapSize;

        return drag;
    }

    private Dimension getBoundingSize(Component source)
    {
        if(source instanceof Window)
        {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle bounds = env.getMaximumWindowBounds();
            return new Dimension(bounds.width, bounds.height);
        }
        else
        {
            return source.getParent().getSize();
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        if(!potentialDrag) return;

        source.removeMouseListener(this);
        potentialDrag = false;

        if(changeCursor) source.setCursor(originalCursor);

        if(destination instanceof JComponent)
        {
            ((JComponent)destination).setAutoscrolls(autoscrolls);
        }
        else
        {
            destination.validate();
        }
    }
}