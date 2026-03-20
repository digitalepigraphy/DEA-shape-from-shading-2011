<?php
function new_session($ip){
	$link = mysql_connect('localhost', 'tragouda_EB', 'digitalworlds1') or die('Could not connect: ' . mysql_error());
	
	mysql_select_db('tragouda_EB') or die('Could not select database');

	// Performing SQL query
	$result = mysql_query("INSERT INTO dea_sessions (id,ip,created,updated) VALUES (substring(MD5(RAND()), -16),'".$ip."',NOW(),NOW()) ON DUPLICATE KEY UPDATE id=substring(MD5(RAND()), -16)") or die('Query failed: ' . mysql_error());
	// Free resultset
	//mysql_free_result($result);
	
	// Performing SQL query
	$result = mysql_query("SELECT id FROM dea_sessions WHERE ip='".$ip."' ORDER BY created DESC LIMIT 1 ") or die('Query failed: ' . mysql_error());
	
	$ret=0;
	while ($line = mysql_fetch_array($result, MYSQL_ASSOC)) {
		foreach ($line as $col_value) {
			$ret=$col_value;
		}
	}
	// Free resultset
	mysql_free_result($result);

	// Closing connection
	mysql_close($link);
	return $ret;
}

function new_inscription($session_id){
	$link = mysql_connect('localhost', 'tragouda_EB', 'digitalworlds1') or die('Could not connect: ' . mysql_error());
	
	mysql_select_db('tragouda_EB') or die('Could not select database');

	// Performing SQL query
	$result = mysql_query("INSERT INTO dea_inscriptions (id,created_by_session_id,created) VALUES (substring(MD5(RAND()), -16),'".$session_id."',NOW()) ON DUPLICATE KEY UPDATE id=substring(MD5(RAND()), -16)") or die('Query failed: ' . mysql_error());
	// Free resultset
	//mysql_free_result($result);
	
	// Performing SQL query
	$result = mysql_query("SELECT id FROM dea_inscriptions WHERE created_by_session_id='".$session_id."' ORDER BY created DESC LIMIT 1 ") or die('Query failed: ' . mysql_error());
	
	$ret=0;
	while ($line = mysql_fetch_array($result, MYSQL_ASSOC)) {
		foreach ($line as $col_value) {
			$ret=$col_value;
		}
	}
	// Free resultset
	mysql_free_result($result);

	// Closing connection
	mysql_close($link);
	return $ret;
}

function new_image($session_id,$url){
	$link = mysql_connect('localhost', 'tragouda_EB', 'digitalworlds1') or die('Could not connect: ' . mysql_error());
	
	mysql_select_db('tragouda_EB') or die('Could not select database');

	if(strlen($url)!=0)
	{
	// Performing SQL query
	$result = mysql_query("INSERT INTO dea_images (id,created_by_session_id,created,url) VALUES (substring(MD5(RAND()), -16),'".$session_id."',NOW(),'".$url."') ON DUPLICATE KEY UPDATE id=substring(MD5(RAND()), -16)") or die('Query failed: ' . mysql_error());
	}
	else
	{
	// Performing SQL query
	$result = mysql_query("INSERT INTO dea_images (id,created_by_session_id,created) VALUES (substring(MD5(RAND()), -16),'".$session_id."',NOW()) ON DUPLICATE KEY UPDATE id=substring(MD5(RAND()), -16)") or die('Query failed: ' . mysql_error());
	}
	// Free resultset
	//mysql_free_result($result);
	
	// Performing SQL query
	$result = mysql_query("SELECT id FROM dea_images WHERE created_by_session_id='".$session_id."' ORDER BY created DESC LIMIT 1 ") or die('Query failed: ' . mysql_error());
	
	$ret=0;
	while ($line = mysql_fetch_array($result, MYSQL_ASSOC)) {
		foreach ($line as $col_value) {
			$ret=$col_value;
		}
	}
	// Free resultset
	mysql_free_result($result);

	// Closing connection
	mysql_close($link);
	return $ret;
}

function get_fields(){
	$link = mysql_connect('localhost', 'tragouda_EB', 'digitalworlds1') or die('Could not connect: ' . mysql_error());
	
	mysql_select_db('tragouda_EB') or die('Could not select database');

	// Performing SQL query
	$result = mysql_query("SELECT id, name, description, example, type_id, ord, group_id FROM dea_fields ORDER BY ord ASC") or die('Query failed: ' . mysql_error());
	
	$fh = fopen('fields/index.xml', 'w');
	fwrite($fh,'<?xml version="1.0"?><fields>');
	
	
	while ($line = mysql_fetch_array($result, MYSQL_ASSOC)) {
	    echo '<field>';
		fwrite($fh,'<field>');
		$i=0;
		foreach ($line as $col_value) {
			if($i==0)
			{
			   echo '<id>'.$col_value.'</id>';
			   fwrite($fh,'<id>'.$col_value.'</id>');
			}
			else if($i==1)
			{
				echo '<name>'.$col_value.'</name>';
				fwrite($fh,'<name>'.$col_value.'</name>');
			}
			else if($i==2) 
			{
				echo '<description>'.$col_value.'</description>';
				fwrite($fh,'<description>'.$col_value.'</description>');
			}
			else if($i==3) 
			{
				echo '<example>'.$col_value.'</example>';
				fwrite($fh,'<example>'.$col_value.'</example>');
			}
			else if($i==4)
			{
				echo '<type_id>'.$col_value.'</type_id>';
				fwrite($fh,'<type_id>'.$col_value.'</type_id>');
			}
			else if($i==5) 
			{
				echo '<ord>'.$col_value.'</ord>';
				fwrite($fh,'<ord>'.$col_value.'</ord>');
			}
			else if($i==6)
			{
				echo '<group_id>'.$col_value.'</group_id>';
				fwrite($fh,'<group_id>'.$col_value.'</group_id>');
			}
			$i=$i+1;
		}
		echo '</field>';
		fwrite($fh,'</field>');
	}
	fwrite($fh,'</fields>');
	fclose($fh);

	// Free resultset
	mysql_free_result($result);

	// Closing connection
	mysql_close($link);
}

function get_field_groups(){
	$link = mysql_connect('localhost', 'tragouda_EB', 'digitalworlds1') or die('Could not connect: ' . mysql_error());
	
	mysql_select_db('tragouda_EB') or die('Could not select database');

	// Performing SQL query
	$result = mysql_query("SELECT id, name, ord FROM dea_field_groups ORDER BY ord ASC") or die('Query failed: ' . mysql_error());
	
	$fh = fopen('fields/groups.xml', 'w');
	fwrite($fh,'<?xml version="1.0"?><field_groups>');
	
	while ($line = mysql_fetch_array($result, MYSQL_ASSOC)) {
	    echo '<field_group>';
		fwrite($fh,'<field_group>');
		$i=0;
		foreach ($line as $col_value) {
			if($i==0) 
			{
			   echo '<id>'.$col_value.'</id>';
			   fwrite($fh,'<id>'.$col_value.'</id>');
			}
			else if($i==1)
            {
     			echo '<name>'.$col_value.'</name>';
				fwrite($fh,'<name>'.$col_value.'</name>');
			}
			else if($i==2)
			{
				echo '<ord>'.$col_value.'</ord>';
				fwrite($fh,'<ord>'.$col_value.'</ord>');
			}
			$i=$i+1;
		}
		echo '</field_group>';
		fwrite($fh,'</field_group>');
	}
	fwrite($fh,'</field_groups>');
	fclose($fh);
	
	// Free resultset
	mysql_free_result($result);

	// Closing connection
	mysql_close($link);
}

function update_history($record_id){
	$link = mysql_connect('localhost', 'tragouda_EB', 'digitalworlds1') or die('Could not connect: ' . mysql_error());
	
	mysql_select_db('tragouda_EB') or die('Could not select database');

	// Performing SQL query
	$result = mysql_query("select dea_strings.field_id, dea_strings.value, dea_strings.created, dea_strings.created_by_session_id from dea_strings, dea_fields where  dea_strings.inscription_id='".$record_id."' and dea_strings.field_id=dea_fields.id order by dea_strings.created ASC") or die('Query failed: ' . mysql_error());
	
	$fh = fopen('history/'.$record_id.'.xml', 'w');
	fwrite($fh,'<?xml version="1.0"?><history>');
	
	while ($line = mysql_fetch_array($result, MYSQL_ASSOC)) {
		fwrite($fh,'<event>');
		$i=0;
		foreach ($line as $col_value) {
			if($i==0) 
			{
			   fwrite($fh,'<field_id>'.$col_value.'</field_id>');
			}
			else if($i==1) 
			{
			   fwrite($fh,'<field_value>'.$col_value.'</field_value>');
			}
			else if($i==2) 
			{
			   fwrite($fh,'<created>'.$col_value.'</created>');
			}
			else if($i==3) 
			{
			   fwrite($fh,'<session_id>'.$col_value.'</session_id>');
			}
			$i=$i+1;
		}
		fwrite($fh,'</event>');
	}
	fwrite($fh,'</history>');
	fclose($fh);
	
	// Free resultset
	mysql_free_result($result);

	// Closing connection
	mysql_close($link);
}

function get_record($record_id){
	$link = mysql_connect('localhost', 'tragouda_EB', 'digitalworlds1') or die('Could not connect: ' . mysql_error());
	
	mysql_select_db('tragouda_EB') or die('Could not select database');

	// Performing SQL query
	$result = mysql_query("select dea_strings.field_id, dea_strings.value from dea_strings, dea_fields where  dea_strings.inscription_id='".$record_id."' and dea_strings.deleted is null and dea_strings.field_id=dea_fields.id order by dea_fields.ord ASC, dea_strings.created  DESC") or die('Query failed: ' . mysql_error());
	
	$fh = fopen('records/'.$record_id.'.xml', 'w');
	fwrite($fh,'<?xml version="1.0"?><record>');
	
	while ($line = mysql_fetch_array($result, MYSQL_ASSOC)) {
	    echo '<field>';
		fwrite($fh,'<field>');
		$i=0;
		foreach ($line as $col_value) {
			if($i==0) 
			{
			   echo '<field_id>'.$col_value.'</field_id>';
			   fwrite($fh,'<field_id>'.$col_value.'</field_id>');
			}
			else if($i==1) 
			{
			   echo '<field_value>'.$col_value.'</field_value>';
			   fwrite($fh,'<field_value>'.$col_value.'</field_value>');
			}
			$i=$i+1;
		}
		echo '</field>';
		fwrite($fh,'</field>');
	}
	fwrite($fh,'</record>');
	fclose($fh);
	
	// Free resultset
	mysql_free_result($result);

	// Closing connection
	mysql_close($link);
	
	update_history($record_id);
}

function get_images($record_id){
	$link = mysql_connect('localhost', 'tragouda_EB', 'digitalworlds1') or die('Could not connect: ' . mysql_error());
	
	mysql_select_db('tragouda_EB') or die('Could not select database');

	// Performing SQL query
	$result = mysql_query("select dea_images.id, dea_images.url from dea_strings, dea_images where dea_strings.inscription_id='".$record_id."' and dea_strings.field_id=1000 and dea_strings.deleted is null and dea_strings.value=dea_images.id order by dea_images.created DESC") or die('Query failed: ' . mysql_error());
	
	$fh = fopen('records/'.$record_id.'_img.xml', 'w');
	fwrite($fh,'<?xml version="1.0"?><record>');
	
	while ($line = mysql_fetch_array($result, MYSQL_ASSOC)) {
	    echo '<image>';
		fwrite($fh,'<image>');
		$i=0;
		foreach ($line as $col_value) {
			if($i==0) 
			{
			   echo '<image_id>'.$col_value.'</image_id>';
			   fwrite($fh,'<image_id>'.$col_value.'</image_id>');
			}
			else if($i==1) 
			{
			   echo '<image_url>'.$col_value.'</image_url>';
			   fwrite($fh,'<image_url>'.$col_value.'</image_url>');
			}
			$i=$i+1;
		}
		echo '</image>';
		fwrite($fh,'</image>');
	}
	fwrite($fh,'</record>');
	fclose($fh);
	
	// Free resultset
	mysql_free_result($result);

	// Closing connection
	mysql_close($link);
}

function get_sample_values($field_id){
	//$link = mysql_connect('localhost', 'tragouda_EB', 'digitalworlds1') or die('Could not connect: ' . mysql_error());
	
//	mysql_select_db('tragouda_EB') or die('Could not select database');

	// Performing SQL query
	$result = mysql_query("select value from dea_strings where field_id='".$field_id."' and dea_strings.deleted is null group by value order by value") or die('Query failed: ' . mysql_error());
	
	$fh = fopen('fields/'.$field_id.'.xml', 'w');
	fwrite($fh,'<?xml version="1.0"?><sample_values>');
	
	while ($line = mysql_fetch_array($result, MYSQL_ASSOC)) {
	    //echo '<samples>';
		fwrite($fh,'<samples>');
		$i=0;
		foreach ($line as $col_value) {
			if($i==0) 
			{
			   //echo '<value>'.$col_value.'</value>';
			   fwrite($fh,'<value>'.$col_value.'</value>');
			}
			$i=$i+1;
		}
		//echo '</samples>';
		fwrite($fh,'</samples>');
	}
	fwrite($fh,'</sample_values>');
	fclose($fh);
	
	// Free resultset
	mysql_free_result($result);

	// Closing connection
	//mysql_close($link);
}

function get_all_sample_values(){
	$link = mysql_connect('localhost', 'tragouda_EB', 'digitalworlds1') or die('Could not connect: ' . mysql_error());
	
	mysql_select_db('tragouda_EB') or die('Could not select database');

	// Performing SQL query
	$result = mysql_query("select id from dea_fields") or die('Query failed: ' . mysql_error());
	
	
	while ($line = mysql_fetch_array($result, MYSQL_ASSOC)) {
		$i=0;
		foreach ($line as $col_value) {
			if($i==0) 
			{
			   echo ''.$col_value.'...';
			   get_sample_values($col_value);
			   echo 'done<br>';
			}
			$i=$i+1;
		}
	}
	
	// Free resultset
	mysql_free_result($result);

	// Closing connection
	mysql_close($link);
}

function upload_record($session_id, $record_id, $field_id, $field_value){
	$link = mysql_connect('localhost', 'tragouda_EB', 'digitalworlds1') or die('Could not connect: ' . mysql_error());
	
	mysql_select_db('tragouda_EB') or die('Could not select database');

	// Performing SQL query
	$result = mysql_query("UPDATE dea_strings  SET deleted_by_session_id='".$session_id."', deleted=NOW() where field_id=".$field_id." AND inscription_id='".$record_id."' and deleted is null") or die('Query failed: ' . mysql_error());
	
	// Performing SQL query
	$result = mysql_query("INSERT INTO dea_strings (created_by_session_id, created, value, field_id, inscription_id) VALUES('".$session_id."', NOW(), '".$field_value."', ".$field_id.", '".$record_id."')") or die('Query failed: ' . mysql_error());
	
	// Free resultset
	//mysql_free_result($result);

	// Closing connection
	mysql_close($link);
}

function upload_record2($session_id, $record_id, $field_id, $field_value){
	$link = mysql_connect('localhost', 'tragouda_EB', 'digitalworlds1') or die('Could not connect: ' . mysql_error());
	
	mysql_select_db('tragouda_EB') or die('Could not select database');

	// Performing SQL query
	$result = mysql_query("INSERT INTO dea_strings (created_by_session_id, created, value, field_id, inscription_id) VALUES('".$session_id."', NOW(), '".$field_value."', ".$field_id.", '".$record_id."')") or die('Query failed: ' . mysql_error());
	
	// Free resultset
	//mysql_free_result($result);

	// Closing connection
	mysql_close($link);
}

function search_record($session_id, $field_id, $field_value, $num_of_fields, $conjunction){
	$link = mysql_connect('localhost', 'tragouda_EB', 'digitalworlds1') or die('Could not connect: ' . mysql_error());
	
	mysql_select_db('tragouda_EB') or die('Could not select database');

	$q="select count(dea_inscriptions.id), dea_inscriptions.id from dea_inscriptions, dea_strings where dea_inscriptions.id=dea_strings.inscription_id and dea_strings.deleted is null and dea_inscriptions.deleted is null ";
	if($num_of_fields>0)
	{
	$q=$q."and(";
	for($i=0;$i<$num_of_fields;$i++)
	{
		$q=$q."(dea_strings.field_id=".$field_id[$i]." and dea_strings.value LIKE '".$field_value[$i]."')";
	    if($i<$num_of_fields-1) $q=$q."or";
	}
	$q=$q.")";
	}
	$q=$q."group by dea_inscriptions.id";
	if($num_of_fields-1>0)	
	{
		if($conjunction=="and")
			$q=$q." having count(dea_inscriptions.id)>".($num_of_fields-1)." order by count(dea_inscriptions.id) desc";
		else if($conjunction=="or")
			$q=$q." order by count(dea_inscriptions.id) desc";
	}
//	$f=fopen("last_search_query.txt","w");
//	fwrite($f,$q);
//	fclose($f);
	
	// Performing SQL query
	$result = mysql_query($q) or die('Query failed: ' . mysql_error());
	
	while ($line = mysql_fetch_array($result, MYSQL_ASSOC)) {
	    echo '<record>';
		$i=0;
		foreach ($line as $col_value) {
			if($i==0 && $num_of_fields>0) 
			{	
				$m=(($col_value*100)/$num_of_fields);
				if($m>100)$m=100;
				echo '<match>'.$m.'</match>';
			}
			if($i==1) echo '<id>'.$col_value.'</id>';
			$i=$i+1;
		}
		echo '</record>';
	}
	
	// Free resultset
	mysql_free_result($result);

	// Closing connection
	mysql_close($link);
}


?>