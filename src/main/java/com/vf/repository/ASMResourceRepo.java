package com.vf.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vf.model.ASMResource;

@Repository
public interface ASMResourceRepo extends JpaRepository<ASMResource, Integer> {

	@Query("SELECT a FROM ASMResource a WHERE a.app = ?1")
	Collection<ASMResource> findASMResourceByAppName(String appName);

}