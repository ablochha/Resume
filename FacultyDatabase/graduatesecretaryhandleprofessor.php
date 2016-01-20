<?php

session_start();
$_SESSION['POSTDATA'] = $_POST;

if (isset($_POST['show'])) {
	
	header('Location: http://ec2-54-173-9-181.compute-1.amazonaws.com/Assignment3/graduatesecretarysupervisor.php');

}

if (isset($_POST['add'])) {
	
	header('Location: http://ec2-54-173-9-181.compute-1.amazonaws.com/Assignment3/graduatesecretaryaddprofessor.php');

}

if (isset($_POST['delete'])) {
	
	header('Location: http://ec2-54-173-9-181.compute-1.amazonaws.com/Assignment3/graduatesecretarydeleteprofessor.php');

}	

?>