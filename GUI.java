import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import javax.swing.UIManager.*;

public class GUI
extends JFrame
implements ActionListener {
    private JButton waveButton;
    private JButton wavierButton;

    private void
    attemptNimbusStyle() {
        try {
            for (LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (Exception e) {
        }
    }

    public
    GUI() {
        attemptNimbusStyle();
        setTitle("Volume Integral Visualizer");

        waveButton   = new JButton("Wave");
        wavierButton = new JButton("Wavier");

        waveButton  .setActionCommand("set_func_wave");
        wavierButton.setActionCommand("set_func_wavier");

        Container cp = getContentPane();
        cp.setLayout(new FlowLayout());
        cp.add(waveButton);
        cp.add(wavierButton);

        pack();

        waveButton  .addActionListener(this);
        wavierButton.addActionListener(this);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public static void
    main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI gui = new GUI();
                gui.setVisible(true);
            }
        });
    }

    public void
    actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("set_func_wave")) {
            onWaveButtonClicked();
        }
        else if (cmd.equals("set_func_wavier")) {
            onWavierButtonClicked();
        }
    }

    private void
    onWaveButtonClicked() {
        Visualizer.launchWith(new WaveFunc(), "cel");
    }

    private void
    onWavierButtonClicked() {
        Visualizer.launchWith(new WavierFunc(), null);
    }
}
