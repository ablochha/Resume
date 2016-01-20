<?php
   $query = "select * from teachingassistant order by lastname";
   $result = mysqli_query($connection,$query);
   if (!$result) {
        die("databases query failed.");
    }
   echo "Choose a Teaching Assistant<br>";
   echo '<table width="100%" border="2"><tr><td></td><td><b>Last Name</b></td><td><b>First Name</b></td><td><b>Picture</b></td></tr>';
   while ($row = mysqli_fetch_assoc($result)) {

	echo "<tr>";	

        echo '<td><input type="radio" name="teachingassistants" value="';
        echo $row["userid"];
        echo '"></td>';
	echo "<td>" . $row["lastname"] . "</td>";
	echo "<td>" . $row["firstname"] . "</td>";
	echo '<td><img src="'.$row["tapicture"].'"height="150" width="120"></td>';

	echo "</tr>";

   }

   mysqli_free_result($result);
   echo "</table>";
?>