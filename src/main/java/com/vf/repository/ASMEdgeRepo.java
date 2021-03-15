package com.vf.repository;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vf.model.ASMEdge;

@Repository
public interface ASMEdgeRepo extends JpaRepository<ASMEdge, Integer> {
	
	@Query("SELECT a FROM ASMEdge a WHERE a.app = ?1")
	Collection<ASMEdge> findASMEdgeByAppName(String appName);
}