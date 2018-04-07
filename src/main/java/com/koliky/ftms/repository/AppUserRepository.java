package com.koliky.ftms.repository;

import com.koliky.ftms.model.AppUser;

import java.util.List;

public interface AppUserRepository {
    List<AppUser> findAll();
    AppUser findById(Long id);
    AppUser findByUsername(String username);
    AppUser save(AppUser appUser);
    AppUser update(AppUser appUser);
}