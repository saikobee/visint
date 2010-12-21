/* Copyright 2010 Brian Mock
 *
 *  This file is part of visint.
 *
 *  visint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  visint is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with visint.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import javax.swing.UIManager.*;

/**
 * This class controls the GUI launcher.
 * @author Brian Mock
 */
public class GUI
extends JFrame
implements ActionListener {
    private JButton   waveButton;
    private JButton wavierButton;
    private JButton saddleButton;

    private WaveFunc   wave   = new WaveFunc();
    private WavierFunc wavier = new WavierFunc();
    private SaddleFunc saddle = new SaddleFunc();

    private String theTitle = "Visual Integrator by Brian Mock";

    // Method provided by Oracle.com Java tutorial
    private void
    attemptNimbusStyle() {
        try {
            for (LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equals("Nimbus")) {
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
        setTitle(theTitle);

        waveButton   = new JButton(wave.toString());
        wavierButton = new JButton(wavier.toString());
        saddleButton = new JButton(saddle.toString());

        waveButton   .setActionCommand("set_func_wave"   );
        wavierButton .setActionCommand("set_func_wavier" );
        saddleButton .setActionCommand("set_func_saddle" );

        Container cp = getContentPane();
        cp.setLayout(new FlowLayout());

        cp.add(new JLabel("Function:"));

        cp.add(waveButton  );
        cp.add(wavierButton);
        cp.add(saddleButton);

        pack();

        waveButton  .addActionListener(this);
        wavierButton.addActionListener(this);
        saddleButton.addActionListener(this);

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

    /** Make a vertical separator. */
    public JSeparator
    makeVSep() {
        return new JSeparator(JSeparator.VERTICAL);
    }

    /** Handle actions being performed. */
    public void
    actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("set_func_wave")) {
            Visualizer.launchWith(wave);
        }
        else if (cmd.equals("set_func_wavier")) {
            Visualizer.launchWith(wavier);
        }
        else if (cmd.equals("set_func_saddle")) {
            Visualizer.launchWith(saddle);
        }
    }
}
