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
</form><br><br><hr>

<h1><center>Assign a Supervisor</center></h1>
<br>

<form action="graduatesecretaryassignsupervisor2.php" method="post">
<?php
include 'getprofessors.php';
?>
<br>
<input type="submit" name="head" value="Assign Selected Professor as Headsupervisor">
<input type="submit" name="co" value="Assign Selected Professor as Co-Supervisor">
<br></form>

<?php
   mysqli_close($connection);
?>
</body>
</html>