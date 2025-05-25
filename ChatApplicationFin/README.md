
# Java Chat Application

A simple local client-server chat application written in Java using sockets and Swing. Supports multiple clients, graphical UI for both server and client, and real-time messaging.

---

## Features

- Multi-client support using threads
- Graphical client with message input and display
- Graphical server with live log and stop control
- Broadcast messages to all clients
- Clients can disconnect and reconnect
- User names are assigned and shown in messages

---

### 1. Run the Server

- Option A: Launch `ServerWindow.java` for GUI version
- Option B: Run `ChatServer.java` for console version

### 2. Run Clients

Each client can be started by running `ClientWindow.java`. You will be asked to enter your name before joining the chat.

You can run multiple instances to simulate multiple users.

---

## Reconnect Feature

If a client is disconnected (manually or due to server shutdown), the "Reconnect" button allows them to reconnect without retyping their name.

---

## To Stop the Server

In GUI mode: click **Stop Server**

In console mode: type `exit` and press Enter

---

## 📌 Notes

- All communication is done via TCP sockets
- Messages are broadcasted to all connected clients
- Handles basic disconnection gracefully
- For simplicity, no encryption or authentication is implemented

---


Created by Halahan Kiril. For educational and portfolio purposes.
