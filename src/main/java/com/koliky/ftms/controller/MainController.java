package com.koliky.ftms.controller;

import com.koliky.ftms.model.AppUser;
import com.koliky.ftms.repository.AppUserRepository;
import com.koliky.ftms.service.JwtFilter;
import com.koliky.ftms.service.MainService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MainController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private MainService mainService;

    @Autowired
    private JwtFilter jwtFilter;

    private static class RequestUsername {
        public String username;

        public RequestUsername(final String username) {
            this.username = username;
        }
    }

    @RequestMapping(value = "/user/getbyusername", method = RequestMethod.POST, headers = "Content-Type=Application/json")
    public AppUser getAppUser(@RequestBody final RequestUsername user, ServletRequest request) throws ServletException {
        Claims claims = jwtFilter.checkToken(request);
        if (mainService.checkRoleUser(claims)) {
            throw new ServletException("Invalid role");
        }
        AppUser appUser = appUserRepository.findByUsername(user.username);
        return appUser;
    }
}