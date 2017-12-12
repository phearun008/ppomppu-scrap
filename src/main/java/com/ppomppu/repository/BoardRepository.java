package com.ppomppu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppomppu.model.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer>{

}
