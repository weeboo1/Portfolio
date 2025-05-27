Project SkillTrack

Description
SkillTrack is a lightweight web application that allows users to track and manage their personal skill development. Users can register, log in, add new skills, and break each skill down into actionable steps. The app also tracks progress and visualizes it with a chart.

Features
- User registration and login
- Add and view personal skills
- Add steps to each skill and mark them as done/undone
- Progress bar chart (via Chart.js) showing completion percentage per skill
- Simple and clean UI with dark/light mode (Dark/light theme toggle)

Technologies Used
- PHP (Backend)
- MySQL (Database)
- HTML/CSS (Frontend)
- Chart.js (Data visualization)
- JavaScript (Theme toggle)

Folder Structure
- `dashboard.php` – Main user interface
- `login.php`  `register.php` – Authentication
- `skill.php` – Skill detail view
- `db.php` – PDO connection setup
- `style.css` – Theme styling
- `theme-toggle.js` – Theme toggle script

How to Run
1. Create a local MySQL database named `skilltrack`
2. Import `schema.sql` into the database
3. Edit `db.php` with your database credentials
4. Run using a PHP local server
5. Access via browser
