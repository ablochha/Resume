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

<h1><center>Update an Existing Teaching Assistant</center></h1>
<br>

Please completely fill in the following information:<br>
<?php
$_POST = $_SESSION['POSTDATA'];
$query = 'select * from teachingassistant where userid="'.$_POST["teachingassistants"].'"';
$result = mysqli_query($connection, $query);
$row = mysqli_fetch_assoc($result);

echo '<form action="graduatesecretaryupdateteachingassistant2.php" method="post" enctype="multipart/form-data">';
echo '<table><tr><td>First Name:</td><td><input type="text" name="firstname" value="'.$row["firstname"].'"></td></tr>';
echo '<tr><td>Last Name:</td><td><input type="text" name="lastname" value="'.$row["lastname"].'"></td></tr>';
echo '<tr><td>Degree Type:</td><td><input type="radio" name="degree" value="PhD">PhD';
echo '<input type="radio" name="degree" value="Masters">Masters</td></tr>';
echo '<tr><td>TA Picture:</td><td><input type="file" name="file" id="file"></td></tr></table>';
echo '<input type="submit" value="Update Teaching Assistant"></form>';

   mysqli_close($connection);
?>
</body>
</html>