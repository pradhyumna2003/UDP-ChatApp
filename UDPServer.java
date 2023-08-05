import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
//
import java.net.*;

public class UDPServer {
    //
    public JFrame frame;
    public JLabel titleLabel;
    public JTextArea scrollableTextArea;
    public JScrollPane scrollPane;
    public JPanel inputPanel;
    public JTextField inputTextField;
    public JButton sendButton;
    public InetAddress clientAddress;
    public int clientPort;
    public DatagramSocket serverSocket;

    public static void main(String[] args) {
        // SwingUtilities.invokeLater(() -> createAndShowGUI());
        UDPServer us = new UDPServer();
        us.createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Chat-Server App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Title
        titleLabel = new JLabel("Server");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(titleLabel, BorderLayout.NORTH);

        // Scrollable Text Area
        scrollableTextArea = new JTextArea(20, 40);
        scrollPane = new JScrollPane(scrollableTextArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Input Text Field and Send Button
        inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        inputTextField = new JTextField(20);
        sendButton = new JButton("Send");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = inputTextField.getText();
                scrollableTextArea.append("\n(me)" + inputText);
                byte[] sendData = inputText.getBytes();
                try {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress,
                            clientPort);
                    serverSocket.send(sendPacket);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                inputTextField.setText("");
            }
        });

        inputPanel.add(inputTextField);
        inputPanel.add(sendButton);

        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
        connect();
    }

    private void connect() {
        try {
            int c = 0;
            System.out.println("frame1");
            serverSocket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            clientAddress = receivePacket.getAddress();
            clientPort = receivePacket.getPort();

            scrollableTextArea.append("\t Connected \t");
            while (c < 30) {
                DatagramPacket rmsg = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(rmsg);
                c++;
                String message = new String(rmsg.getData(), 0, rmsg.getLength());
                scrollableTextArea.append("\n\t\t" + message + "(client)");
            }
            serverSocket.close();
        } catch (Exception e) {

        }
    }

}
