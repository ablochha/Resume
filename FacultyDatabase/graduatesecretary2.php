<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Assignment 3</title>
</head>

<body>

<?php
include 'connectdb.php';
session_start();
?>

<form action="professorindex1.php" method="post">
<input type="submit" value="Home">
</form>

<h1><center>Welcome Graduate Secretary</center></h1>
<br>
View Databases:<br><br>

<form action="graduatesecretary2.php" method="post">
<input type="submit" value="Professor">
</form><br>

<form action="graduatesecretary3.php" method="post">
<input type="submit" value="Course">
</form><br>

<form action="graduatesecretary4.php" method="post">
<input type="submit" value="Teaching Assistant">
</form><br><br><hr><br><br>

<form action="graduatesecretaryhandleprofessor.php" method="post">
<?php
include 'getprofessors.php';
?>
<br>

<div style='float:left'>
<input type="submit" name="show" value="Show Supervised Teaching Assistants">
<input type="submit" name="add" value="Add a New Professor">
<input type="submit" name="delete" value="Delete Selected Professor">
</form></div>

<?php
mysqli_close($connection);
?>
</body>
</html>