package it.unibo.oop.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Second example of reactive GUI.
 */
public final class ConcurrentGUI extends JFrame {

    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;

    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
    private final JButton stop = new JButton("stop");
    private final JLabel display = new JLabel();

    public ConcurrentGUI() {
        super();
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screen.getWidth() * WIDTH_PERC), (int) (screen.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(display);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);

        final Agent agent = new Agent();
        new Thread(agent).start();

        //Listeners
        stop.addActionListener((e) -> agent.stopCounting());
        up.addActionListener((e) -> agent.countUp());
        down.addActionListener((e) -> agent.countDown());
    }

    private class Agent implements Runnable {

        private volatile boolean stop = false;
        private volatile boolean up = true;
        private int counter = 0;

        @Override
        public void run() {
            while (!this.stop) {
                try {
                    final var nextText = Integer.toString(this.counter);
                    SwingUtilities.invokeAndWait(() -> ConcurrentGUI.this.display.setText(nextText));
                    if (!this.up) {
                        this.counter--;
                    } else {
                        this.counter++;
                    }
                    Thread.sleep(100);
                } catch (InvocationTargetException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        public void stopCounting() {
            this.stop = true;
            ConcurrentGUI.this.stop.setEnabled(false);
            ConcurrentGUI.this.up.setEnabled(false);
            ConcurrentGUI.this.down.setEnabled(false);
        }
            
        public void countUp() {
           this.up = true;
        }

        public void countDown() {
            this.up = false;
        }
    }
}
