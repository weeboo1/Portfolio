
# Java Chat Application

This is a simple local chat application built in Java. It uses sockets for network communication and Swing for the graphical interface. The app allows multiple clients to connect to a server and chat with each other in real time.

# Features

- Supports multiple clients at once using threads
- Graphical interface for both server and client
- Messages are broadcast to all connected clients
- Clients can join, leave, and reconnect to the chat
- Each client has a username that's shown with their messages
- Server has a log window and a button to stop the server
  
---
  # How to Run

# 1. Start the Server

- **GUI version**: Run `ServerWindow.java`  
- **Console version**: Run `ChatServer.java`

# 2. Run Clients

Each client can be started by running `ClientWindow.java`. You will be asked to enter your name before joining the chat.
You can open several clients at once to simulate multiple users.

# Reconnect Feature

If a client is disconnected (manually or because the server shuts down), the *Reconnect* button allows them to reconnect without retyping their name.

## Stopping the Server

- In GUI: Just press the **Stop Server** button  
- In console: Type `exit` and press Enter

---

# Notes

- Communication is done over TCP sockets
- Messages are broadcasted to all connected clients
- Handles client disconnects without crashing
- For simplicity, no encryption or authentication is implemented

by Halahan Kiril.
