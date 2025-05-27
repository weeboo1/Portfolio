<?php
require 'db.php';
session_start();

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

$user_id = $_SESSION['user_id'];
$name = $_SESSION['user_name'];

if (!isset($_GET['id']) || !is_numeric($_GET['id'])) {
    header("Location: dashboard.php");
    exit();
}

$skill_id = (int)$_GET['id'];

// Check that skill belongs to the user
$stmt = $pdo->prepare("SELECT * FROM skills WHERE id = ? AND user_id = ?");
$stmt->execute([$skill_id, $user_id]);
$skill = $stmt->fetch();

if (!$skill) {
    header("Location: dashboard.php");
    exit();
}

// Add step
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['step_desc'])) {
    $desc = trim($_POST['step_desc']);
    if ($desc !== '') {
        $stmt = $pdo->prepare("INSERT INTO steps (skill_id, description, is_done) VALUES (?, ?, 0)");
        $stmt->execute([$skill_id, $desc]);
    }
}

// Toggle step status (done/not done)
if (isset($_GET['toggle_step']) && is_numeric($_GET['toggle_step'])) {
    $step_id = (int)$_GET['toggle_step'];
    // Check that step belongs to this skill
    $stmt = $pdo->prepare("SELECT * FROM steps WHERE id = ? AND skill_id = ?");
    $stmt->execute([$step_id, $skill_id]);
    $step = $stmt->fetch();
    if ($step) {
        $new_status = $step['is_done'] ? 0 : 1;
        $stmt = $pdo->prepare("UPDATE steps SET is_done = ? WHERE id = ?");
        $stmt->execute([$new_status, $step_id]);
    }
    header("Location: skill.php?id=$skill_id");
    exit();
}

$steps = $pdo->prepare("SELECT * FROM steps WHERE skill_id = ? ORDER BY id ASC");
$steps->execute([$skill_id]);
$steps = $steps->fetchAll();
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Skill: <?= htmlspecialchars($skill['title']) ?></title>
  <link rel="stylesheet" href="style.css" />
  <script src="theme-toggle.js"></script>
</head>
<body>
<h2>Skill: <?= htmlspecialchars($skill['title']) ?></h2>
<p><?= nl2br(htmlspecialchars($skill['description'])) ?></p>
<button onclick="toggleTheme()">Toggle Theme</button>
<a href="dashboard.php">Back to list</a> |
<a href="logout.php">Logout</a>

<h3>Steps</h3>
<ul>
  <?php foreach ($steps as $step): ?>
    <li>
      <a href="skill.php?id=<?= $skill_id ?>&toggle_step=<?= $step['id'] ?>" 
         style="text-decoration: <?= $step['is_done'] ? 'line-through' : 'none' ?>; cursor:pointer;">
         <?= htmlspecialchars($step['description']) ?>
      </a>
    </li>
  <?php endforeach; ?>
</ul>

<h3>Add Step</h3>
<form method="POST">
  <textarea name="step_desc" placeholder="Step description" required></textarea><br />
  <button type="submit">Add Step</button>
</form>
</body>
</html>
