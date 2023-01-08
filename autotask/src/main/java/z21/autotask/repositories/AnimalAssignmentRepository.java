package z21.autotask.repositories;

import z21.autotask.entities.AnimalAssignment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional @Repository
public interface AnimalAssignmentRepository extends JpaRepository<AnimalAssignment, Integer>{
   
}
