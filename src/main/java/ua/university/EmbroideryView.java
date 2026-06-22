package ua.university;

import javax.swing.*;
import java.awt.*;

class EmbroideryView extends JFrame {
    private final CanvasPanel canvasPanel;
    public final JScrollPane scrollPane;

    public JComboBox<String> colorPicker;
    public JCheckBox horizontalSym;
    public JCheckBox verticalSym;

    public JButton btnDupHoriz;
    public JButton btnDupVert;
    public JButton btnZoomIn;
    public JButton btnZoomOut;
    public JButton btnCenter;
    public JButton btnSave;
    public JButton btnLoad;
    public JButton btnClear;

    public EmbroideryView(EmbroideryModel model) {
        this.setTitle("Піксельна вишивка. Редактор орнаменту | Олександр Малецький");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        canvasPanel = new CanvasPanel(model);

        scrollPane = new JScrollPane(canvasPanel);
        scrollPane.setPreferredSize(new Dimension(620, 620));

        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = buildControlPanel();
        this.add(controlPanel, BorderLayout.WEST);

        this.pack();
        this.setLocationRelativeTo(null);
    }

    private JPanel buildControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;

        JPanel colorSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        colorSubPanel.add(new JLabel("Колір: "));
        colorPicker = new JComboBox<>(new String[]{"Червоний", "Чорний", "Синій", "Зелений", "Гумка"});
        colorSubPanel.add(colorPicker);
        gbc.gridy = 0;
        panel.add(colorSubPanel, gbc);

        gbc.gridy = 1;
        panel.add(new JSeparator(), gbc);

        gbc.gridy = 2;
        panel.add(new JLabel("Масштаб (Зум):"), gbc);
        JPanel zoomPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        btnZoomIn = new JButton("+");
        btnZoomOut = new JButton("-");
        zoomPanel.add(btnZoomIn);
        zoomPanel.add(btnZoomOut);
        gbc.gridy = 3;
        panel.add(zoomPanel, gbc);

        btnCenter = new JButton("Відцентрувати");
        gbc.gridy = 4;
        panel.add(btnCenter, gbc);

        gbc.gridy = 5;
        panel.add(new JSeparator(), gbc);

        gbc.gridy = 6;
        panel.add(new JLabel("Симетрія при малюванні:"), gbc);
        horizontalSym = new JCheckBox("Горизонтальна");
        verticalSym = new JCheckBox("Вертикальна");
        horizontalSym.setSelected(true);
        verticalSym.setSelected(true);
        gbc.gridy = 7;
        panel.add(horizontalSym, gbc);
        gbc.gridy = 8;
        panel.add(verticalSym, gbc);

        gbc.gridy = 9;
        panel.add(new JSeparator(), gbc);

        gbc.gridy = 10;
        panel.add(new JLabel("Фізичне дублювання:"), gbc);
        btnDupHoriz = new JButton("Здублювати вправо ->");
        btnDupVert = new JButton("Здублювати вниз \\/");
        gbc.gridy = 11;
        panel.add(btnDupHoriz, gbc);
        gbc.gridy = 12;
        panel.add(btnDupVert, gbc);

        gbc.gridy = 13;
        panel.add(new JSeparator(), gbc);

        btnSave = new JButton("Зберегти PNG");
        btnLoad = new JButton("Відкрити PNG");
        btnClear = new JButton("Скинути / Нова");
        gbc.gridy = 14;
        panel.add(btnSave, gbc);
        gbc.gridy = 15;
        panel.add(btnLoad, gbc);
        gbc.gridy = 16;
        panel.add(btnClear, gbc);

        return panel;
    }

    public CanvasPanel getCanvas() {
        return canvasPanel;
    }
}
