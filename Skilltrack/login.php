<?php
require 'db.php';
session_start();

$errors = [];
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = filter_var($_POST['email'], FILTER_VALIDATE_EMAIL);
    $password = $_POST['password'];

    if (!$email || !$password) {
        $errors[] = "Please enter email and password";
    } else {
        $stmt = $pdo->prepare("SELECT * FROM users WHERE email = ?");
        $stmt->execute([$email]);
        $user = $stmt->fetch();
        if ($user && password_verify($password, $user['password_hash'])) {
            $_SESSION['user_id'] = $user['id'];
            $_SESSION['user_name'] = $user['name'];
            header("Location: dashboard.php");
            exit();
        } else {
            $errors[] = "Invalid email or password";
        }
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Login</title>
  <link rel="stylesheet" href="style.css" />
  <script src="theme-toggle.js"></script>
</head>
<body>
<h2>Login</h2>
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
  <input name="password" type="password" placeholder="Password" required /><br />
  <button type="submit">Log In</button>
</form>
<p>No account? <a href="register.php">Register here</a></p>
</body>
</html>
