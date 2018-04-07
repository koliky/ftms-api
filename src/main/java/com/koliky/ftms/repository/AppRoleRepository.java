package com.koliky.ftms.repository;

import com.koliky.ftms.model.AppRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppRoleRepository extends CrudRepository<AppRole, Long> {
    List<AppRole> findAll();
}