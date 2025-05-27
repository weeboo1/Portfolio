<?php
require 'db.php';
session_start();

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

$user_id = $_SESSION['user_id'];
$name = $_SESSION['user_name'];

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['title'])) {
    $title = $_POST['title'];
    $desc = $_POST['description'];
    $stmt = $pdo->prepare("INSERT INTO skills (user_id, title, description) VALUES (?, ?, ?)");
    $stmt->execute([$user_id, $title, $desc]);
}

$skills = $pdo->prepare("SELECT * FROM skills WHERE user_id = ? ORDER BY id DESC");
$skills->execute([$user_id]);
$skills = $skills->fetchAll();

$progress = [];
foreach ($skills as $skill) {
    $stepData = $pdo->prepare("SELECT COUNT(*) as total, SUM(is_done) as done FROM steps WHERE skill_id = ?");
    $stepData->execute([$skill['id']]);
    $data = $stepData->fetch();
    $progress[] = [
        'title' => $skill['title'],
        'percent' => ($data['total'] > 0) ? round(($data['done'] / $data['total']) * 100) : 0
    ];
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>SkillTrack</title>
  <link rel="stylesheet" href="style.css">
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <script src="theme-toggle.js"></script> 
</head>
<body>
<h2>Hello, <?= htmlspecialchars($name) ?>!</h2>
<button onclick="toggleTheme()">Toggle Theme</button>
<a href="logout.php">Logout</a>

<h3>Your Skills</h3>
<ul>
  <?php foreach ($skills as $skill): ?>
    <li><a href="skill.php?id=<?= $skill['id'] ?>"><?= htmlspecialchars($skill['title']) ?></a></li>
  <?php endforeach; ?>
</ul>

<h3>Add a Skill</h3>
<form method="POST">
  <input name="title" placeholder="Skill Title" required><br>
  <textarea name="description" placeholder="Description"></textarea><br>
  <button type="submit">Add</button>
</form>

<h3>Skills Progress</h3>
<canvas id="progressChart" width="400" height="200"></canvas>
<script>
const ctx = document.getElementById('progressChart');
new Chart(ctx, {
  type: 'bar',
  data: {
    labels: <?= json_encode(array_column($progress, 'title')) ?>,
    datasets: [{
      label: '% Completed',
      data: <?= json_encode(array_column($progress, 'percent')) ?>,
      backgroundColor: 'rgba(54, 162, 235, 0.6)',
      borderColor: 'rgba(54, 162, 235, 1)',
      borderWidth: 1
    }]
  },
  options: {
    scales: {
      y: { beginAtZero: true, max: 100 }
    }
  }
});
</script>
</body>
</html>
