Project: Password Manager

Description:
This is a console-based password manager application written in C++. It showcases key programming skills such as object-oriented design, file handling, basic encryption, and clean code structure. It's a great demonstration of practical C++ use in real-world scenarios.

Core Features:
- Master password authentication
- Add new entries (website, username, password)
- View all saved entries (decrypted)
- Delete entries by website name
- Save all data encrypted in a file (`vault.dat`)
- Built-in secure password generator

Encryption:
A simple symmetric XOR encryption is used to protect the stored data. The same function is used for both encryption and decryption by applying the XOR operation with a repeating key.

⚙ Technologies:
- Language: C++
- Compilation: g++ (MSYS2 environment)
- Platform: Console (cross-platform capable with small adjustments)

Note:
This is an educational project. For real-world password storage, a secure encryption library like OpenSSL and proper key management should be used.

Skills Demonstrated:
- C++ OOP (classes, constructors, encapsulation)
- File I/O
- Custom encryption logic
- CLI interface design
- Secure data handling principles

File Structure:
- `main.cpp`: Program entry point and menu navigation
- `PasswordManager.h/.cpp`: Core logic for storing, encrypting, and managing passwords
- `vault.dat`: Encrypted storage file (binary/text mix)
