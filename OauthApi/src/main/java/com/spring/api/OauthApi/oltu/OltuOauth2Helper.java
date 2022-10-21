package com.spring.api.OauthApi.oltu;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;


/** Clase encapsuladora de las funcionalidades de la libreria oltu de apache para la gestion atenticaciones en Oauth2*/
public class OltuOauth2Helper {
	private String client_id;
	private String client_secret;
	private String remoteEndPoint;
	private String tokenLocation;
	private String localEndPoint;
	
	/**
	 * Constructor por parametros de un objeto OltuOauth2Helper
	 * @param client_id identificador de la aplicación
	 * @param client_secret contraseña de la aplicación
	 * @param remoteEndPoint endpoint del servidor Oauth para la autenticacion del usuario
	 * @param tokenLocation endpoint del servidor Oaut para la autenticacion de la aplicación
	 * @param localEndPoint endpoint de la aplicacion, para la redirección de las respuestas del servidor Oauth
	 * */
	public OltuOauth2Helper(String client_id, String client_secret, String remoteEndPoint, String tokenLocation,
			String localEndPoint) {
		super();
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.remoteEndPoint = remoteEndPoint;
		this.tokenLocation = tokenLocation;
		this.localEndPoint = localEndPoint;
	}
	
	/** Constructor vacio de un objeto OltuOauth2Helper */
	public OltuOauth2Helper() { super(); }
	
	/**Metodo getter del atributo client_id
	 * @return String con el valor de client_id */
	public String getClient_id() { return client_id; }
	
	/**Metodo setter del atributo client_id
	 * @param String con un client_id*/
	public void setClient_id(String client_id) { this.client_id = client_id; }
	
	/**Metodo getter del atributo client_secret
	 * @return String con el valor de client_secret */
	public String getClient_secret() { return client_secret; }
	
	/**Metodo setter del atributo client_secret
	 * @param String con un client_secret*/
	public void setClient_secret(String client_secret) { this.client_secret = client_secret; }
	
	/**Metodo getter del atributo remoteEndPoint
	 * @return String con el valor de remoteEndPoint */
	public String getRemoteEndPoint() { return remoteEndPoint; }
	
	/**Metodo setter del atributo remoteEndPoint
	 * @param String con un remoteEndPoint*/
	public void setRemoteEndPoint(String remoteEndPoint) { this.remoteEndPoint = remoteEndPoint; }
	
	/**Metodo getter del atributo tokenLocation
	 * @return String con el valor de tokenLocation */
	public String getTokenLocation() { return tokenLocation; }
	
	/**Metodo setter del atributo tokenLocation
	 * @param String con un tokenLocation*/
	public void setTokenLocation(String tokenLocation) { this.tokenLocation = tokenLocation; }
	
	/**Metodo getter del atributo LocalEndPoint
	 * @return String con el valor de LocalEndPoint */
	public String getLocalEndPoint() { return localEndPoint; }
	
	/**Metodo setter del atributo localEndPoint
	 * @param String con un localEndPoint*/
	public void setLocalEndPoint(String localEndPoint) { this.localEndPoint = localEndPoint; }
	
	
	/**
	 * Metodo para la construccion de la url de autenticacion del usuario a partir de los atributos de clase remoteEndPoint, client_id y localEndPoint
	 * @return String con la url de redireccion para la autenticacion del usuario.
	 * @exception OAuthSystemException Error en la construccion de la url
	 * */
	public String urlCodeRequest(){
		String res = "";
		
		try {
   			 //Logica para la peticion del CODE de respuesta del servidor OAuth  
   			  OAuthClientRequest request = OAuthClientRequest
   			  .authorizationLocation(this.remoteEndPoint) .setClientId(this.client_id)
   			  .setRedirectURI(this.localEndPoint) 
   			  .buildQueryMessage();

   			  System.out.println(request.getLocationUri());
   			  
   			  //Redireccion a la url de la respuesta
   			  res = request.getLocationUri();
   			  
   			  } catch (OAuthSystemException e) { e.printStackTrace(); }
		return res;
   			 
	}
	
	
	/**
	 * Metodo para la gestion de la peticion del token al servidor Oaut2 y recogida del token devuelto por la misma
	 * @param code String con el codigo code de la autenticacion del usuario
	 * @return String token respuesta de la solicitud al servidor Oauth
	 * @exception OAuthSystemException Error en la construccion de la peticion Oauth
	 * @exception OAuthProblemException Error en la respuesta del servidor Oauth
	 * */
	public String getToken(String code){
		String res = "";
		try {
        	//Logica de la peticion del Token una vez recogido el CODE de la primera peticion al servidor Oauth
			OAuthClientRequest request = OAuthClientRequest
					.tokenLocation(this.tokenLocation)
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setClientId(this.client_id)
					.setClientSecret(this.client_secret)
					.setRedirectURI(this.localEndPoint)
					.setCode(code)
					.buildQueryMessage();
			
			//Se establece en la cabecera el formato de la respuesta, en este caso como JSON
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-Type", "application/json");
			
			OAuthClient cliente = new OAuthClient(new URLConnectionClient());

			OAuthJSONAccessTokenResponse response = cliente.accessToken(request);
			String accessToken = response.getAccessToken();
            
           res = accessToken;
            
            
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			e.printStackTrace();
		}
		
		return res;
   			 
	}
	
}
