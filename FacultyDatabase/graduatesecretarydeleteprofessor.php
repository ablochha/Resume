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

<h1><center>Delete a Professor</center></h1>
<br>

<?php
   session_start();
   $_POST = $_SESSION['POSTDATA'];
   $userID=$_POST["professors"];

   $queryd = 'delete from cosupervisorof where professoruserid="'.$userID.'"';
   $resultd = mysqli_query($connection, $queryd);
   if (!$resultd) {
        die("Error deleting from cosupervisorof");
   }

   $query = 'delete from professor where userid="'.$userID.'"';
   $result=mysqli_query($connection, $query);
   if (!$result) {
        die("Please make sure that you assign new head supervisors for Teaching Assistant's, or delete Teaching Assistants before deleting their professor");
   }
	
	
   echo "User ID: " . $userID . " was deleted from the database.<br><br>";

   $query2 = 'select * from professor order by lastname';
   $result2 = mysqli_query($connection, $query2);
   if (!$result2) {
   	die("database query2 failed");
   }

   echo '<table width="100%" border="2"><tr><td><b>Last Name</b></td><td><b>First Name</b></td><td><b>User ID</b></td></tr>';

   while ($row=mysqli_fetch_assoc($result2)) {

	echo "<tr>"; 

        echo "<td>" . $row["lastname"] . "</td>";
	echo "<td>" . $row["firstname"] . "</td>";
	echo "<td>" . $row["userid"] . "</td>";
		
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