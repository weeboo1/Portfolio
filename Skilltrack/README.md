# **SkillTrack**

# Description
SkillTrack is a lightweight web application that allows users to track and manage their personal skill development. Users can create an account, add skill they’re working on, break them down into smaller steps, and track their progress visually.

# Features
- User registration and login system
- Add and manage a list of your personal skills
- Break each skill into smaller steps and mark them as done or not
- Progress tracking with a visual chart for each skill (using Chart.js)
- Clean and minimal interface with support for dark and light modes

# Technologies Used
- PHP (Backend)
- MySQL (Database)
- HTML/CSS (Frontend)
- Chart.js (Data visualization)
- JavaScript (Interactivity and theme toggle)

# Folder Structure
- `dashboard.php` – Main user interface
- `login.php`  `register.php` – handles user authentication
- `skill.php` – detailed view of a selected skill 
- `db.php` – sets up database connection using PDO 
- `style.css` – Theme styling
- `theme-toggle.js` – toggles between themes 

# How to Run
1. Create a local MySQL database named `skilltrack`
2. Import `schema.sql` into the database
3. Edit `db.php` with your database credentials
4. Run using a PHP local server
5. Open the app in your browser

by Halahan Kiril

