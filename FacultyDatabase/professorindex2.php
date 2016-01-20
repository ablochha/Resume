<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Assignment 3</title>
</head>

<body>

<?php
include 'login.php';
include 'connectdb.php';

if (isset($_POST['login'])) {

	$password = strtolower($_POST['password']);
	$query = 'select password from password where password="'.$password.'" limit 1';
	$result = mysqli_query($connection2, $query);

	if (mysqli_num_rows($result) == 1) {
		
		header('Location: http://ec2-54-173-9-181.compute-1.amazonaws.com/Assignment3/graduatesecretary1.php');

	}	

}

?>

<form method="post">
Password: <input type="text" name="password">
<input type="submit" name="login" value="Login">
</form>

<h1><center>Welcome Professors</center></h1>
<br>
Search by:<br><br>

<form action="professorindex2.php" method="post">
<input type="submit" value="Professor">
</form><br>

<form action="professorindex3.php" method="post">
<input type="submit" value="Course">
</form><br>

<form action="professorindex4.php" method="post">
<input type="submit" value="Teaching Assistant">
</form><br><br><hr><br><br>

<form action="professorsupervisor.php" method="post">
<?php
include 'getprofessors.php';
?>
<br>
<input type="submit" value="Show Supervised Teaching Assistants">
</form>

<?php
mysqli_close($connection);
mysqli_close($connection2);
?>
</body>
</html>