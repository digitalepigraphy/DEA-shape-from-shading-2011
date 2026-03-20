<?php
require("DEA_db.php");

function getRealIpAddr()
{
    if (!empty($_SERVER['HTTP_CLIENT_IP']))   //check ip from share internet
    {
      $ip=$_SERVER['HTTP_CLIENT_IP'];
    }
    elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR']))   //to check ip is pass from proxy
    {
      $ip=$_SERVER['HTTP_X_FORWARDED_FOR'];
    }
    else
    {
      $ip=$_SERVER['REMOTE_ADDR'];
    }
    return $ip;
}

$ip=getRealIpAddr();
$id= new_session($ip);

echo '<?xml version="1.0"?>
<session>
	<id>'.$id.'</id>
	<ip>'.$ip.'</ip>
</session>';

?>