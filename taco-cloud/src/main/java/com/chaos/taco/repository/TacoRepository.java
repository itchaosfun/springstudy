package com.chaos.taco.repository;

import com.chaos.taco.bean.Taco;
import org.springframework.data.repository.CrudRepository;

public interface TacoRepository extends CrudRepository<Taco,Long> {
}
