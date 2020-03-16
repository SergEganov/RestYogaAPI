package com.example.YogaRestAPI.controllers;

import com.example.YogaRestAPI.assemblers.LoungeLightModelAssembler;
import com.example.YogaRestAPI.domain.Lounge;
import com.example.YogaRestAPI.errors.Lounge.LoungeNotFoundException;
import com.example.YogaRestAPI.models.LoungeLightModel;
import com.example.YogaRestAPI.models.LoungeModel;
import com.example.YogaRestAPI.service.LoungeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/lounges", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class LoungeController {

    private final LoungeService loungeService;

    @Autowired
    public LoungeController(LoungeService loungeService) {
        this.loungeService = loungeService;
    }

    @ApiOperation("Получить список всех помещений")
    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(value = "pageN", defaultValue = "0") Integer page,
                                                @RequestParam(value = "size", defaultValue = "5") Integer size) {
        Page<Lounge> loungesPaged = loungeService.findAllPaginated(page, size);


       /* CollectionModel<LoungeModel> lounges = new LoungeModelAssembler().toCollectionModel(loungesPaged);
        lounges.add(linkTo(methodOn(LoungeController.class).findAll(page, size)).withRel("lounges"));*/

        CollectionModel<LoungeLightModel> lounges = new LoungeLightModelAssembler().toCollectionModel(loungesPaged);
        lounges.add(linkTo(methodOn(LoungeController.class).findAll(page, size)).withRel("lounges"));

        return new ResponseEntity<>(lounges,HttpStatus.OK);
    }

    @ApiOperation("Получить помещение по id")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        Lounge lounge = loungeService.findById(id).orElseThrow(() -> new LoungeNotFoundException(id));
        LoungeModel loungeModel = new LoungeModel(lounge);
        loungeModel.add(linkTo(methodOn(LoungeController.class).findById(id)).withRel("lounge"));
        return new ResponseEntity<>(loungeModel, HttpStatus.OK);
    }

    @ApiOperation("Создать новое помещение")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createNew(@RequestBody Lounge lounge) {
        loungeService.checkLoungeExist(lounge);
        LoungeModel loungeModel = new LoungeModel(loungeService.save(lounge));
        loungeModel.add(linkTo(methodOn(LoungeController.class).findById(lounge.getId())).withRel("lounge"));
        return new ResponseEntity<>(lounge, HttpStatus.CREATED);
    }

    @ApiOperation("Обновить существующее помещение или создать новое, если такого нет")
    @PutMapping("/{id}")
    public ResponseEntity saveOrUpdate(@RequestBody Lounge lounge, @PathVariable Long id) {
        return loungeService.findById(id)
                .map(lng -> {
                    BeanUtils.copyProperties(lounge, lng);
                    lng.setId(id);
                    loungeService.checkForUpdate(lng);
                    loungeService.save(lng);
                    LoungeModel loungeModel = new LoungeModel(lng);
                    loungeModel.add(linkTo(methodOn(LoungeController.class)
                            .saveOrUpdate(lounge, id))
                            .withRel("lounge"));
                    return new ResponseEntity<>(loungeModel, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    loungeService.checkLoungeExist(lounge);
                    LoungeModel loungeModel = new LoungeModel(loungeService.save(lounge));
                    loungeModel.add(linkTo(methodOn(LoungeController.class)
                            .findById(id))
                            .withRel("lounge"));
                    return new ResponseEntity<>(loungeModel, HttpStatus.CREATED);
                });
    }

    @ApiOperation("Обновить параметры помещения")
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patch(@RequestBody Lounge patch, @PathVariable Long id) {
        Lounge lounge = loungeService.findById(id).orElseThrow(() -> new LoungeNotFoundException(id));
        patch.setId(id);
        loungeService.checkForUpdate(patch);
        LoungeModel loungeModel = new LoungeModel(loungeService.save(loungeService.patch(patch, lounge)));
        loungeModel.add(linkTo(methodOn(LoungeController.class).patch(patch, id)).withRel("lounge"));
        return new ResponseEntity<>(loungeModel, HttpStatus.OK);
    }

    @ApiOperation("Удалить помещение")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        loungeService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
