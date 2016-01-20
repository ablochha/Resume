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

<h1><center>Assign a Teaching Assistant</center></h1>
<br>

<?php
   session_start();
   $_POST2 = $_SESSION['POSTDATA'];
   $courseNumber=$_POST2["courses"];
   $term=$_POST2["term"];
   $year=$_POST2["year"];
   $userID=$_POST["teachingassistants"];
   $numberOfStudents=$_POST["numberofstudents"];
  
   $query = 'insert into assignedto values("'.$userID.'","'.$courseNumber.'",'.$year.','.$numberOfStudents.',"'.$term.'")';

   $result=mysqli_query($connection, $query);
   if (!$result) {
        die("Error: insert failed");
    }
   
   echo "Updated information." . "<br><br>";

   $query2 = 'select teachingassistant.firstname, teachingassistant.lastname, teachingassistant.userid, teachingassistant.tapicture, assignedto.term, assignedto.year, assignedto.numberofstudents, assignedto.coursenumber from teachingassistant, assignedto where teachingassistant.userid=assignedto.tauserid and assignedto.coursenumber="'.$courseNumber.'" order by teachingassistant.firstname';
   $result2 = mysqli_query($connection, $query2);
   if (!$result2) {
   	die("database query2 failed");
   }

   echo '<table width="100%" border="2"><tr><td><b>Course Number</b></td><td><b>Teaching Assistant</b></td><td><b>Term</b></td><td><b>Year</b></td><td><b>Number of Students</b></td><td><b>Picture</b></td></tr>';

   while ($row=mysqli_fetch_assoc($result2)) {

	echo "<tr>"; 

        echo "<td>" . $row["coursenumber"] . "</td>";
	echo "<td>" . $row["firstname"] . " " . $row["lastname"] . "</td>";
	echo "<td>" . $row["term"] . "</td>";
	echo "<td>" . $row["year"] . "</td>";
	echo "<td>" . $row["numberofstudents"] . "</td>";
	echo '<td><img src="'.$row["tapicture"].'"height="150" width="120"></td>';
		
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