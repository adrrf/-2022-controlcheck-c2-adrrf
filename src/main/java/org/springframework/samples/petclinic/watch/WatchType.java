package org.springframework.samples.petclinic.watch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class WatchType extends BaseEntity {
	@Column(unique = true)
	@NotNull
	@Size(min = 3, max = 30)
    String name;
	
	@NotNull
    Boolean overtime;
}
