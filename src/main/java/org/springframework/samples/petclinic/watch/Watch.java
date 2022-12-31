package org.springframework.samples.petclinic.watch;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.vet.Vet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Watch extends BaseEntity {
	
	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate date;
	
	@NotNull
	@Column(name = "start")
	@DateTimeFormat(pattern = "HH:mm:ss")
    LocalTime beginTime;

	@NotNull
	@Column(name = "end")
	@DateTimeFormat(pattern = "HH:mm:ss")
    LocalTime finishTime;

    @ManyToOne(optional=false)
    @NotNull
    Vet vet;

    @ManyToOne(optional=false)
    @NotNull
    WatchType type;

    @Transient
    public long getDuration(){   // Expressed in minutes
        Duration duration=Duration.between(beginTime, finishTime);
        return duration.toMinutes();
    }

    @Transient
    public boolean isConcurrentInTime(Watch w){
        return !this.finishTime.isBefore(w.beginTime) && !this.beginTime.isAfter(w.finishTime); 
    } 

}
