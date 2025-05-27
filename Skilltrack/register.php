<?php
require 'db.php';
session_start();

$errors = [];
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = filter_var($_POST['email'], FILTER_VALIDATE_EMAIL);
    $name = trim($_POST['name']);
    $password = $_POST['password'];
    $password_confirm = $_POST['password_confirm'];

    if (!$email) {
        $errors[] = "Please enter a valid email";
    }
    if (empty($name)) {
        $errors[] = "Please enter your name";
    }
    if (strlen($password) < 6) {
        $errors[] = "Password must be at least 6 characters";
    }
    if ($password !== $password_confirm) {
        $errors[] = "Passwords do not match";
    }

    if (empty($errors)) {
        // Check if email is unique
        $stmt = $pdo->prepare("SELECT id FROM users WHERE email = ?");
        $stmt->execute([$email]);
        if ($stmt->fetch()) {
            $errors[] = "User with this email already exists";
        } else {
            $hash = password_hash($password, PASSWORD_DEFAULT);
            $stmt = $pdo->prepare("INSERT INTO users (email, name, password_hash) VALUES (?, ?, ?)");
            $stmt->execute([$email, $name, $hash]);

            $_SESSION['user_id'] = $pdo->lastInsertId();
            $_SESSION['user_name'] = $name;
            header("Location: dashboard.php");
            exit();
        }
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Register</title>
  <link rel="stylesheet" href="style.css" />
  <script src="theme-toggle.js"></script>
</head>
<body>
<h2>Register</h2>
<button onclick="toggleTheme()">Toggle Theme</button>
<?php if ($errors): ?>
  <ul style="color:red;">
    <?php foreach ($errors as $e): ?>
      <li><?=htmlspecialchars($e)?></li>
    <?php endforeach; ?>
  </ul>
<?php endif; ?>
<form method="POST">
  <input name="email" type="email" placeholder="Email" required /><br />
  <input name="name" placeholder="Name" required /><br />
  <input name="password" type="password" placeholder="Password" required /><br />
  <input name="password_confirm" type="password" placeholder="Confirm Password" required /><br />
  <button type="submit">Register</button>
</form>
<p>Already have an account? <a href="login.php">Log in here</a></p>
</body>
</html>
