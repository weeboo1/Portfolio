import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ClientWindow extends JFrame {
    private JTextArea chatArea = new JTextArea();
    private JTextField inputField = new JTextField();
    private JButton sendBtn = new JButton("Send");
    private JButton reconnectBtn = new JButton("Reconnect"); // <--- Новая кнопка
    private ChatClient client;
    private final String host;
    private final int port;
    private final String name;

    public ClientWindow(String host, int port, String name) {
        this.host = host;
        this.port = port;
        this.name = name;

        setTitle("Chat — " + name);
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Панель снизу с полем ввода и кнопками
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(inputField, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(sendBtn);
        buttons.add(reconnectBtn); // <--- Добавляем новую кнопку
        bottom.add(buttons, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        connectClient(); // Первая попытка подключения

        // Обработка кнопки Send
        ActionListener sendAction = e -> {
            String msg = inputField.getText().trim();
            if (!msg.isEmpty()) {
                client.send(msg);
                inputField.setText("");
            }
        };
        sendBtn.addActionListener(sendAction);
        inputField.addActionListener(sendAction);

        // Обработка кнопки Reconnect
        reconnectBtn.addActionListener(e -> reconnect());
    }

    private void appendMessage(String msg) {
        SwingUtilities.invokeLater(() -> chatArea.append(msg + "\n"));
    }

    private void connectClient() {
        client = new ChatClient(host, port, name, this::appendMessage);
        try {
            client.connect();
            appendMessage("Connected as " + name);
        } catch (IOException e) {
            appendMessage("Failed to connect: " + e.getMessage());
            client = null; // <--- обязательно!
        }
    }


private void reconnect() {
    if (client != null) {
        try {
            client.disconnect(); // закрыть старое соединение
        } catch (IOException e) {
            appendMessage("Error disconnecting: " + e.getMessage());
        }
    }

    appendMessage("Reconnecting...");
    connectClient();
}


    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog("Enter your name:");
        if (name != null && !name.trim().isEmpty()) {
            SwingUtilities.invokeLater(() -> new ClientWindow("localhost", 12346, name).setVisible(true));
        }
    }
}
