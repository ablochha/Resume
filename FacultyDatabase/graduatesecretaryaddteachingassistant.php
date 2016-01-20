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

<h1><center>Add a New Teaching Assistant</center></h1>
<br>

<form action="graduatesecretaryaddteachingassistant2.php" method="post" enctype="multipart/form-data">
<table>
<tr><td>First Name:</td><td><input type="text" name="firstname"></td></tr>
<tr><td>Last Name:</td><td><input type="text" name="lastname"></td></tr>
<tr><td>Student Number:</td><td><input type="text" name="studentnumber"></td></tr>
<tr><td>User ID:</td><td><input type="text" name="userid"></td></tr>
<tr><td>Degree Type:</td><td><input type="radio" name="degree" value="PhD">PhD
<input type="radio" name="degree" value="Masters">Masters</td></tr>
<tr><td>Head Supervisor:</td><td><input type="text" name="headsupervisor"></td></tr>
<tr><td>TA Picture:</td><td><input type="file" name="file" id="file"></td></tr>
</table>
<input type="submit" name="addta" value="Add Teaching Assistant">
</form>

<?php

   mysqli_close($connection);

?>
</body>
</html>