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

<h1><center>Assign a Supervisor</center></h1>
<br>

<?php
   session_start();
   $_POST2 = $_SESSION['POSTDATA'];
   $taUserID=$_POST2["teachingassistants"];
   $profUserID=$_POST["professors"];

   if (isset($_POST['head'])) {

	$query = 'update teachingassistant set headsupervisoruserid="'.$profUserID.'" where userid="'.$taUserID.'"';
	$result=mysqli_query($connection, $query);
   	if (!$result) {
       		die("Error: update failed");
    	}

	echo "Updated Information.<br><br>";

	$query1 = 'select teachingassistant.firstname, teachingassistant.lastname, professor.firstname as pfirstname, professor.lastname as plastname from teachingassistant, professor where teachingassistant.headsupervisoruserid=professor.userid and teachingassistant.userid="'.$taUserID.'"';
	$result1=mysqli_query($connection, $query1);
   	if (!$result1) {
       		die("Error: update failed");
    	}

	echo '<table width="100%" border="2"><tr><td><b>Last Name</b></td><td><b>First Name</b></td><td><b>Head Supervisor</b></td></tr>';
   	while ($row = mysqli_fetch_assoc($result1)) {

		echo "<tr>";	

		echo "<td>" . $row["lastname"] . "</td>";
		echo "<td>" . $row["firstname"] . "</td>";
		echo "<td>" . $row["pfirstname"] . " " . $row["plastname"] . "</td>";

		echo "</tr>";

   	}

   }

   if (isset($_POST['co'])) {

	$query = 'insert into cosupervisorof values("'.$profUserID.'","'.$taUserID.'")';
	$result=mysqli_query($connection, $query);
   	if (!$result) {
       		die("Error: update failed");
    	}

	echo "Updated Information.<br><br>";

	$query1 = 'select teachingassistant.firstname, teachingassistant.lastname, professor.firstname as pfirstname, professor.lastname as plastname from teachingassistant, professor, cosupervisorof where teachingassistant.userid=cosupervisorof.tauserid and professor.userid=cosupervisorof.professoruserid and teachingassistant.userid="'.$taUserID.'"';
	$result1=mysqli_query($connection, $query1);
   	if (!$result1) {
       		die("Error: update failed");
    	}

	echo '<table width="100%" border="2"><tr><td><b>Last Name</b></td><td><b>First Name</b></td><td><b>Co-Supervisor</b></td></tr>';
   	while ($row = mysqli_fetch_assoc($result1)) {

		echo "<tr>";	

		echo "<td>" . $row["lastname"] . "</td>";
		echo "<td>" . $row["firstname"] . "</td>";
		echo "<td>" . $row["pfirstname"] . " " . $row["plastname"] . "</td>";

		echo "</tr>";

   	}
   }

?>  

<?php
   mysqli_close($connection);
?>
</body>
</html>