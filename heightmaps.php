<?php
$dea=$_GET['dea'];
header('Content-Type: image/png');
if ((strpos($dea,'/')===false)&&(strpos($dea,'\\')===false)&&(strpos($dea,'%')===false))
readfile('http://www.digitalworlds.ufl.edu/angelos/lab/DEA/heightmaps/'.$fnm.'.png');
else readfile('http://www.digitalworlds.ufl.edu/angelos/lab/DEA/heightmaps/d1a7a25fdaf3010d.png');
?>