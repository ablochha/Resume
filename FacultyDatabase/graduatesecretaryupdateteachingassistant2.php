<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Assignment 3</title>
</head>
<body>
<?php
include 'connectdb.php';
include 'upload_file.php';
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

<?php
   session_start();
   $_POST2 = $_SESSION['POSTDATA'];
   $firstName= $_POST["firstname"];
   $lastName = $_POST["lastname"];
   $userID=$_POST2["teachingassistants"];
   $degree=$_POST["degree"];

   $query = 'update teachingassistant set firstname="'.$firstName.'", lastName="'.$lastName.'", degree="'.$degree.'", tapicture="'.$tapicture.'" where userid="'.$userID.'"';

   $result=mysqli_query($connection, $query);
   if (!$result) {
        die("Error: insert failed");
    }
   
   echo "Updated information." . "<br><br>";

   $query2 = 'select * from teachingassistant order by lastname';
   $result2 = mysqli_query($connection, $query2);
   if (!$result2) {
   	die("database query2 failed");
   }

   echo '<table width="100%" border="2"><tr><td><b>Last Name</b></td><td><b>First Name</b></td><td><b>Student Number</b></td><td><b>User ID</b></td><td><b>Degree Type</b></td><td><b>Head Supervisor User ID</b></td><td><b>Picture</b></td></tr>';

   while ($row=mysqli_fetch_assoc($result2)) {

	echo "<tr>"; 

        echo "<td>" . $row["lastname"] . "</td>";
	echo "<td>" . $row["firstname"] . "</td>";
	echo "<td>" . $row["studentnumber"] . "</td>";
	echo "<td>" . $row["userid"] . "</td>";
	echo "<td>" . $row["degree"] . "</td>";
	echo "<td>" . $row["headsupervisoruserid"] . "</td>";
	echo '<td><img src="'.$row["tapicture"].'" height="150" width="120"></td>';
		
        echo "</tr>";

   }
   mysqli_free_result($result2);
   echo "</table>";
   
?>

<?php
   mysqli_close($connection);
?>
</body>
</html>