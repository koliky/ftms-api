package com.koliky.ftms.service;

import com.koliky.ftms.model.AppUser;
import com.koliky.ftms.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    public AppUser save(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public AppUser findById(Long id) {
        return appUserRepository.findById(id);
    }

    public AppUser findByEmployeeId(String employeeId) {
        return appUserRepository.findByEmployeeId(employeeId);
    }
}
