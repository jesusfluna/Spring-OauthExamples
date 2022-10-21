package com.spring.api.OauthApi.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.api.OauthApi.oltu.OltuOauth2Helper;

/**Micro controlador para la resolucion simple de una peticion Oauth*/
@RestController
@RequestMapping("/api/oauth")
public class OauthController {
	private String client_id = "723d7fa9860b5effda18";  // Identificador de la aplicación, valor fijo 
	private String client_secret = "5ff153778105b32cc062f6b04c83316844a439e6"; // Contraseña de la aplicacion, puede ser reseteado a otro valor 
	private String remoteEndPoint = "https://github.com/login/oauth/authorize"; // EndPoint del servidor Oauth para la autenticacion del usuario 
	private String tokenLocation = "https://github.com/login/oauth/access_token";  // Endpoint del servidor Oauth para la solicitud del token con las credenciales de la aplicación
	private String localEndPoint = "http://localhost:8081/api/oauth/token"; // EndPoint a donde redirigirá el servidor Oauth tras resolver las autenticaciones
	private String appEndPoint = ""; //EndPoint del cliente final donde se redirigirá la resolución del token.
	
	
//	@GetMapping("/")
//	public void getApiCode(HttpServletResponse response) throws IOException {
//		
//		OltuOauth2Helper oltuOuth = new OltuOauth2Helper(this.client_id, 
//        		this.client_secret, 
//        		this.remoteEndPoint, 
//        		this.tokenLocation, 
//        		this.localEndPoint);
//		
//		response.sendRedirect(oltuOuth.urlCodeRequest());
//	}

	/**
	 * Metodo para la redireccion de una peticion de autenticacion de usuario a la web de autenticacion del servidor Oauth2
	 * @param endPoint String opcional con la direccion endPoint de la aplicacion cliente
	 * @param response HttpServletResponse respuesta http
	 * @exception IOException Error en la gestion de entrada/salida de un parametro
	 * */
	@GetMapping("/")
	public void getApiCodeJson(@RequestParam(required = false) String endPoint, HttpServletResponse response){
		
		OltuOauth2Helper oltuOuth = new OltuOauth2Helper(this.client_id, 
        		this.client_secret, 
        		this.remoteEndPoint, 
        		this.tokenLocation, 
        		this.localEndPoint);
		
		if(endPoint!=null) { this.appEndPoint = endPoint; }
		
		try {
			
			response.sendRedirect(oltuOuth.urlCodeRequest());
			
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * Metodo para la peticion del token al servidor Oauth2
	 * @param code String con el code de autenticacion del usuario
	 * @param response HttpServletResponse respuesta http
	 * @exception IOException Error en la gestion de entrada/salida de un parametro
	 * */
	@GetMapping("/token")
	public void getApiToken(@RequestParam String code,HttpServletResponse response){
		String res = "";
		String endPoint= "";
		
		OltuOauth2Helper oltuOuth = new OltuOauth2Helper(this.client_id, 
        		this.client_secret, 
        		this.remoteEndPoint, 
        		this.tokenLocation, 
        		this.localEndPoint);
		
		res = oltuOuth.getToken(code);
		
		
		if(!this.appEndPoint.equals("")) {
			endPoint = this.appEndPoint;
		}else {
			endPoint = "http://localhost:8080";
		}
		
		try {
			response.sendRedirect(endPoint+"?token="+res);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
}
