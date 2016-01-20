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

<?php
   $courseName= $_POST["coursename"];
   $courseNumber = $_POST["coursenumber"];

   $query = 'insert into course values("' . $courseNumber . '","' . $courseName . '")';
   $result=mysqli_query($connection, $query);
   if (!$result) {
        die("Error: insert failed");
    }
   
   echo $courseNumber . ", " . $courseName . " was added as a Course." . "<br><br>";

   $query2 = 'select * from course order by coursenumber';
   $result2 = mysqli_query($connection, $query2);
   if (!$result2) {
   	die("database query2 failed");
   }

   echo '<table width="100%" border="2"><tr><td><b>Course Number</b></td><td><b>Course Name</b></td></tr>';

   while ($row=mysqli_fetch_assoc($result2)) {

	echo "<tr>"; 

        echo "<td>" . $row["coursenumber"] . "</td>";
	echo "<td>" . $row["coursename"] . "</td>";	
	
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