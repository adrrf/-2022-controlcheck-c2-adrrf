package org.springframework.samples.petclinic.watch;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class WatchService {
	
	private WatchRepository watchRepo;
	
	@Autowired
	public WatchService(WatchRepository watchRepo) {
		this.watchRepo = watchRepo;
	}
	
	@Transactional(readOnly = true)
    public List<Watch> getAll(){
        return this.watchRepo.findAll();
    }

	@Transactional(readOnly = true)
    public List<WatchType> getAllWatchTypes(){
        return this.watchRepo.findAllWatchTypes();
    }

	@Transactional(readOnly = true)
	public WatchType getWatchType(String name){
        return this.watchRepo.findByName(name);
    }
	
	@Transactional(rollbackFor = { UnfeasibleWatchException.class })
    public Watch save(Watch f4) throws UnfeasibleWatchException {
		List<Watch> watches = this.watchRepo.findAll();
		List<Watch> isConcurrent = watches.stream().filter(w -> w.getDate().equals(f4.getDate()) 
				&& w.isConcurrentInTime(f4) && w.getVet().getId().equals(f4.getVet().getId())).collect(Collectors.toList());
		if (!isConcurrent.isEmpty()) {
			throw new UnfeasibleWatchException();
		}
        return this.watchRepo.save(f4);
    }
    
    public List<Watch> getOverlappedWatches(LocalDate date, LocalTime time, int vetId){
        return this.watchRepo.findOverlappedWatches(date, time, vetId);
    }

    public Page<Watch> getPaginatedWatches(Pageable p) {
        return this.watchRepo.getAllPaginated(p);
    }
}
