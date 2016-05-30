package nl.codefox.gilmore;/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate the port forwarding like option -L of
 * ssh command; the given port on the local host will be forwarded to
 * the given remote host and port on the remote side.
 * $ CLASSPATH=.:../build javac PortForwardingL.java
 * $ CLASSPATH=.:../build java PortForwardingL
 * You will be asked username, hostname, port:host:hostport and passwd.
 * If everything works fine, you will get the shell prompt.
 * Try the port on localhost.
 */

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

import javax.swing.*;
import java.awt.*;

public class PortForwardingL {
    public static void main(String[] arg) {

        int lport;
        String rhost;
        int rport;

        try {
            JSch jsch = new JSch();
            String privateKey = "~/.ssh/id_rsa";
            jsch.addIdentity(privateKey);

            Session session = jsch.getSession("josh", "192.168.0.23", 22);

            lport = 3307;
            rhost = "192.168.0.23";
            rport = 3306;

            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);

            session.connect();

            //Channel channel=session.openChannel("shell");
            //channel.connect();

            int assinged_port = session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
        public String getPassword() {
            return null;
        }

        public boolean promptYesNo(String str) {
            Object[] options = {"yes", "no"};
            int foo = JOptionPane.showOptionDialog(null,
                    str,
                    "Warning",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            return foo == 0;
        }

        String passphrase;
        JTextField passphraseField = (JTextField) new JPasswordField(20);

        public String getPassphrase() {
            return passphrase;
        }

        public boolean promptPassphrase(String message) {
            Object[] ob = {passphraseField};
            int result =
                    JOptionPane.showConfirmDialog(null, ob, message,
                            JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                passphrase = passphraseField.getText();
                return true;
            } else {
                return false;
            }
        }

        public boolean promptPassword(String message) {
            return true;
        }

        public void showMessage(String message) {
            JOptionPane.showMessageDialog(null, message);
        }

        final GridBagConstraints gbc =
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0);
        private Container panel;

        public String[] promptKeyboardInteractive(String destination,
                                                  String name,
                                                  String instruction,
                                                  String[] prompt,
                                                  boolean[] echo) {
            panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            gbc.weightx = 1.0;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.gridx = 0;
            panel.add(new JLabel(instruction), gbc);
            gbc.gridy++;

            gbc.gridwidth = GridBagConstraints.RELATIVE;

            JTextField[] texts = new JTextField[prompt.length];
            for (int i = 0; i < prompt.length; i++) {
                gbc.fill = GridBagConstraints.NONE;
                gbc.gridx = 0;
                gbc.weightx = 1;
                panel.add(new JLabel(prompt[i]), gbc);

                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weighty = 1;
                if (echo[i]) {
                    texts[i] = new JTextField(20);
                } else {
                    texts[i] = new JPasswordField(20);
                }
                panel.add(texts[i], gbc);
                gbc.gridy++;
            }

            if (JOptionPane.showConfirmDialog(null, panel,
                    destination + ": " + name,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE)
                    == JOptionPane.OK_OPTION) {
                String[] response = new String[prompt.length];
                for (int i = 0; i < prompt.length; i++) {
                    response[i] = texts[i].getText();
                }
                return response;
            } else {
                return null;  // cancel
            }
        }
    }
}
