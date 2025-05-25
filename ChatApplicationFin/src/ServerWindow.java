import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.BindException;

public class ServerWindow extends JFrame {
    private final JTextArea logArea   = new JTextArea();
    private final JButton startBtn    = new JButton("Start Server");
    private final JButton stopBtn     = new JButton("Stop Server");
    private final int port;
    private ChatServer server;
    private Thread  serverThread;

    public ServerWindow(int port) {
        super("Chat Server");
        this.port = port;
        initUI();
        attachActions();
        pack();
        setLocationRelativeTo(null);   // центр экрана
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initUI() {
        // Логи
        logArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setPreferredSize(new Dimension(500, 300));

        // Панель кнопок
        JPanel buttons = new JPanel();
        buttons.add(startBtn);
        buttons.add(stopBtn);
        stopBtn.setEnabled(false);    // пока сервер не запущен

        // Собираем окно
        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(scroll,   BorderLayout.CENTER);
        getContentPane().add(buttons,  BorderLayout.SOUTH);
    }

    private void attachActions() {
        // Кнопка Start
        startBtn.addActionListener(e -> {
            startBtn.setEnabled(false);
            appendLog("Attempting to start server on port " + port + "...");
            serverThread = new Thread(this::startServer, "Server-Thread");
            serverThread.start();
        });

        // Кнопка Stop
        stopBtn.addActionListener(e -> {
            stopBtn.setEnabled(false);
            appendLog("Stopping server...");
            if (server != null) {
                server.stop();
            }
            // Ждём завершения потока, чтобы снова можно было стартовать
            new Thread(() -> {
                try {
                    if (serverThread != null) serverThread.join();
                } catch (InterruptedException ignored) {}
                SwingUtilities.invokeLater(() -> startBtn.setEnabled(true));
            }).start();
        });
    }

    // Запуск ChatServer в фоновом потоке
    private void startServer() {
        try {
            server = new ChatServer(port) {
                @Override
                public void broadcast(String msg, ClientHandler sender) {
                    super.broadcast(msg, sender);
                    appendLog(msg);
                }
            };
            SwingUtilities.invokeLater(() -> {
                appendLog("Server started on port " + port);
                stopBtn.setEnabled(true);
            });
            server.start();  // блокирует до server.stop()
        } catch (BindException be) {
            appendLog("ERROR: Port " + port + " is already in use.");
            SwingUtilities.invokeLater(() -> startBtn.setEnabled(true));
        } catch (IOException ioe) {
            appendLog("ERROR starting server: " + ioe.getMessage());
            SwingUtilities.invokeLater(() -> startBtn.setEnabled(true));
        } finally {
            appendLog("Server thread exiting.");
        }
    }
    

    // Безопасно из любого потока логаем в JTextArea
    private void appendLog(String msg) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(msg + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServerWindow(12346));
    }
}
