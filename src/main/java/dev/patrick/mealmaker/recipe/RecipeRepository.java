package dev.patrick.mealmaker.recipe;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    @Query(value = "SELECT * FROM recipes WHERE " +
            "(COALESCE(:title, '') = '' OR LOWER (title) LIKE LOWER(CONCAT(:title, '%'))) AND " +
            "(COALESCE(:minCost, -1) = -1 OR total_cost >= :minCost) AND " +
            "(COALESCE(:maxCost, -1) = -1 OR total_cost <= :maxCost) AND " +
            "(COALESCE(:minCookTime, -1) = -1 OR cook_time >= :minCookTime) AND " +
            "(COALESCE(:maxCookTime, -1) = -1 OR cook_time <= :maxCookTime) AND " +
            "(COALESCE(:startDate, null) IS NULL OR created_date >= :startDate) AND " +
            "(COALESCE(:endDate, null) IS NULL OR created_date <= :endDate)",
            countQuery = "SELECT COUNT(*) FROM recipes WHERE " +
                    "(COALESCE(:title, '') = '' OR LOWER (title) LIKE LOWER(CONCAT(:title, '%'))) AND " +
                    "(COALESCE(:minCost, -1) = -1 OR total_cost >= :minCost) AND " +
                    "(COALESCE(:maxCost, -1) = -1 OR total_cost <= :maxCost) AND " +
                    "(COALESCE(:minCookTime, -1) = -1 OR cook_time >= :minCookTime) AND " +
                    "(COALESCE(:maxCookTime, -1) = -1 OR cook_time <= :maxCookTime) AND " +
                    "(COALESCE(:startDate, null) IS NULL OR created_date >= :startDate) AND " +
                    "(COALESCE(:endDate, null) IS NULL OR created_date <= :endDate)",
            nativeQuery = true)
    List<Recipe> findByFilters(
            @Param("title") String title,
            @Param("minCost") Double minCost,
            @Param("maxCost") Double maxCost,
            @Param("minCookTime") Integer minCookTime,
            @Param("maxCookTime") Integer maxCookTime,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageRequest
    );
}
