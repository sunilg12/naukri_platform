package com.naukri.central_api.utility;

import com.naukri.central_api.exception.UnAuthorizedException;
import com.naukri.central_api.model.AppUser;
import com.naukri.central_api.service.UserService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class AuthUtil {

    UserService userService;

    @Autowired
    public AuthUtil(UserService userService){
        this.userService = userService;
    }

    @Value("${secret.password}")
    String secretPassword;

    Long expirationTime = 1800000L;

    public String generateToken(String email, String password, String role){

        String payload = email + ":" + password + ":" + role;
        String jwtToken = Jwts.builder().setExpiration(new Date(System.currentTimeMillis()+expirationTime))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secretPassword)
                .setSubject(payload)
                .compact();

        return jwtToken;
    }

    public String extractTokenFromBearerToken(String Authorization){
        return Authorization.substring(7);
    }

    public String decrptJwtToken(String token){
        System.out.println("Decrypt " + token);
        String payload = Jwts.parser().setSigningKey(secretPassword)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return payload;
    }

    public boolean validateToken(String token){
        System.out.println("Validate " + token);
        String payload = this.decrptJwtToken(token);
        String[] details = payload.split(":");
        String email = details[0];
        System.out.println(email);
        String password = details[1];
        System.out.println(password);
        //String role = details[2];
        // should validate the email and password that it's belonging to user or not
        // authutil should call userservice to validate email and password that it belongs to correct user or not
        return userService.validateCredentials(email, password);
    }

    public String generateTokenFromLoginDetails(String email, String password){
        boolean resp = userService.validateCredentials(email, password);
        if(!resp){
            throw new UnAuthorizedException("Invalid Credentials");
        }
        AppUser user = userService.getUserByEmail(email);

        return generateToken(email,password,user.getUserType());
    }
}
