#include "PasswordManager.h"
#include <iostream>

int main() {
    PasswordManager manager("vault.dat");

    std::string masterPassword;
    std::cout << "Enter master password: ";
    std::getline(std::cin, masterPassword);

    if (!manager.login(masterPassword)) {
        std::cout << "Incorrect password!\n";
        return 1;
    }

    int choice;
    do {
        std::cout << "\n1. Add entry\n2. Show entries\n3. Delete entry\n4. Generate password\n0. Exit\nChoice: ";
        std::cin >> choice;
        std::cin.ignore();

        if (choice == 1) {
            Entry e;
            std::cout << "Site: ";
            std::getline(std::cin, e.site);
            std::cout << "Login: ";
            std::getline(std::cin, e.login);
            std::cout << "Password: ";
            std::getline(std::cin, e.password);
            manager.addEntry(e);
        } else if (choice == 2) {
            manager.showEntries();
        } else if (choice == 3) {
            std::string site;
            std::cout << "Enter site to delete: ";
            std::getline(std::cin, site);
            manager.deleteEntry(site);
        } else if (choice == 4) {
            int len;
            std::cout << "Length: ";
            std::cin >> len;
            std::cin.ignore();
            std::cout << "Generated password: " << manager.generatePassword(len) << "\n";
        }
    } while (choice != 0);

    return 0;
}
