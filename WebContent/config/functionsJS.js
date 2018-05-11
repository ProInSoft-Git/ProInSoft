
//Funcion para campo numerico
var nav4plus = window.Event ? true : false;
function solonumeros(evt){
 var key = nav4plus ? evt.which : evt.keyCode;
 var its_char = String.fromCharCode(key);
 //alert ("vr:"+key);
 if( key != 0 && key != 13){ //este if es para que acepte el enter
  if( key == 8){return true}
  if( key == 46){return true} // acepta punto
  var mask_string =/\d/
  return mask_string.test(its_char.toString())? true : false
 }
}

//Ajax por Post
function genera_ajax(div,direccion,datos,efecto) 
{
	div="#"+div;
	if(efecto==1)
	{
		$(div).hide("slide", { direction: "right" }, 300);	
	}
	$.ajax({
		  data: datos,		  
		  type: "POST",
		  url: direccion,
		  success: function(a) 
		  {
			  if(efecto==1)
			  {
			  	  $(div).show("slide", { direction: "left" }, 200).html(a);
			  }
			  else
			  {
				  $(div).html(a);
			  }
		  }
		});
}

//Decimales
var nav4plus = window.Event ? true : false;
function decimales(evt,id)
{
  var key = nav4plus ? evt.which : evt.keyCode;
  var its_char = String.fromCharCode(key);
  
  //Captura el valor del campo
  var cadena=document.getElementById(id).value;
  //Valida si ya existe la interseccion decimal en el campo
  var pos = cadena.indexOf(".");
   
  if( key != 0 && key != 13)
  { //este if es para que acepte el enter
   if( key == 8){return true}
   if( key == 46)//Deja ingresar el punto una vez
   {	   
	   if(cadena.indexOf('.') != -1)//Valida si ya existe el punto en la cadena para dejarlo ingresar
	   {
		return false
	   }
	   return true
   } 
   if(pos!= -1)//Si existe el punto en la cadena deja ingresar solamente dos digitos decimales despues
   {
	  if(cadena.length>pos+2)
	  {
		  return false
	  }
   }
   var mask_string =/\d/
   return mask_string.test(its_char.toString())? true : false
  }
}

//Campo de fecha
function campoFecha(id)
{
	$(function () {
		$("#"+id).datepicker({ autoclose: true }).val();
	});
}

//Modal
function generarModal(clase,porcentajeAncho,alto,recargaPagina,overLay,codigoAlCerrar)
{
	$(document).ready(function() 
	{
		anchoPage=$(window).width();
		porcentaje=porcentajeAncho;
		if(anchoPage<600)
		{
			porcentaje=90;
		}
		//alert(anchoPage);
		ancho=anchoPage*porcentaje/100;
		$("."+clase).colorbox
		(
		  {
			  iframe:true, width:ancho, height:alto,overlayClose:overLay,scrolling:true
			  ,onClosed:function()
			  { 
				 if(codigoAlCerrar!="")
				 {
					 eval(codigoAlCerrar);
				 }
				 if(recargaPagina==1)
				 {
					 location.reload(true); 
				 }			 
			  }
		  }
		);
		//Example of preserving a JavaScript event for inline calls.
		$("#click").click(function(){ 
			$('#click').css({"background-color":"black", "color":"#fff", "cursor":"inherit"}).text("Open this window again and this message will still be here.");
			return false;
		});
	});
}
