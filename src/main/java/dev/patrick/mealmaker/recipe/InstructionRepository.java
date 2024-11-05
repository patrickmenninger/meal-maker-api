package dev.patrick.mealmaker.recipe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructionRepository extends JpaRepository<Instruction, Integer> {
}
