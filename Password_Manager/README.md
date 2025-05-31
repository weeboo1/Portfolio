# Project: Password Manager

# Description:
A lightweight console application written in C++ that helps you keep track of your passwords in one place. Instead of relying on your browser — which isn't always the most secure option — you can store credentials locally in an encrypted file.

# Core Features:
- Master password authentication
- Add new entries (website, username, password)
- View all saved entries (decrypted for display)
- Delete entries by website name
- Save all data encrypted in a file (`vault.dat`)
- Generate strong random passwords on the fly

# Encryption:
Passwords are encrypted using a basic XOR cipher with a repeated key based on your master password. The same method is used for both encryption and decryption.

# Technologies:
- Language: C++
- Compilation: g++ (MSYS2 environment)
- Platform: Console

# File Structure:
- `main.cpp`: Handles the user interface and main logic loop
- `PasswordManager.h/.cpp`: Core functionality: encryption, file I/O, and data handling
- `vault.dat`: Stores encrypted password entries 

By Kiril Halahan
