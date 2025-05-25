import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

// Основной класс сервера чата
public class ChatServer {
    // Серверный сокет, принимающий входящие подключения
    private ServerSocket serverSocket;

    // Потокобезопасное множество обработчиков клиентов
    private final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();

    // Флаг «работает ли сервер» — volatile, чтобы изменения сразу видели потоки
    private volatile boolean running = false;

    // Конструктор: открывает ServerSocket на заданном порту
    public ChatServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    // Запустить сервер: начинает слушать входящие подключения
    public void start() {
        running = true;
        System.out.println("Server started on port " + serverSocket.getLocalPort());

        // Пул потоков для обслуживания клиентов
        ExecutorService pool = Executors.newCachedThreadPool();

        // Главный цикл принятия подключений
        while (running) {
            try {
                // Ждём нового клиента (блокирует до подключения)
                Socket clientSocket = serverSocket.accept();

                // Создаём обработчик для этого клиента
                ClientHandler handler = new ClientHandler(clientSocket, this);

                // Регистрируем обработчик в списке
                clients.add(handler);

                // Передаём обработку в пул потоков
                pool.submit(handler);
            } catch (IOException e) {
                // Если сервер ещё работает — выводим ошибку
                if (running) e.printStackTrace();
            }
        }

        // После выхода из цикла — корректно останавливаем пул
        pool.shutdown();
    }

    // Остановить сервер: разрываем цикл и закрываем сокет
public void stop() {
    running = false;
    try {
        serverSocket.close(); // завершает accept()
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Закрываем всех клиентов
    for (ClientHandler client : clients) {
        client.close(); // сделаем этот метод публичным
    }

    System.out.println("Server stopped.");
}


    // Рассылка сообщения всем подключённым (включая отправителя)
    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            client.send(message);
        }
    }

    // Удалить клиента из списка (при отключении)
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    // Точка входа для запуска консольного сервера
    public static void main(String[] args) {
        int port = 12346;
        try {
            ChatServer server = new ChatServer(port);
            // Запускаем start() в отдельном потоке, чтобы не блокировать main()
            new Thread(server::start).start();                                                  //--------

            // Простая команда «exit» в консоли для остановки
            System.out.println("Type 'exit' to stop server.");
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String line = console.readLine();
                if ("exit".equalsIgnoreCase(line)) {                                            //----
                    server.stop();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Обработчик одного клиента; реализует Runnable, чтобы его можно было запустить в потоке
class ClientHandler implements Runnable {
    private Socket socket;              // сокет этого клиента
    private ChatServer server;          // ссылка на сервер для broadcast/remove
    private BufferedReader in;          // для чтения из сокета
    private PrintWriter out;            // для записи в сокет
    private String name;                // имя пользователя

    private boolean closed = false; // Новый флаг

    
    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    // Метод, который выполняется в отдельном потоке
    @Override
    public void run() {
        try {
            // Настраиваем потоки ввода/вывода
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);  //---------- true?

            // Первый шаг: запрашиваем имя пользователя
            out.println("Enter your name:");
            name = in.readLine();  // ждём, пока клиент введёт имя

            // Оповещаем всех: пользователь зашёл
            server.broadcast(name + " joined the chat.", this);  //this?
            System.out.println(name + " connected.");

            // Основной цикл чтения сообщений
            String msg;                                  // как работает Основной цикл чтения сообщений? где мы в коде принимаем сообщение от пользователя
            while ((msg = in.readLine()) != null) {
                // Команда /quit завершает сессию
                if ("/quit".equalsIgnoreCase(msg)) break;       //equalsIgnoreCase()?

                // Формируем «Имя: текст»
                String fullMsg = name + ": " + msg;
                System.out.println(fullMsg);               // лог в консоль сервера
                server.broadcast(fullMsg, this);           // рассылаем всем
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // При любом завершении (исключение, /quit или закрытие потока) — закрываем ресурсы
            close();  
        }
    }

    // Отправить этому клиенту текст
    public void send(String message) {
        out.println(message);
    }

    // Корректное завершение работы с клиентом
    public void close() {
        if (closed) return; // предотвратить повтор
        closed = true;

        try {
            server.removeClient(this);
            server.broadcast(name + " left the chat.", this);
            socket.close();
            System.out.println(name + " disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

/*
Что здесь происходит и зачем:

ServerSocket слушает порт и создаёт Socket для каждого входящего соединения.

Каждый клиент обрабатывается в своём потоке (ClientHandler), чтобы несколько пользователей могли одновременно обмениваться сообщениями.

Сервер хранит коллекцию активных ClientHandler и метод broadcast() рассылает любое сообщение каждому.

Флаг running и метод stop() дают возможность корректно остановить сервер извне.
*/



// как это вообще работает, если мы не вызываем эти методы в main?