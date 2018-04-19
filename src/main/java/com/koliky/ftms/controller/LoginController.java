package com.koliky.ftms.controller;

import com.koliky.ftms.model.AppRole;
import com.koliky.ftms.model.AppUser;
import com.koliky.ftms.service.AppUserService;
import com.koliky.ftms.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/security")
@CrossOrigin(origins = "http://localhost:8081")
public class LoginController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private MainService mainService;

    private static class UserLogin {
        public String username;
        public String password;
    }

    private static class LoginResponse {
        public String token;
        public String username;
        public List<String> roles;

        public LoginResponse(final String token, final String username, final List<String> roles) {
            this.token = token;
            this.username = username;
            this.roles = roles;
        }
    }

    private static class ResultResponse {
        public String status;

        public ResultResponse(final String status) {
            this.status = status;
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Content-Type=Application/json")
    public LoginResponse login(@RequestBody final UserLogin userLogin) throws ServletException {
        AppUser appUser = appUserService.findByUsername(userLogin.username);
        if(appUser == null || !mainService.checkPassword(userLogin.password, appUser)) {
            throw new ServletException("Invalid login");
        }
        List<String> roles = new ArrayList<>();
        for(AppRole appRole: appUser.getAppRoles()) {
            roles.add(appRole.getRoleName());
        }
        return new LoginResponse(
                mainService.createToken(appUser,roles),
                appUser.getUsername(),
                roles
        );
    }

    @RequestMapping(value = "/createuseradmin", method = RequestMethod.GET)
    public ResultResponse createUserAdmin() throws ParseException {
        AppUser appUser = new AppUser();
        appUser.setCreateDate(new Date());
        appUser.setEmployeeId("00000");
        appUser.setFirstName("Admin");
        appUser.setLastName("Admin");
        appUser.setSex("Male");
        appUser.setDepartment("MIS");
        appUser.setShift("A");
        appUser.setStartDate(mainService.stringToDate("01/01/2011"));
        appUser.setChangePassword("NoChange");
        appUser.setUsername("admin");
        appUser.setPhoneNumber("814");
        appUser.setAddress("Amata");
        appUser.setEmail("apichate@foamtecintl.com");
        appUser.setImageProfile("admin.png");
        appUser.setPassword(mainService.hasPassword("adminpassword"));
        Set<AppRole> appRoleSet = appUser.getAppRoles();
        AppRole appRole = new AppRole();
        appRole.setCreateDate(new Date());
        appRole.setRoleName("Admin");
        appRole.setAppUser(appUser);
        appRoleSet.add(appRole);
        appUser.setAppRoles(appRoleSet);
        appUserService.save(appUser);
        return new ResultResponse("createUserAdmin");
    }
}