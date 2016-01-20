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
   $whichTeachingAssistant= $_POST["teachingassistants"];

   $query = 'select * from teachingassistant where userid="'.$whichTeachingAssistant.'"';
   $result=mysqli_query($connection,$query);
   $row=mysqli_fetch_assoc($result);
   echo "Viewing the courses assigned to " . $row["firstname"] . " " . $row["lastname"] . "<br><br>";

   $query1 = 'select assignedto.coursenumber, assignedto.term, assignedto.year, teachingassistant.degree from teachingassistant, assignedto where teachingassistant.userid=assignedto.tauserid and teachingassistant.userid="'.$whichTeachingAssistant.'" order by coursenumber';
   $result1=mysqli_query($connection,$query1);
   if (!$result1) {
   	die("database query1 failed.");
   }
   
   if (mysqli_num_rows($result1) > 8 && $row["degree"] == "PhD") {

	echo "<br><b>Note: This student has been assigned to too many courses.</b>";

   }

   if (mysqli_num_rows($result1) > 3 && $row["degree"] == "Masters") {

	echo "<br><b>Note: This student has been assigned to too many courses.</b>";

   }

   echo '<table width="100%" border="2"><tr><td><b>Course Number</b></td><td><b>Term</b></td><td><b>Year</b></td></tr>';

   while ($row=mysqli_fetch_assoc($result1)) {

	echo "<tr>"; 

        echo "<td>" . $row["coursenumber"] . "</td>";
	echo "<td>" . $row["term"] . "</td>";
	echo "<td>" . $row["year"] . "</td>";
	
        echo "</tr>";
  	
   }

   echo "</table>";
   mysqli_free_result($result1);
   
?>

<?php
   mysqli_close($connection);
?>
</body>
</html>