
import myutil.TrajectoryDrawer;
import org.jzy3d.chart.Chart;
import org.jzy3d.plot3d.rendering.canvas.ICanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class BaseForm extends JFrame {

    private JPanel mainPanel = new JPanel();
    private JPanel controlPanel = new JPanel();
    private JButton drawButton = new JButton("Нарисовать график");

    private JTextField bVectorField = new JTextField();
    private JTextField eVectorField = new JTextField();
    private JTextField qChargeField = new JTextField();
    private JTextField pMassField = new JTextField();
    private JTextField pVelocityField = new JTextField();
    private JTextField timeStepField = new JTextField();

    private JLabel bVectorLabel = new JLabel("Индуктивность B: i, j, k");
    private JLabel eVectorLabel = new JLabel("Напряженность E: i, j, k");
    private JLabel qChargeLabel = new JLabel("Q (заряд частицы), (нКл)");
    private JLabel pMassLabel = new JLabel("Масса частицы, (кг): ");
    private JLabel pVelocityLabel = new JLabel("Начальная скорость частицы: i, j, k (м/с)");
    private JLabel timeStepLabel = new JLabel("Время, (с)");
    private JLabel warningLabel = new JLabel("<HTML> Вводите дробные числа через точку! Например '45.68' </HTML>");

    //I know I could use ArrayList instead of naked arrays, but it seems like an overkill for this one
    private int preferredVectorLength = 3;
    private double[] bVector = new double[preferredVectorLength];
    private double[] eVector = new double[preferredVectorLength];
    private double[] pVelocity = new double[preferredVectorLength];
    private double qCharge;
    private double pMass;
    private double timeStep;

    public class DrawActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            String bvalues = bVectorField.getText().replaceAll("\\s", "");
            String evalues = eVectorField.getText().replaceAll("\\s", "");
            String vvalues = pVelocityField.getText().replaceAll("\\s", "");
            String[] strBvalues = bvalues.split(",");
            String[] strEvalues = evalues.split(",");
            String[] strVvalues = vvalues.split(",");
            for (int i = 0; i < preferredVectorLength; i++) {
                try {
                    bVector[i] = Double.parseDouble(strBvalues[i]);
                    eVector[i] = Double.parseDouble(strEvalues[i]);
                    pVelocity[i] = Double.parseDouble(strVvalues[i]);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(mainPanel,
                            "Введите вектор в верном формате 'x,y,z' (например: 1,1,1)");
                    return;
                }
            }

            try {
                qCharge = Double.parseDouble(qChargeField.getText());
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(mainPanel, "Неверный формат числа заряда");
                return;
            }
            try {
                pMass = Double.parseDouble(pMassField.getText());
                if (pMass <= 0) {
                    JOptionPane.showMessageDialog(mainPanel, "Некорреткное значение массы. Масса должна быть больше нуля");
                    throw (new NumberFormatException());
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(mainPanel, "Неверный формат числа массы");
                return;
            }
            try {
                timeStep = Double.parseDouble(timeStepField.getText());
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(mainPanel, "Неверный формат числа времени");
                return;
            }


            Chart chart = TrajectoryDrawer.getChart(pVelocity,bVector,eVector,qCharge,pMass,timeStep);
            Component[] componentList = mainPanel.getComponents();
            for (Component c : componentList) {
                if (c instanceof ICanvas)
                    mainPanel.remove(c);
            }
            mainPanel.add((Component) chart.getCanvas(), BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    private BaseForm() {

        SwingUtilities.invokeLater(this::initComponents);

    }

    public static void main(String[] args) {
        BaseForm mainForm = new BaseForm();
        initFrame(mainForm, "Траектория движения заряженной частицы в магнитном поле", mainForm.mainPanel);
        mainForm.setVisible(true);
    }

    private static void initFrame(JFrame someFrame, String title, Container contentPane) {
        someFrame.setTitle(title);
        someFrame.setContentPane(contentPane);
        someFrame.pack();
        someFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initComponents() {

        Chart chart = TrajectoryDrawer.getInitialChart();
        this.setMaximumSize(new Dimension(1280, 720));
        this.setMinimumSize(new Dimension(1000, 500));

        this.drawButton.addActionListener(new DrawActionListener());
        this.bVectorField.setMaximumSize(new Dimension(250, 25));
        this.bVectorField.setHorizontalAlignment(JTextField.CENTER);
        this.eVectorField.setMaximumSize(new Dimension(250, 25));
        this.eVectorField.setHorizontalAlignment(JTextField.CENTER);
        this.qChargeField.setMaximumSize(new Dimension(250, 25));
        this.qChargeField.setHorizontalAlignment(JTextField.CENTER);
        this.pMassField.setMaximumSize(new Dimension(250, 25));
        this.pMassField.setHorizontalAlignment(JTextField.CENTER);
        this.pVelocityField.setMaximumSize(new Dimension(250, 25));
        this.pVelocityField.setHorizontalAlignment(JTextField.CENTER);
        this.timeStepField.setMaximumSize(new Dimension(250, 25));
        this.timeStepField.setHorizontalAlignment(JTextField.CENTER);

        this.bVectorLabel.setMaximumSize(new Dimension(250, 25));
        this.bVectorLabel.setHorizontalAlignment(JTextField.CENTER);
        this.eVectorLabel.setMaximumSize(new Dimension(250, 25));
        this.eVectorLabel.setHorizontalAlignment(JTextField.CENTER);
        this.qChargeLabel.setMaximumSize(new Dimension(250, 25));
        this.qChargeLabel.setHorizontalAlignment(JTextField.CENTER);
        this.pMassLabel.setMaximumSize(new Dimension(250, 25));
        this.pMassLabel.setHorizontalAlignment(JTextField.CENTER);
        this.pVelocityLabel.setMaximumSize(new Dimension(250, 25));
        this.pVelocityLabel.setHorizontalAlignment(JTextField.CENTER);
        this.timeStepLabel.setMaximumSize(new Dimension(250, 25));
        this.timeStepLabel.setHorizontalAlignment(JTextField.CENTER);
        this.warningLabel.setMaximumSize(new Dimension(200, 60));
        this.warningLabel.setHorizontalAlignment(JTextField.CENTER);

        this.controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        this.controlPanel.add(bVectorLabel);
        this.controlPanel.add(bVectorField);
        this.controlPanel.add(eVectorLabel);
        this.controlPanel.add(eVectorField);
        this.controlPanel.add(qChargeLabel);
        this.controlPanel.add(qChargeField);
        this.controlPanel.add(pMassLabel);
        this.controlPanel.add(pMassField);
        this.controlPanel.add(pVelocityLabel);
        this.controlPanel.add(pVelocityField);
        this.controlPanel.add(timeStepLabel);
        this.controlPanel.add(timeStepField);
        this.controlPanel.add(warningLabel);
        this.controlPanel.add(drawButton);

        this.drawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.bVectorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.eVectorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.qChargeField.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.pMassField.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.pVelocityField.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.bVectorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.eVectorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.qChargeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.pMassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.pVelocityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.timeStepLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.mainPanel.setLayout(new BorderLayout());
        this.mainPanel.add((Component) chart.getCanvas(), BorderLayout.CENTER);
        this.mainPanel.add(controlPanel, BorderLayout.EAST);
        this.mainPanel.setPreferredSize(new Dimension(800, 400));
    }

}
