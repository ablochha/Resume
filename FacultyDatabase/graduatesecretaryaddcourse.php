<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Assignment 3</title>
</head>
<body>
<?php
include 'connectdb.php';
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

<h1><center>Add a New Course</center></h1>
<br>

<form action="graduatesecretaryaddcourse2.php" method="post" enctype="multipart/form-data">
<table>
<tr><td>Course Name:</td><td><input type="text" name="coursename"></td></tr>
<tr><td>Course Number:</td><td><input type="text" name="coursenumber"></td></tr>
</table>
<input type="submit" value="Add Course">
</form>

<?php
   mysqli_close($connection);
?>
</body>
</html>