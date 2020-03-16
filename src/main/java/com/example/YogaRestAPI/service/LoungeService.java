package com.example.YogaRestAPI.service;

import com.example.YogaRestAPI.domain.Lounge;
import com.example.YogaRestAPI.errors.Lounge.LoungeExistsException;
import com.example.YogaRestAPI.repos.LoungeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoungeService {

    private final LoungeRepo loungeRepo;

    @Autowired
    public LoungeService(LoungeRepo loungeRepo) {
        this.loungeRepo = loungeRepo;
    }

    public List<Lounge> findAll() {
        return loungeRepo.findAll();
    }

    public Page<Lounge> findAllPaginated(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return loungeRepo.findAll(pageable);
    }

    public Lounge save(Lounge lounge) {
        return loungeRepo.save(lounge);
    }

    public void deleteById(Long id) {
        loungeRepo.deleteById(id);
    }

    public Optional<Lounge> findById(Long id) {
        return loungeRepo.findById(id);
    }

    public void checkLoungeExist(Lounge lounge) {
        Lounge loungeFromDb = loungeRepo.findByName(lounge.getName());
        if (loungeFromDb != null) {
            throw new LoungeExistsException(lounge.getName());
        }
    }

    public void checkForUpdate(Lounge lounge) {
        Lounge loungeFromDb = loungeRepo.findByName(lounge.getName());
        if (loungeFromDb != null && (!loungeFromDb.getId().equals(lounge.getId()))) {
            throw new LoungeExistsException(lounge.getName());
        }
    }

    public Lounge patch(Lounge patch, Lounge target) {
        if (patch.getName() != null) {
            target.setName(patch.getName());
        }
        if (patch.getAddress() != null) {
            target.setAddress(patch.getAddress());
        }
        if (patch.getCapacity() != 0) {
            target.setCapacity(patch.getCapacity());
        }
        if (patch.getStartTime() != null) {
            target.setStartTime(patch.getStartTime());
        }
        if (patch.getFinishTime() != null) {
            target.setFinishTime(patch.getFinishTime());
        }
        if (patch.getActivities() != null) {
            target.setActivities(patch.getActivities());
        }
        if (patch.getActivityTypes() != null) {
            target.setActivityTypes(patch.getActivityTypes());
        }
        return target;
    }
}
