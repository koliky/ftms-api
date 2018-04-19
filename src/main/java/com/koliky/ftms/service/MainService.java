package com.koliky.ftms.service;

import com.koliky.ftms.model.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MainService {

    final String SECRET_KEY = "pacific03srv";

    public boolean checkPassword(String password, AppUser appUser) {
        return BCrypt.checkpw(password, appUser.getPassword());
    }

    public Date stringToDate(String strDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(strDate);
    }

    public String hasPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public String createToken(AppUser appUser, List<String> roles) {

        return Jwts.builder().setSubject("token")
                .claim("username", appUser.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(DatePlusMinutes(60 * 24))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Date DatePlusMinutes(int minutes) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, minutes);
        return now.getTime();
    }

    public boolean checkRoleAdmin(Claims claims) {
        List<String> roles = (List<String>) claims.get("roles");
        if(roles.indexOf("Admin") >= 0) {
            return false;
        }
        return true;
    }

    public boolean checkRoleUser(Claims claims) {
        List<String> roles = (List<String>) claims.get("roles");
        if(roles.indexOf("User") >= 0) {
            return false;
        }
        return true;
    }
}