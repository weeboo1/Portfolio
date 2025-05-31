#include "PasswordManager.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include <ctime>
#include <cstdlib>
#include <algorithm>

PasswordManager::PasswordManager(const std::string& file) : filename(file) {
    load();
}

// XOR encryption
std::string PasswordManager::encryptDecrypt(const std::string& data, const std::string& key) {
    std::string result = data;
    for (size_t i = 0; i < data.size(); ++i) {
        result[i] = data[i] ^ key[i % key.length()];
    }
    return result;
}

std::string PasswordManager::hash(const std::string& input) {
    unsigned int hash = 0;
    for (char c : input) {
        hash = hash * 101 + c;
    }
    return std::to_string(hash);
}

bool PasswordManager::login(const std::string& masterPassword) {
    if (masterPasswordHash.empty()) {
        masterPasswordHash = hash(masterPassword);
        save();
        return true;
    }
    return masterPasswordHash == hash(masterPassword);
}

void PasswordManager::addEntry(const Entry& entry) {
    entries.push_back(entry);
    save();
}

void PasswordManager::showEntries() {
    for (const auto& e : entries) {
        std::cout << "Site: " << e.site << ", Login: " << e.login << ", Password: " << e.password << "\n";
    }
}

void PasswordManager::deleteEntry(const std::string& site) {
    entries.erase(std::remove_if(entries.begin(), entries.end(),
        [&site](const Entry& e) { return e.site == site; }), entries.end());
    save();
}

void PasswordManager::load() {
    std::ifstream file(filename, std::ios::binary);
    if (!file.is_open()) return;

    std::getline(file, masterPasswordHash);
    std::string line;
    while (std::getline(file, line)) {
        std::istringstream ss(encryptDecrypt(line, masterPasswordHash));
        Entry e;
        std::getline(ss, e.site, ',');
        std::getline(ss, e.login, ',');
        std::getline(ss, e.password, ',');
        entries.push_back(e);
    }
}

void PasswordManager::save() {
    std::ofstream file(filename, std::ios::binary | std::ios::trunc);
    file << masterPasswordHash << "\n";
    for (const auto& e : entries) {
        std::string data = e.site + "," + e.login + "," + e.password;
        file << encryptDecrypt(data, masterPasswordHash) << "\n";
    }
}

std::string PasswordManager::generatePassword(int length) {
    const std::string chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ012345679!@#$%^&*()";
    std::string password;
    srand(time(0));
    for (int i = 0; i < length; ++i) {
        password += chars[rand() % chars.size()];
    }
    return password;
}
