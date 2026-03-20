<?php
require("DEA_db.php");

function getFileExtension($fileName)
{
   $parts=explode(".",$fileName);
   return $parts[count($parts)-1];
}

$session_id=$_POST['session_id'];
$image_id=$_POST['image_id'];
$error_id=0;
$id=0;

/*if ((($_FILES["file"]["type"] == "image/png")
|| ($_FILES["file"]["type"] == "image/jpeg")
|| ($_FILES["file"]["type"] == "image/pjpeg"))
if ($_FILES["file"]["size"] < 20000))*/
if(1==1)
  {
  if ($_FILES["file"]["error"] > 0)
    {
    //echo "Return Code: " . $_FILES["file"]["error"] . "<br />";
	$error_id=1;
    }
  else
    {
    //echo "Upload: " . $_FILES["file"]["name"] . "<br />";
    //echo "Type: " . $_FILES["file"]["type"] . "<br />";
    //echo "Size: " . ($_FILES["file"]["size"] / 1024) . " Kb<br />";
    //echo "Temp file: " . $_FILES["file"]["tmp_name"] . "<br />";

	$filename="thumbnails/" . $image_id.".".getFileExtension($_FILES["file"]["name"]);
	
    if (file_exists($filename))
      {
      //echo $filename . " already exists. ";
	  $error_id=2;
      }
    else
      {
      if(move_uploaded_file($_FILES["file"]["tmp_name"],$filename)){}else $error_id=3;
      //echo "Stored in: " . "upload/" . $_FILES["file"]["name"];
      }
    }
  }
else
  {
  $error_id=4;
  }



echo '<?xml version="1.0"?>
<image>
	<id>'.$image_id.'</id>
	<error>'.$error_id.'</error>
</image>';

?>