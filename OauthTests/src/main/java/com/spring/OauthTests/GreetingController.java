package com.spring.OauthTests;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.OauthTests.Oltu.OltuOauth2Helper;

import models.Usuario;

@Controller
public class GreetingController {
	private String client_id = "723d7fa9860b5effda18"; //Client_id de la aplicacion
	private String client_secret = "5ff153778105b32cc062f6b04c83316844a439e6"; //Client_secret de la aplicacion
	private String remoteEndPoint = "https://github.com/login/oauth/authorize"; //URL de autorizacion del servidor OAuth2
	private String tokenLocation = "https://github.com/login/oauth/access_token"; //URL de peticion del token del servidor
	private String localEndPoint = "http://localhost:8080/oauth/code"; //URL del callback de nuestra aplicacion para la gestion del CODE
	
	private OltuOauth2Helper oltuOuth;
	
    @GetMapping("/")
    public String showForm(Model model) {
        Usuario user = new Usuario("","");
        model.addAttribute("user", user);
         
        
        return "index";
    }
    
    @PostMapping("/")
    public String registerUser(@ModelAttribute("user") Usuario user,Model model) {
    	boolean correcto = false;
    	//Para la simulacion de la autenticacion del usuario se establece por codigo que el unico usuario correcto es user:12345
    	if(user.getNombre().equals("user") && user.getContrasena().equals("12345")) {
    		correcto = true;
    	}
    	
        System.out.println(user.toString()+" : [Estado] = "+correcto);
        model.addAttribute("correcto", correcto);
        return "greeting";
    }
    
    @GetMapping("/oauth")
    public String oauth(Model model) {
    	Usuario user = new Usuario("","");
        model.addAttribute("user", user);
        
        this.oltuOuth = new OltuOauth2Helper(this.client_id, 
	        		this.client_secret, 
	        		this.remoteEndPoint, 
	        		this.tokenLocation, 
	        		this.localEndPoint);

        return "redirect:"+this.oltuOuth.urlCodeRequest();
        	
        
    }
    
    @GetMapping("/oauth/code")
    public String oauth(@RequestParam String code,Model model) {
    	boolean error = true;
    	String token = "";

    	token = this.oltuOuth.getToken(code);
    	if(!token.equals("")) { error = false; }
            

        model.addAttribute("token", token);
        model.addAttribute("error", error);
        
        return "token";
    }

}
