<?php
$dbhost2 = "localhost";
$dbuser2= "root";
$dbpass2 = "cs3319";
$dbname2 = "login";
$connection2 = mysqli_connect($dbhost2, $dbuser2,$dbpass2,$dbname2);
if (mysqli_connect_errno()) {
     die("database connection failed :" .
     mysqli_connect_error() .
     "(" . mysqli_connect_errno() . ")"
         );
    }
?>