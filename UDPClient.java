import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;

public class UDPClient {

    public JFrame frame;
    public JLabel titleLabel;
    public JTextArea scrollableTextArea;
    public JScrollPane scrollPane;
    public JPanel inputPanel;
    public JTextField inputTextField;
    public JButton sendButton;
    public DatagramSocket clientSocket;
    public InetAddress serverAddress;
    public int serverPort;

    public static void main(String[] args) {
        UDPClient u = new UDPClient();
        u.createAndShowGUI();
    }

    private void createAndShowGUI() {
        this.frame = new JFrame("Chat-client App");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new BorderLayout());

        // Title
        this.titleLabel = new JLabel("Client");
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
                scrollableTextArea.append("\n" + inputText + "(me:client)");
                byte[] sendData = inputText.getBytes();

                try {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress,
                            serverPort);
                    clientSocket.send(sendPacket);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
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
            System.out.println("frame1");
            this.clientSocket = new DatagramSocket();
            this.serverAddress = InetAddress.getByName("localhost");

            //
            byte[] receiveData = new byte[1024];

            serverPort = 9876;
            int c = 0;
            String message = "connected";
            byte[] sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            clientSocket.send(sendPacket);
            scrollableTextArea.append("\t Connected \t");
            while (c < 30) {
                DatagramPacket rmsg = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(rmsg);
                c++;
                String strmsg = new String(rmsg.getData(), 0, rmsg.getLength());
                scrollableTextArea.append("\n\t\t" + strmsg + "(server)");
            }
            clientSocket.close();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
