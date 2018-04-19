package com.koliky.ftms.controller;

import com.koliky.ftms.model.AppUser;
import com.koliky.ftms.service.AppUserService;
import com.koliky.ftms.service.JwtFilterService;
import com.koliky.ftms.service.MainService;
import io.jsonwebtoken.Claims;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8081")
public class MainController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private MainService mainService;

    @Autowired
    private JwtFilterService jwtFilterService;

    @RequestMapping(value = "/user/getbyusername", method = RequestMethod.POST, headers = "Content-Type=Application/json")
    public AppUser getAppUser(@RequestBody Map<String, String> data, HttpServletRequest request) throws ServletException {
        Claims claims = jwtFilterService.checkToken(request);
        AppUser appUser = appUserService.findByUsername((String) claims.get("username"));
        return appUser;
    }

    @RequestMapping(value = "/user/get-image-profile/{username}", method = RequestMethod.GET)
    public void getImageProfile(@PathVariable("username") String username, HttpServletResponse response) throws IOException, ServletException {
        AppUser appUser = appUserService.findByUsername(username);
        String pathProfileImage = System.getProperty("user.dir") + "/images-profile/user.png";
        if("non".indexOf(appUser.getImageProfile()) < 0) {
            pathProfileImage = System.getProperty("user.dir") + "/images-profile/" + appUser.getImageProfile();
        }
        File file = new File(pathProfileImage);
        FileInputStream fis = new FileInputStream(file);
        byte[] byteData = IOUtils.toByteArray(fis);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        response.setHeader("Content-Disposition", "inline;filename=" + appUser.getImageProfile());
        response.getOutputStream().write(byteData);
    }
}