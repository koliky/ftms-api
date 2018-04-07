package com.koliky.ftms.controller;

import com.koliky.ftms.model.AppRole;
import com.koliky.ftms.model.AppUser;
import com.koliky.ftms.repository.AppUserRepository;
import com.koliky.ftms.service.JwtFilter;
import com.koliky.ftms.service.MainService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AdminApiController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private MainService mainService;

    @Autowired
    private JwtFilter jwtFilter;

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
    public AppUser adminCreateUser(@RequestBody final User user, ServletRequest request) throws ServletException, ParseException, IOException {
        Claims claims = jwtFilter.checkToken(request);
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
        return appUserRepository.save(appUser);
    }
}