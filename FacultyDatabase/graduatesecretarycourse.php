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
   $whichCourse= $_POST["courses"];
   $whichTerm= $_POST["term"];
   $whichYear= $_POST["year"];

   $query = 'select * from course where coursenumber="'.$whichCourse.'"';
   $result=mysqli_query($connection,$query);
   $row=mysqli_fetch_assoc($result);
   echo "Viewing the teaching assistants assigned to " . $row["coursenumber"] . ", " . $row["coursename"] . "<br><br>";

   $query1 = 'select teachingassistant.firstname, teachingassistant.lastname, teachingassistant.userid, teachingassistant.tapicture, teachingassistant.degree, assignedto.numberofstudents from teachingassistant, assignedto where teachingassistant.userid=assignedto.tauserid and assignedto.coursenumber="'.$whichCourse.'" and assignedto.term="'.$whichTerm.'" and assignedto.year="'.$whichYear.'" order by teachingassistant.lastname';
   $result1=mysqli_query($connection,$query1);
   if (!$result1) {
   	die("database query1 failed.");
   }
   echo '<table width="100%" border="2"><tr><td><b>Last Name</b></td><td><b>First Name</b></td><td><b>User ID</b></td><td><b>Degree Type</b></td><td><b>Number of Students</b></td><td><b>Picture</b></td></tr>';
   while ($row=mysqli_fetch_assoc($result1)) {

        echo "<tr>"; 

        echo "<td>" . $row["lastname"] . "</td>";
	echo "<td>" . $row["firstname"] . "</td>";
	echo "<td>" . $row["userid"] . "</td>";
	echo "<td>" . $row["degree"] . "</td>";
	echo "<td>" . $row["numberofstudents"] . "</td>";
	echo '<td><img src="'.$row["tapicture"].'"height="150" width="120"></td>';
	
        echo "</tr>";
   	
   }
   mysqli_free_result($result1);
   echo "</table>";
?>
<?php
   mysqli_close($connection);
?>
</body>
</html>