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

<h1><center>Assign a Teaching Assistant</center></h1>
<br>

<form action="graduatesecretaryassigncourse2.php" method="post">
<?php
include 'getteachingassistants.php';
?>
<br>Number of Students Taking this Course: <input type="text" name="numberofstudents">
<input type="submit" value="Assign Selected Teaching Assistant">
<br></form>

<?php
   mysqli_close($connection);
?>
</body>
</html>