package com.koliky.ftms.controller;

import com.koliky.ftms.model.AppRole;
import com.koliky.ftms.model.AppUser;
import com.koliky.ftms.service.AppUserService;
import com.koliky.ftms.service.JwtFilterService;
import com.koliky.ftms.service.MainService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8081")
public class AdminApiController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private MainService mainService;

    @Autowired
    private JwtFilterService jwtFilterService;

    private static class Result {
        public String status;
        public String message;

        public Result(final String status, final String message) {
            this.status = status;
            this.message = message;
        }
    }

    private static class User {
        public String employeeId;
        public String firstName;
        public String lastName;
        public String sex;
        public String department;
        public String shift;
        public String startDate;
        public List<String> roles;
    }

    @RequestMapping(value = "/admin/createuser", method = RequestMethod.POST, headers = "Content-Type=Application/json")
    public AppUser adminCreateUser(@RequestBody final User user, HttpServletRequest request) throws ServletException, ParseException {
        Claims claims = jwtFilterService.checkToken(request);
        if (mainService.checkRoleAdmin(claims)) {
            throw new ServletException("Invalid role");
        }
        AppUser appUser = new AppUser();
        appUser.setCreateDate(new Date());
        appUser.setEmployeeId(user.employeeId);
        appUser.setFirstName(user.firstName);
        appUser.setLastName(user.lastName);
        appUser.setSex(user.sex);
        appUser.setDepartment(user.department);
        appUser.setShift(user.shift);
        appUser.setStartDate(mainService.stringToDate(user.startDate));
        appUser.setChangePassword("change");
        appUser.setImageProfile("non");
        appUser.setUsername(user.employeeId);
        appUser.setPassword(mainService.hasPassword(user.employeeId));
        Set<AppRole> appRoleSet = appUser.getAppRoles();
        for(String s: user.roles) {
            AppRole appRole = new AppRole();
            appRole.setCreateDate(new Date());
            appRole.setRoleName(s);
            appRole.setAppUser(appUser);
            appRoleSet.add(appRole);
        }
        appUser.setAppRoles(appRoleSet);
        return appUserService.save(appUser);
    }

    @RequestMapping(value = "/admin/findbyid", method = RequestMethod.POST, headers = "Content-Type=Application/json")
    public AppUser findById(@RequestBody Map<String, Long> data, HttpServletRequest request) throws ServletException {
        Claims claims = jwtFilterService.checkToken(request);
        if (mainService.checkRoleAdmin(claims)) {
            throw new ServletException("Invalid role");
        }
        return appUserService.findById(data.get("id"));
    }

    @RequestMapping(value = "/admin/validateempid", method = RequestMethod.POST, headers = "Content-Type=Application/json")
    public AppUser findByEmployeeId(@RequestBody Map<String, String> data, HttpServletRequest request) throws ServletException {
        Claims claims = jwtFilterService.checkToken(request);
        if (mainService.checkRoleAdmin(claims)) {
            throw new ServletException("Invalid role");
        }
        AppUser appUser = appUserService.findByEmployeeId(data.get("employeeId"));
        if(appUser == null) {
            appUser = new AppUser();
        }
        return appUser;
    }
}