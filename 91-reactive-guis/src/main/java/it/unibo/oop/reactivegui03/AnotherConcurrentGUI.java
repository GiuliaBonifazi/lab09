package it.unibo.oop.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
//import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Third experiment with reactive gui.
 */
public final class AnotherConcurrentGUI extends JFrame {
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;

    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
    private final JButton stop = new JButton("stop");
    private final JLabel display = new JLabel();

    public AnotherConcurrentGUI() {
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

        final AgentTime agent = new AgentTime();
        new Thread(agent.getAgentCounting()).start();
        new Thread(agent).start();

        //Listeners
        stop.addActionListener((e) -> agent.getAgentCounting().stopCounting());
        up.addActionListener((e) -> agent.getAgentCounting().countUp());
        down.addActionListener((e) -> agent.getAgentCounting().countDown());
    }

    private class AgentCounting implements Runnable {

        private volatile boolean stop = false;
        private volatile boolean up = true;
        private int counter = 0;
        private double startTime = System.currentTimeMillis();

        @Override
        public void run() {
            while (!this.stop) {
                try {
                    final var nextText = Integer.toString(this.counter);
                    SwingUtilities.invokeAndWait(() -> AnotherConcurrentGUI.this.display.setText(nextText));
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

        private void stopCounting() {
            this.stop = true;
            AnotherConcurrentGUI.this.stop.setEnabled(false);
            AnotherConcurrentGUI.this.up.setEnabled(false);
            AnotherConcurrentGUI.this.down.setEnabled(false);
        }
            
        private void countUp() {
           this.up = true;
        }

        private void countDown() {
            this.up = false;
        }

        private double getStartTime() {
            return this.startTime;
        }
    }

    private class AgentTime implements Runnable {
        private AgentCounting agentCount = new AgentCounting();

        @Override
        public void run() {
            while (!this.agentCount.stop) {
                if (System.currentTimeMillis() >= (this.agentCount.getStartTime() + 10_000)) {
                    agentCount.stopCounting();
                }
            }
        } 
        
        public AgentCounting getAgentCounting() {
            return this.agentCount;
        }
    }
}
