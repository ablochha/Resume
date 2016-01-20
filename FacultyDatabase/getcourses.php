<?php
   $query = "select * from course order by coursenumber";
   $result = mysqli_query($connection,$query);
   if (!$result) {
        die("databases query failed.");
    }
   echo "Choose a Course <br>";
   echo '<table width="100%" border="2"><tr><td></td><td><b>Course Number</b></td><td><b>Course Name</b></td></tr>';
   while ($row = mysqli_fetch_assoc($result)) {

	echo "<tr>";

	
        echo '<td><input type="radio" name="courses" value="';
        echo $row["coursenumber"];
        echo '"></td>';
	echo "<td>" . $row["coursenumber"] . "</td>";
	echo "<td>" . $row["coursename"] . "</td>";

	echo "</tr>";

   }
   mysqli_free_result($result);
   echo "</table>";
?>