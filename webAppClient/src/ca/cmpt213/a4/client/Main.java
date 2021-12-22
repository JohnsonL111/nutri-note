package ca.cmpt213.a4.client;

import ca.cmpt213.a4.client.view.GUI;

import javax.swing.*;

/**
 * Driver class for application.
 */
public class Main {
    /**
     * Runs our application.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        // create the frame on the event dispatching thread.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }
}
