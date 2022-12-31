package org.springframework.samples.petclinic.watch;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchRepository extends CrudRepository<Watch, Integer>{   
    List<Watch> findAll();
    @Query("Select wt FROM WatchType wt")
    List<WatchType> findAllWatchTypes();
    @Query("Select wt FROM WatchType wt WHERE wt.name=:name")
    WatchType findByName(@Param("name") String name);
    @Query("Select w FROM Watch w WHERE w.date=:date AND w.beginTime<:time AND w.finishTime>:time AND w.vet.id=:vetId")
    List<Watch> findOverlappedWatches(@Param("date")LocalDate date, @Param("time")LocalTime time, @Param("vetId")int vetId);
    Optional<Watch> findById(int id);
    Watch save(Watch p);
    @Query("Select w FROM Watch w")
    Page<Watch> getAllPaginated(Pageable page);
 }
