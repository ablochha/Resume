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
</form><br><br><hr><br><br>


<?php
   session_start();
   $_POST = $_SESSION['POSTDATA'];
   $whichOwner= $_POST["professors"];

   $query = 'select * from professor where userid="' . $whichOwner . '"';
   $result=mysqli_query($connection,$query);
   $row=mysqli_fetch_assoc($result);
   echo "Viewing: Professor " . $row["firstname"] . " " . $row["lastname"] . "<br><br>";

   $query1 = 'select teachingassistant.firstname, teachingassistant.lastname, teachingassistant.userid, teachingassistant.tapicture, teachingassistant.degree from professor, teachingassistant where professor.userid=teachingassistant.headsupervisoruserid and professor.userid="' . $whichOwner . '" order by teachingassistant.lastname';
   $result1=mysqli_query($connection,$query1);

   echo "Headsupervises:" . "<br>";
    if (!$result1) {
         die("database query1 failed.");
     }
 
   echo '<table width="100%" border="2"><tr><td><b>Last Name</b></td><td><b>First Name</b></td><td><b>User ID</b></td><td><b>Degree Type</b></td><td><b>Picture</b></td></tr>';

    while ($row=mysqli_fetch_assoc($result1)) {
      
	echo "<tr>"; 

        echo "<td>" . $row["lastname"] . "</td>";
	echo "<td>" . $row["firstname"] . "</td>";
	echo "<td>" . $row["userid"] . "</td>";
	echo "<td>" . $row["degree"] . "</td>";
	echo '<td><img src="'.$row["tapicture"].'"height="150" width="120"></td>';
	
        echo "</tr>";

     }
     mysqli_free_result($result1);
   echo "</table>";
   echo "<br><br>";

   $query2 = 'select teachingassistant.firstname, teachingassistant.lastname, teachingassistant.tapicture, teachingassistant.degree, teachingassistant.userid from cosupervisorof, teachingassistant where cosupervisorof.tauserid=teachingassistant.userid and cosupervisorof.professoruserid="'.$whichOwner.'"';
   $result2=mysqli_query($connection, $query2);
   echo "Cosupervises:" . "<br>";
   if (!$result2) {
   	die("database query2 failed.");
   }

   echo '<table width="100%" border="2"><tr><td><b>Last Name</b></td><td><b>First Name</b></td><td><b>User ID</b></td><td><b>Degree Type</b></td><td><b>Picture</b></td></tr>';

   while ($row=mysqli_fetch_assoc($result2)) {

	echo "<tr>"; 

        echo "<td>" . $row["lastname"] . "</td>";
	echo "<td>" . $row["firstname"] . "</td>";
	echo "<td>" . $row["userid"] . "</td>";
	echo "<td>" . $row["degree"] . "</td>";
	echo '<td><img src="'.$row["tapicture"].'"height="150" width="120"></td>';
	
        echo "</tr>";

   	
   }
 
   echo "</table>";

   mysqli_free_result($result2);
?>
<?php
   mysqli_close($connection);
?>
</body>
</html>