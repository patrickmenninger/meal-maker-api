package dev.patrick.mealmaker.repository;

import dev.patrick.mealmaker.recipes.Recipe;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CustomRecipeRepository {

    public static final double MAX_COST = 200.00;
    public static final int LOW_COOK_TIME = 0;
    public static final int UP_COOK_TIME = 2880;

    @Autowired
    MongoTemplate mongoTemplate;

    public List<Recipe> findRecipes(HttpServletRequest req) {

        Map<String, String[]> params = req.getParameterMap();

        String title = "";
        int cookTimeUp = UP_COOK_TIME;
        int cookTimeLow = LOW_COOK_TIME;
        String totalCost = "200.00";

        System.out.println(req.getQueryString());

        if (params.get("title") != null && !params.get("title")[0].equals("")) {

            System.out.println(params.get("title")[0]);
            title = params.get("title")[0];
        }

        if (params.get("cookTime") != null && !params.get("cookTime")[0].equals("")) {

            String[] cookTimes = params.get("cookTime")[0].split("_");

            int maxCookTime = Integer.parseInt(cookTimes[1].split("-")[1]);
            int minCookTime = Integer.parseInt(cookTimes[1].split("-")[0]);

            for (int i = 1; i < cookTimes.length; i++) {

                System.out.println(cookTimes[i]);

                if (Integer.parseInt(cookTimes[i].split("-")[0]) < minCookTime) {
                    minCookTime = Integer.parseInt(cookTimes[i].split("-")[0]);
                }

                if (Integer.parseInt(cookTimes[i].split("-")[1]) > maxCookTime) {
                    maxCookTime = Integer.parseInt(cookTimes[i].split("-")[1]);
                }

            }

            cookTimeUp = maxCookTime;
            cookTimeLow = minCookTime;
            System.out.println("low " + cookTimeLow + " high " + cookTimeUp);
        }

        if (params.get("totalCost") != null && !params.get("totalCost")[0].equals("")) {
            System.out.println(params.get("totalCost")[0]);
            totalCost = params.get("totalCost")[0];
        }

        Query query = new Query();

        query.addCriteria(Criteria.where("title").regex(".*" + title + ".*", "i"));
        query.addCriteria(Criteria.where("cookTime").lte(cookTimeUp).gte(cookTimeLow));
        query.addCriteria(Criteria.where("totalCost").lt(Double.parseDouble(totalCost)));

        return mongoTemplate.find(query, Recipe.class, "recipes");

    }

}
