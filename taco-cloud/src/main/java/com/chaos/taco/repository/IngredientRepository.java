package com.chaos.taco.repository;

import com.chaos.taco.bean.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient,String> {

}
