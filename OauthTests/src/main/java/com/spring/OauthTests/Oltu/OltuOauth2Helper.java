package com.spring.OauthTests.Oltu;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;


public class OltuOauth2Helper {
	private String client_id;
	private String client_secret;
	private String remoteEndPoint;
	private String tokenLocation;
	private String localEndPoint;
	
	public OltuOauth2Helper(String client_id, String client_secret, String remoteEndPoint, String tokenLocation,
			String localEndPoint) {
		super();
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.remoteEndPoint = remoteEndPoint;
		this.tokenLocation = tokenLocation;
		this.localEndPoint = localEndPoint;
	}
	
	public OltuOauth2Helper() {
		super();
	}
	
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getClient_secret() {
		return client_secret;
	}
	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}
	public String getRemoteEndPoint() {
		return remoteEndPoint;
	}
	public void setRemoteEndPoint(String remoteEndPoint) {
		this.remoteEndPoint = remoteEndPoint;
	}
	public String getTokenLocation() {
		return tokenLocation;
	}
	public void setTokenLocation(String tokenLocation) {
		this.tokenLocation = tokenLocation;
	}
	public String getLocalEndPoint() {
		return localEndPoint;
	}
	public void setLocalEndPoint(String localEndPoint) {
		this.localEndPoint = localEndPoint;
	}
	
	
	public String urlCodeRequest(){
		String res = "";
		
		try {
   			/* Logica para la peticion del CODE de respuesta del servidor OAuth*/  
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
	
	public String getToken(String code){
		String res = "";
		try {
        	/* Logica de la peticion del Token una vez recogido el CODE 
        	 * de la primera peticion al servidor Oauth*/
			OAuthClientRequest request = OAuthClientRequest
					.tokenLocation(this.tokenLocation)
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setClientId(this.client_id)
					.setClientSecret(this.client_secret)
					.setRedirectURI(this.localEndPoint)
					.setCode(code)
					.buildQueryMessage();
			
			/* Se establece en la cabecera el formato de la respuesta, en este caso como JSON*/
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
