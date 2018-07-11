import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.io.*;

public class MainFrame extends JFrame{
    private static final int FILLCOLORSELECTED = 0;
    private static final int LINECOLORSELECTED = 1;

    private boolean drawingNew = false;
    private static final int NONSELECTED = 0;
    private static final int RECT = 1;
    private static final int RRECT = 2;
    private static final int TRI = 3;
    private static final int CIR = 4;
    private static final int LINE = 5;
    private JPanel figureSelectPanel;
    private JPanel figurePropertyPanel;
    private Color panelBackgroundColor = Color.WHITE;
    private int chageColorMode = 0;
    private Figure current = null;
    private int optionPanelWidth = 320;
    private int optionPanelHeight = 400;
    private Dimension drawPanelSize = new Dimension(1280, 1280 * 9 / 16);
    private int selctedFigure = NONSELECTED;
    private Point pressedPoint;
    private ArrayList<Figure> figureArrayList = new ArrayList<>();
    private JPanel drawPanel;
    private ArrayList<Component> componentArrayList = new ArrayList<>();
    private Modifier modifier;
    private int dx, dy;

    public MainFrame()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500,1000);
        setTitle("GEdit");

        //Top Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        //TopMenu - SaveAs
        JMenuItem saveItem2 = new JMenuItem("Save As");
        saveItem2.setMnemonic('s');
        fileMenu.add(saveItem2);
        saveItem2.addActionListener(

                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        savefile();

                    }
                }
        );

        JMenuItem clear = new JMenuItem("Clear");
        clear.setMnemonic('c');
        fileMenu.add(clear);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        //TopMenu - Load File
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.setMnemonic('l');
        fileMenu.add(loadItem);
        loadItem.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        loadfile();

                    }
                }
        );
        //TopMenu - Exit
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('x');
        fileMenu.add(exitItem);
        exitItem.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        System.exit(0);

                    }
                }
        );

        //Make Top Menu
        JMenuBar topBar = new JMenuBar();
        setJMenuBar(topBar);
        topBar.add(fileMenu);

        JPanel OptionsPanel = new JPanel();
        OptionsPanel.setLayout(new CardLayout());
        drawPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                for(Figure figure:figureArrayList){
                    Graphics2D untrustedg2d = (Graphics2D)g.create();
                    figure.render(untrustedg2d);
                    untrustedg2d.dispose();
                }
                Graphics2D untrustedg2d = (Graphics2D)g.create();
                modifier.render(untrustedg2d);
                untrustedg2d.dispose();
            }
        };
        drawPanel.setSize(drawPanelSize);
        drawPanel.setBackground(Color.WHITE);
        drawPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        figureSelectPanel = new JPanel();
        figureSelectPanel.setLayout(new FlowLayout());
        figureSelectPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        modifier = new Modifier();

        //Add new figure buttons here
        ImageIcon rectImg = new ImageIcon("C:\\Users\\rladl\\Desktop\\ROOT\\Programing\\_Java\\GEditCombined\\src\\rectangle.png");
        JButton rectButton = new JButton(rectImg);
        rectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selctedFigure = RECT;
            }
        });
        ImageIcon circleImg = new ImageIcon("C:\\Users\\rladl\\Desktop\\ROOT\\Programing\\_Java\\GEditCombined\\src\\circle.png");
        JButton circleButton = new JButton(circleImg);
        circleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selctedFigure = CIR;
            }
        });

        ImageIcon triangleImg = new ImageIcon("C:\\Users\\rladl\\Desktop\\ROOT\\Programing\\_Java\\GEditCombined\\src\\triangle.png");
        JButton triangleButton = new JButton(triangleImg);
        triangleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selctedFigure = TRI;
            }
        });

        ImageIcon lineImg = new ImageIcon("C:\\Users\\rladl\\Desktop\\ROOT\\Programing\\_Java\\GEditCombined\\src\\line.png");
        JButton lineButton = new JButton(lineImg);
        lineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selctedFigure = LINE;
            }
        });
        //Add figure button behaviors here

        //Add figure buttons to panel here
        figureSelectPanel.add(rectButton);
        figureSelectPanel.add(triangleButton);
        figureSelectPanel.add(circleButton);
        figureSelectPanel.add(lineButton);
        figureSelectPanel.setBackground(Color.WHITE);



        //Figure propertiees
        figurePropertyPanel = new JPanel();
        figurePropertyPanel.setBorder(BorderFactory.createTitledBorder("도형 속성"));
        figurePropertyPanel.setBackground(Color.WHITE);
        figurePropertyPanel.setLayout(new BorderLayout());

        //Add new figure properties here
        JPanel sizeOptionPanel = new JPanel();
        sizeOptionPanel.setLayout(new GridLayout(0,3));
        sizeOptionPanel.setBackground(Color.WHITE);
        sizeOptionPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JButton sizeOptionButton = new JButton("크기");
        sizeOptionButton.setOpaque(false);
        sizeOptionButton.setContentAreaFilled(false);
        sizeOptionButton.setBorderPainted(false);
        sizeOptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sizeOptionPanel.setVisible(!sizeOptionPanel.isVisible());
            }
        });

        figurePropertyPanel.add(sizeOptionButton,"North");
        figurePropertyPanel.add(sizeOptionPanel,"North");

        //SIze properties
        JPanel heightOption = new JPanel();
        heightOption.setSize(optionPanelWidth,50);
        heightOption.setBackground(Color.WHITE);
        JLabel heightTextLabel = new JLabel("높이");
        JTextField heightLabel = new JTextField(heightTextGen());

        heightLabel.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                heightLabel.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (isInteger(heightLabel.getText())) {
                    if (current.is_width_height_ratio_fixed()) {
                        float ratio = current.get_width_height_ratio();
                        current.set_height(Integer.parseInt(heightLabel.getText()));
                        current.set_width((int) (current.get_height()
                                * ratio));
                    } else {
                        current.set_height(Integer.parseInt(heightLabel.getText()));
                    }
                }
            }
        });
        heightOption.setLayout(new FlowLayout());
        heightOption.add(heightTextLabel);
        heightOption.add(heightLabel);
        sizeOptionPanel.add(heightOption);

        JPanel widthOption = new JPanel();
        widthOption.setBackground(Color.WHITE);
        JLabel widthTextLabel = new JLabel("너비");
        JTextField widthLabel = new JTextField(widthTextGen());

        widthLabel.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                widthLabel.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (isInteger(widthLabel.getText())) {
                    if (current.is_width_height_ratio_fixed()) {
                        float ratio = current.get_width_height_ratio();
                        current.set_width(Integer.parseInt(widthLabel.getText()));
                        current.set_height((int)(current.get_width()
                                / ratio));
                    } else {
                        current.set_width(Integer.parseInt(widthLabel.getText()));
                    }
                }
            }
        });
        widthOption.setLayout(new FlowLayout());
        widthOption.add(widthTextLabel);
        widthOption.add(widthLabel);
        sizeOptionPanel.add(widthOption);

        JPanel rotateOption = new JPanel();
        rotateOption.setBackground(Color.WHITE);
        JLabel rotateTextLabel = new JLabel("회전");
        JTextField rotateLabel = new JTextField(rotateTextGen());

        rotateLabel.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                rotateLabel.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(!isInteger(rotateLabel.getText())){
                    rotateLabel.setText(rotateTextGen());
                }
                else {
                    current.set_rotate_angle(
                            Integer.parseInt(rotateLabel.getText()));
                }
            }
        });
        rotateOption.setLayout(new FlowLayout());
        rotateOption.add(rotateTextLabel);
        rotateOption.add(rotateLabel);
        sizeOptionPanel.add(rotateOption);

        JPanel ratioOption = new JPanel();
        ratioOption.setBackground(Color.WHITE);
        JCheckBox ratioFixOptionBox = new JCheckBox("Height width ratio fixed",ratioFixedGen());
        ratioFixOptionBox.setBackground(Color.WHITE);
        ratioFixOptionBox.setEnabled(false);
        ratioFixOptionBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    current.set_width_height_ratio_fixed(true);
                }
                else{
                    current.set_width_height_ratio_fixed(false);
                }
            }
        });
        ratioOption.add(ratioFixOptionBox);
        //sizeOptionPanel.add(ratioOption);

        JPanel locationOptionPanel = new JPanel();
        locationOptionPanel.setBackground(Color.WHITE);
        locationOptionPanel.setLayout(new GridLayout(0,1));
        locationOptionPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton locationOptionButton = new JButton("위치");
        locationOptionButton.setOpaque(false);
        locationOptionButton.setBorderPainted(false);
        locationOptionButton.setContentAreaFilled(false);
        locationOptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                locationOptionPanel.setVisible(!locationOptionPanel.isVisible());
            }
        });

        figurePropertyPanel.add(locationOptionButton,"North");
        figurePropertyPanel.add(locationOptionPanel,"North");

        JPanel xOption = new JPanel();
        xOption.setBackground(Color.WHITE);
        JLabel xTextLabel = new JLabel("x축 위치");
        JTextField xLabel = new JTextField(xTextGen());

        xLabel.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                xLabel.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(!isInteger(xLabel.getText())){
                    xLabel.setText(xTextGen());
                }
                else {
                    current.set_location(
                            Integer.parseInt(xLabel.getText()),
                            current.get_location().y);
                }
            }


        });
        xOption.setLayout(new FlowLayout());
        xOption.add(xTextLabel);
        xOption.add(xLabel);


        JPanel yOption = new JPanel();
        yOption.setBackground(Color.WHITE);
        JLabel yTextLabel = new JLabel("y축 위치");
        JTextField yLabel = new JTextField(yTextGen());

        yLabel.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                yLabel.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!isInteger(yLabel.getText())) {
                    yLabel.setText(yTextGen());
                } else {
                    current.set_location(
                            Integer.parseInt(yLabel.getText()),
                            current.get_location().y);
                }
            }
        });
        yOption.setLayout(new FlowLayout());
        yOption.add(yTextLabel);
        yOption.add(yLabel);

        locationOptionPanel.add(xOption);
        locationOptionPanel.add(yOption);

        JPanel colorOption = new JPanel();
        colorOption.setLayout(new GridLayout(3,2));
        colorOption.setBackground(Color.WHITE);
        colorOption.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JColorChooser fillColorChooser = new JColorChooser();
        fillColorChooser.setPreviewPanel(new JPanel());
        fillColorChooser.setColor(fillColorGen());

        JColorChooser lineColorChooser = new JColorChooser();
        lineColorChooser.setPreviewPanel(new JPanel());
        lineColorChooser.setColor(lineColorGen());

        JButton fillColorButton = new JButton("채우기 색");
        fillColorButton.setIcon(new MetalComboBoxIcon());
        fillColorButton.setHorizontalTextPosition(SwingConstants.LEFT);

        JButton lineColorButton = new JButton("선 색");
        lineColorButton.setIcon(new MetalComboBoxIcon());
        lineColorButton.setHorizontalTextPosition(SwingConstants.LEFT);

        JLabel fillPreview = new JLabel(" ");
        fillPreview.setSize(10,10);
        fillPreview.setForeground(Color.GRAY);

        JLabel linePreview = new JLabel(" ");
        linePreview.setSize(10,10);
        linePreview.setForeground(Color.GRAY);

        fillColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null,"채우기 색상",Color.GRAY);
                if(current instanceof Fillable_Figure){
                    ((Fillable_Figure) current).set_fill_color(newColor);
                    fillPreview.setForeground(newColor);
                    repaint();
                }
            }
        });

        lineColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(fillColorChooser,"채우기 색상",Color.GRAY);
                if(current != null) {
                    current.set_line_color(newColor);
                    linePreview.setForeground(newColor);
                    repaint();
                }
            }
        });

        JPanel fillTransparencyOption = new JPanel();
        fillTransparencyOption.setBackground(Color.WHITE);
        JLabel fillTransparencyLabel = new JLabel("투명도");
        JTextField fillTransparencyTextLabel = new JTextField(rotateTextGen());

        fillTransparencyTextLabel.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                fillTransparencyTextLabel.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(!isInteger(fillTransparencyTextLabel.getText())){
                    fillTransparencyTextLabel.setText(fillTransparencyTextGen());
                }
                else if(current instanceof Fillable_Figure){
                    ((Fillable_Figure)current).set_fill_transparency(
                            Integer.parseInt(fillTransparencyTextLabel.getText()));
                }
            }
        });

        fillTransparencyOption.setLayout(new FlowLayout());
        fillTransparencyOption.add(fillTransparencyLabel);
        fillTransparencyOption.add(fillTransparencyTextLabel);

        JPanel lineTransparencyOption = new JPanel();
        lineTransparencyOption.setBackground(Color.WHITE);
        JLabel lineTransparencyLabel = new JLabel("투명도");
        JTextField lineTransparencyTextLabel = new JTextField(rotateTextGen());

        lineTransparencyTextLabel.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                lineTransparencyTextLabel.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(!isInteger(lineTransparencyTextLabel.getText())){
                    lineTransparencyTextLabel.setText(lineTransparencyTextGen());
                }
                else {
                    current.set_line_transparency(
                            Integer.parseInt(lineTransparencyTextLabel.getText()));
                }
            }
        });

        lineTransparencyOption.setLayout(new FlowLayout());
        lineTransparencyOption.add(lineTransparencyLabel);
        lineTransparencyOption.add(lineTransparencyTextLabel);


        colorOption.add(fillColorButton);
        colorOption.add(fillTransparencyOption);
        colorOption.add(lineColorButton);
        colorOption.add(lineTransparencyOption);

        final String FIGURE = "도형";
        final String SIZE = "크기";
        final String LOCATION = "위치";
        final String COLOR = "색";
        String[] cards = {FIGURE,SIZE,LOCATION,COLOR};

        OptionsPanel.add(figureSelectPanel, FIGURE);
        OptionsPanel.add(sizeOptionPanel,SIZE);
        OptionsPanel.add(locationOptionPanel,LOCATION);
        OptionsPanel.add(colorOption,COLOR);

        JPanel optionsPanel = new JPanel();
        JPanel optionSelectPanel = new JPanel();
        optionSelectPanel.setLayout(new GridLayout(0,4));

        JButton figureButton = new JButton("도형");
        JButton sizeButton = new JButton("크기");
        JButton locatonButton = new JButton("위치");
        JButton colorButton = new JButton("색");

        figureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout)OptionsPanel.getLayout();
                cardLayout.show(OptionsPanel,FIGURE);
            }
        });
        sizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout)OptionsPanel.getLayout();
                cardLayout.show(OptionsPanel,SIZE);
            }
        });
        locatonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout)OptionsPanel.getLayout();
                cardLayout.show(OptionsPanel,LOCATION);
            }
        });
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout)OptionsPanel.getLayout();
                cardLayout.show(OptionsPanel,COLOR);
            }
        });

        optionSelectPanel.add(figureButton);
        figureButton.setOpaque(false);
        figureButton.setContentAreaFilled(false);
        optionSelectPanel.add(sizeButton);
        sizeButton.setOpaque(false);
        sizeButton.setContentAreaFilled(false);
        optionSelectPanel.add(locatonButton);
        locatonButton.setOpaque(false);
        locatonButton.setContentAreaFilled(false);
        optionSelectPanel.add(colorButton);
        colorButton.setOpaque(false);
        colorButton.setContentAreaFilled(false);

        optionSelectPanel.setBackground(Color.WHITE);
        optionSelectPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        optionsPanel.setLayout(new BorderLayout());
        optionsPanel.setSize(330,150);

        optionsPanel.add(optionSelectPanel);

        optionsPanel.add(optionSelectPanel,"North");
        optionsPanel.add(OptionsPanel,"Center");

        ComponentMover mover = new ComponentMover(optionsPanel,optionsPanel);

        setLayout(null);
        getContentPane().setBackground(new Color(200,200,200));
        add(optionsPanel);
        add(drawPanel);

        heightLabel.setEnabled(false);
        componentArrayList.add(heightLabel);
        widthLabel.setEnabled(false);
        componentArrayList.add(widthLabel);
        rotateLabel.setEnabled(false);
        componentArrayList.add(rotateLabel);
        ratioFixOptionBox.setEnabled(false);
        componentArrayList.add(ratioFixOptionBox);
        xLabel.setEnabled(false);
        componentArrayList.add(xLabel);
        yLabel.setEnabled(false);
        componentArrayList.add(yLabel);
        fillColorButton.setEnabled(false);
        componentArrayList.add(fillColorButton);
        lineColorButton.setEnabled(false);
        componentArrayList.add(lineColorButton);
        fillTransparencyTextLabel.setEnabled(false);
        componentArrayList.add(fillTransparencyTextLabel);
        lineTransparencyTextLabel.setEnabled(false);
        componentArrayList.add(lineTransparencyTextLabel);

        drawPanel.addMouseMotionListener(new drawPanelMotionHandler(modifier));
        drawPanel.addMouseListener(new drawPanelHandler(modifier));
        drawPanel.addMouseListener(modifier.mouseHandler);
        drawPanel.addMouseMotionListener(modifier.mouseDragHandler);
        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(current != null){
                    heightLabel.setText(heightTextGen());
                    widthLabel.setText(widthTextGen());
                    rotateLabel.setText(rotateTextGen());
                    xLabel.setText(xTextGen());
                    yLabel.setText(yTextGen());
                    fillTransparencyTextLabel.setText(fillTransparencyTextGen());
                    lineTransparencyTextLabel.setText(lineTransparencyTextGen());
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newHeight = getContentPane().getHeight();
                int newWidth = getContentPane().getWidth();
                optionsPanel.setLocation(0,0);
                drawPanel.setLocation((newWidth - drawPanel.getWidth()) / 2, (newHeight - drawPanel.getHeight()) / 2);
            }
        });

        JButton RefreshButtonL = new JButton("확인");
        locationOptionPanel.add(RefreshButtonL);
        RefreshButtonL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isInteger(heightLabel.getText())) {
                    if(current.is_width_height_ratio_fixed()){
                        float ratio = current.get_width_height_ratio();
                        current.set_height(Integer.parseInt(heightLabel.getText()));
                        current.set_width((int)(current.get_height()
                                * ratio));
                    }else {
                        current.set_height(Integer.parseInt(heightLabel.getText()));
                    }
                }
                if (isInteger(widthLabel.getText())) {
                    if (current.is_width_height_ratio_fixed()) {
                        float ratio = current.get_width_height_ratio();
                        current.set_width(Integer.parseInt(widthLabel.getText()));
                        current.set_height((int)(current.get_width()
                                / ratio));
                    } else {
                        current.set_width(Integer.parseInt(widthLabel.getText()));
                    }
                }
                if (isInteger(rotateLabel.getText())) {
                    current.set_rotate_angle(Integer.parseInt(rotateLabel.getText()));
                }
                if (ratioFixOptionBox.isSelected()) {
                    current.set_width_height_ratio_fixed(true);
                } else {
                    current.set_width_height_ratio_fixed(false);
                }
                if (isInteger(xLabel.getText())) {
                    current.set_location(Integer.parseInt(xLabel.getText()), current.get_location().y);
                }
                if (isInteger(yLabel.getText())) {
                    current.set_location(current.get_location().x, Integer.parseInt(yLabel.getText()));
                }
                if (isInteger(fillTransparencyLabel.getText())) {
                    if (current instanceof Fillable_Figure) {
                        ((Fillable_Figure) current).set_fill_transparency(Integer.parseInt(fillTransparencyLabel.getText()));
                    }
                }
                if (isInteger(lineTransparencyLabel.getText())) {
                    current.set_line_transparency(Integer.parseInt(lineTransparencyLabel.getText()));
                }
                repaint();
            }
        });

        JButton RefreshButtonS = new JButton("확인");
        sizeOptionPanel.add(RefreshButtonS);
        RefreshButtonS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isInteger(heightLabel.getText())) {
                    if(current.is_width_height_ratio_fixed()){
                        float ratio = current.get_width_height_ratio();
                        current.set_height(Integer.parseInt(heightLabel.getText()));
                        current.set_width((int)(current.get_height()
                                * ratio));
                    }else {
                        current.set_height(Integer.parseInt(heightLabel.getText()));
                    }
                }
                if (isInteger(widthLabel.getText())) {
                    if (current.is_width_height_ratio_fixed()) {
                        float ratio = current.get_width_height_ratio();
                        current.set_width(Integer.parseInt(widthLabel.getText()));
                        current.set_height((int)(current.get_width()
                                / ratio));
                    } else {
                        current.set_width(Integer.parseInt(widthLabel.getText()));
                    }
                }
                if (isInteger(rotateLabel.getText())) {
                    current.set_rotate_angle(Integer.parseInt(rotateLabel.getText()));
                }
                if (ratioFixOptionBox.isSelected()) {
                    current.set_width_height_ratio_fixed(true);
                } else {
                    current.set_width_height_ratio_fixed(false);
                }
                if (isInteger(xLabel.getText())) {
                    current.set_location(Integer.parseInt(xLabel.getText()), current.get_location().y);
                }
                if (isInteger(yLabel.getText())) {
                    current.set_location(current.get_location().x, Integer.parseInt(yLabel.getText()));
                }
                if (isInteger(fillTransparencyLabel.getText())) {
                    if (current instanceof Fillable_Figure) {
                        ((Fillable_Figure) current).set_fill_transparency(Integer.parseInt(fillTransparencyLabel.getText()));
                    }
                }
                if (isInteger(lineTransparencyLabel.getText())) {
                    current.set_line_transparency(Integer.parseInt(lineTransparencyLabel.getText()));
                }
                repaint();
            }
        });

        JButton RefreshButtonC = new JButton("확인");
        colorOption.add(RefreshButtonC);
        RefreshButtonC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isInteger(heightLabel.getText())) {
                    if(current.is_width_height_ratio_fixed()){
                        float ratio = current.get_width_height_ratio();
                        current.set_height(Integer.parseInt(heightLabel.getText()));
                        current.set_width((int)(current.get_height()
                                * ratio));
                    }else {
                        current.set_height(Integer.parseInt(heightLabel.getText()));
                    }
                }
                if (isInteger(widthLabel.getText())) {
                    if (current.is_width_height_ratio_fixed()) {
                        float ratio = current.get_width_height_ratio();
                        current.set_width(Integer.parseInt(widthLabel.getText()));
                        current.set_height((int)(current.get_width()
                                / ratio));
                    } else {
                        current.set_width(Integer.parseInt(widthLabel.getText()));
                    }
                }
                if (isInteger(rotateLabel.getText())) {
                    current.set_rotate_angle(Integer.parseInt(rotateLabel.getText()));
                }
                if (ratioFixOptionBox.isSelected()) {
                    current.set_width_height_ratio_fixed(true);
                } else {
                    current.set_width_height_ratio_fixed(false);
                }
                if (isInteger(xLabel.getText())) {
                    current.set_location(Integer.parseInt(xLabel.getText()), current.get_location().y);
                }
                if (isInteger(yLabel.getText())) {
                    current.set_location(current.get_location().x, Integer.parseInt(yLabel.getText()));
                }
                if (isInteger(fillTransparencyLabel.getText())) {
                    if (current instanceof Fillable_Figure) {
                        ((Fillable_Figure) current).set_fill_transparency(Integer.parseInt(fillTransparencyLabel.getText()));
                    }
                }
                if (isInteger(lineTransparencyLabel.getText())) {
                    current.set_line_transparency(Integer.parseInt(lineTransparencyLabel.getText()));
                }
                repaint();
            }
        });


    }

    private class drawPanelHandler extends MouseAdapter{
        private Modifier modifier;
        private drawPanelHandler(Modifier modifier){
            super();

            this.modifier = modifier;
        }

        public void mousePressed(MouseEvent e) {
            pressedPoint = e.getPoint();

            dx = 0;
            dy = 0;

            if(selctedFigure != NONSELECTED){
                drawingNew = true;
                switch (selctedFigure){
                    case RECT:
                        current = new Rectangle(Color.CYAN,Color.GRAY,
                                2,0,0,
                                pressedPoint.x, pressedPoint.y);
                        break;
                    case TRI:
                        current = new Triangle(Color.CYAN, Color.GRAY,
                                2,0,0,
                                pressedPoint.x, pressedPoint.y);
                        break;
                    case CIR:
                        current = new Circle(Color.CYAN, Color.GRAY,
                                2, 0,0,
                                pressedPoint.x, pressedPoint.y);
                        break;
                    case LINE:
                        current = new Line(Color.GRAY,2,
                                0,0, pressedPoint.x, pressedPoint.y);
                        break;
                }
                figureArrayList.add(current);
            }else {
                current = null;
                modifier.set_current(null);
                disableComponents(componentArrayList);

                for (int i = figureArrayList.size() - 1; i >= 0; --i) {
                    if (figureArrayList.get(i).contains(pressedPoint)) {
                        current = figureArrayList.get(i);
                        enableComponents(componentArrayList);
                        modifier.set_current(current);
                        dx = current.get_location().x - pressedPoint.x;
                        dy = current.get_location().y - pressedPoint.y;
                        break;
                    }
                    modifier.set_current(figureArrayList.get(i));
                    modifier.updateCircles();
                    if(modifier.circleContains(e.getPoint())){
                        current = figureArrayList.get(i);
                        enableComponents(componentArrayList);
                        modifier.set_current(current);
                        dx = current.get_location().x - pressedPoint.x;
                        dy = current.get_location().y - pressedPoint.y;
                        break;
                    }
                    modifier.set_current(null);
                }

            }
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            selctedFigure = NONSELECTED;
            drawingNew = false;
            repaint();
        }

        public void mouseDragged(MouseEvent e){
            System.out.print("check");
            repaint();
        }
    }

    private void enableComponents(ArrayList<Component> components){
        for(Component component: components){
            component.setEnabled(true);
        }
    }

    private void disableComponents(ArrayList<Component> components){
        for(Component component: components){
            component.setEnabled(false);
        }
    }

    private class drawPanelMotionHandler extends MouseMotionAdapter{
        private Modifier modifier;
        private drawPanelMotionHandler(Modifier modifier){
            super();
            this.modifier = modifier;
        }

        public void mouseDragged(MouseEvent e) {
            if(drawingNew){
                if(current instanceof Fillable_Figure) {
                    switch (getSide(pressedPoint, e.getPoint())) {
                        case 0:
                            current.set_width(Math.abs(e.getX() - pressedPoint.x));
                            current.set_height(Math.abs(e.getY() - pressedPoint.y));
                            break;
                        case 1:
                            current.set_location(pressedPoint.x, e.getY());
                            current.set_width(Math.abs(e.getX() - pressedPoint.x));
                            current.set_height(Math.abs(e.getY() - pressedPoint.y));
                            break;
                        case 2:
                            current.set_location(e.getPoint());
                            current.set_width(Math.abs(pressedPoint.x - e.getX()));
                            current.set_height(Math.abs(pressedPoint.y - e.getY()));
                            break;
                        case 3:
                            current.set_location(e.getX(),pressedPoint.y);
                            current.set_width(Math.abs(pressedPoint.x - e.getX()));
                            current.set_height(Math.abs(pressedPoint.y - e.getY()));
                            break;
                    }
                }else{
                    ((Line) current).setEndPoint(e.getPoint());
                }
            }else {
                if (current != null) {
                    current.set_location(e.getX() + dx, e.getY() + dy);
                }
            }
            repaint();
        }
    }

    private boolean isInteger(String input)
    {
        try{
            Integer.parseInt(input);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    private String heightTextGen()
    {
        if(current == null) return "No Current Figure";
        else return String.format("%d",current.get_height());
    }

    private String widthTextGen()
    {
        if(current == null) return "No Current Figure";
        else return String.format("%d",current.get_width());
    }

    private String rotateTextGen()
    {
        if(current == null) return "No Current Figure";
        else return String.format("%.2f",current.get_rotate_angle());
    }

    private boolean ratioFixedGen()
    {
        if(current == null) return false;
        else return current.is_width_height_ratio_fixed();
    }

    private String xTextGen()
    {
        if(current == null) return "No Current Figure";
        else return String.format("%d",current.get_location().x);
    }

    private String yTextGen()
    {
        if(current == null) return "No Current Figure";
        else return String.format("%d",current.get_location().y);
    }

    private Color fillColorGen()
    {
        if(current == null) return Color.BLACK;
        else if(current instanceof Fillable_Figure){
            return ((Fillable_Figure) current).get_fill_color();
        }
        else return Color.BLACK;
    }

    private Color lineColorGen()
    {
        if(current == null) return Color.BLACK;
        else return current.get_line_color();
    }

    private String  lineTransparencyTextGen(){
        if(current == null) return "No Current Figure";
        else return String.format("%d",current.get_line_transparency());
    }

    private String fillTransparencyTextGen(){
        if(current == null) return "No Current Figure";
        else if(current instanceof Fillable_Figure) return String.format("%d",((Fillable_Figure) current).get_fill_transparency());
        else return String.format("%d",current.get_line_transparency());
    }

    private int getSide(Point pressedPoint, Point currentPoint){
        if(pressedPoint.x < currentPoint.x){
            if(pressedPoint.y < currentPoint.y){
                return 0;
            }else{
                return 1;
            }
        }
        else{
            if(pressedPoint.y < currentPoint.y){
                return 3;
            }else{
                return 2;
            }
        }
    }

    private void savefile()
    {
        try{
            File f = new File("./figureFile.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            ObjectOutputStream o = new ObjectOutputStream(fileOutputStream);
            o.writeObject(figureArrayList);
            o.close();

        }catch (Exception ex){}
    }

    private void loadfile() {
        try {
            File file = new File("./figureFile.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);

            ArrayList<Figure> figures = (ArrayList<Figure>)objectInputStream.readObject();
            figureArrayList.addAll(figures);
            repaint();
            objectInputStream.close();

        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    private void clear(){
        figureArrayList.removeAll(figureArrayList);
        for(Figure figure: figureArrayList){
            figureArrayList.remove(figure);
        }
        repaint();
    }
}
