#ifndef PASSWORD_MANAGER_H
#define PASSWORD_MANAGER_H

#include <string>
#include <vector>

struct Entry {
    std::string site;
    std::string login;
    std::string password;
};

class PasswordManager {
private:
    std::string masterPasswordHash;
    std::vector<Entry> entries;
    std::string filename;

    std::string encryptDecrypt(const std::string& data, const std::string& key);
    std::string hash(const std::string& input);
    void load();
    void save();

public:
    PasswordManager(const std::string& file);
    bool login(const std::string& masterPassword);
    void addEntry(const Entry& entry);
    void showEntries();
    void deleteEntry(const std::string& site);
    std::string generatePassword(int length);
};

#endif
