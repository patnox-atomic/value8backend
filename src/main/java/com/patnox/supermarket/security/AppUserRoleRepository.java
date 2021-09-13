package com.patnox.supermarket.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public interface AppUserRoleRepository extends JpaRepository<AppUserRole, Long> 
{
	Optional<AppUserRole> findByName(String name);
}
