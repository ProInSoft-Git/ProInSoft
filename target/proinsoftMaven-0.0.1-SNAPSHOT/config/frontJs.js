//Mensajes de alerta
function alerts(tipoMensaje,mensaje)
{
	alerta ='<div class="alert alert-'+tipoMensaje+'" id="divAlertAction">' ;
	alerta+=  mensaje;
	alerta+= '<span class="tools pull-right">' ;
	alerta+= '<a href="javascript:;" onclick="document.getElementById(\'divAlertAction\').style.display=\'none\';">';
	alerta+=  '<i class="fa fa-times"></i>';
	alerta+=' </a>';
	alerta+=' </span>';
	alerta+='</div>';		
	return alerta;
}
function imgCargando(path) 
{
	cad  =  '<div class="divCentrado">'; 
	cad=cad+'<img src="'+path+'/img/input-spinner.gif">';
	cad=cad+'</div>';
	$("body").append(cad);
}