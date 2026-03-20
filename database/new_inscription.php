<?php
require("DEA_db.php");

$session_id=$_POST['session_id'];

$id=new_inscription($session_id);

echo '<?xml version="1.0"?>
<inscription>
	<id>'.$id.'</id>
</inscription>';

?>