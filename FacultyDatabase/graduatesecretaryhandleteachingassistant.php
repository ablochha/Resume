<?php

session_start();
$_SESSION['POSTDATA'] = $_POST;

if (isset($_POST['show'])) {
	
	header('Location: http://ec2-54-173-9-181.compute-1.amazonaws.com/Assignment3/graduatesecretaryteachingassistant.php');

}

if (isset($_POST['add'])) {
	
	header('Location: http://ec2-54-173-9-181.compute-1.amazonaws.com/Assignment3/graduatesecretaryaddteachingassistant.php');

}

if (isset($_POST['delete'])) {
	
	header('Location: http://ec2-54-173-9-181.compute-1.amazonaws.com/Assignment3/graduatesecretarydeleteteachingassistant.php');

}

if (isset($_POST['update'])) {

	header('Location: http://ec2-54-173-9-181.compute-1.amazonaws.com/Assignment3/graduatesecretaryupdateteachingassistant.php');

}

if (isset($_POST['assign'])) {
	
	header('Location: http://ec2-54-173-9-181.compute-1.amazonaws.com/Assignment3/graduatesecretaryassignsupervisor.php');

}	

?>