import java.io.*;
import java.net.*;

public class ChatClient {
    private String host;        // адрес сервера
    private int port;           // порт сервера
    private String name;        // имя текущего пользователя
    private Socket socket;      // сокет соединения
    private BufferedReader in;  // чтение от сервера
    private PrintWriter out;    // запись на сервер

    // Интерфейс для обработки входящих сообщений
    public interface MessageListener {
        void onMessage(String message);
    }
    private MessageListener listener;

    // Конструктор: получаем параметры подключения и callback
    public ChatClient(String host, int port, String name, MessageListener listener) {
        this.host     = host;
        this.port     = port;
        this.name     = name;
        this.listener = listener;
    }

    // Установить соединение и запустить поток-приёмник
    public void connect() throws IOException {
        socket = new Socket(host, port);
        in     = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out    = new PrintWriter(socket.getOutputStream(), true);

        // Сервер первым шлёт строку «Enter your name:»
        in.readLine();
        // Отправляем наше имя
        out.println(name);

        // Запускаем фоновый поток для постоянного чтения
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    listener.onMessage(msg);  // передаём GUI
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Отправить строку на сервер
    public void send(String message) {
        out.println(message);
    }

    // Отключиться: сообщить /quit и закрыть сокет
    public void disconnect() throws IOException {
        send("/quit");
        socket.close();
    }
}

/*
Зачем и как:

При connect() мы создаём Socket, настраиваем BufferedReader и PrintWriter.

Сначала читаем приглашение ввести имя, потом сразу его шлём.

Отдельный поток читает все сообщения из in и передаёт их через listener в GUI.

*/


/*
Итог
Сетевой уровень (ChatServer + ClientHandler и ChatClient) заботится о подключениях и пересылке строк.

GUI-уровень (ServerWindow и ClientWindow) отвечает только за визуализацию и ввод, делегируя все сетевые операции соответствующим классам.

Такой чёткий раздел «сеть ↔ UI» облегчает отладку, расширение (например, добавление приватных сообщений, логирования в файл, шифрования) и делает код более поддерживаемым.

*/