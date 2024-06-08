package dev.patika.vetManagementSystem.api;

import dev.patika.vetManagementSystem.bussiness.abstracts.IAnimalService;
import dev.patika.vetManagementSystem.bussiness.abstracts.ICustomerService;
import dev.patika.vetManagementSystem.core.config.modelmapper.IModelMapperService;
import dev.patika.vetManagementSystem.core.config.utilies.Msg;
import dev.patika.vetManagementSystem.core.config.utilies.ResultHelper;
import dev.patika.vetManagementSystem.core.exception.NotFoundException;
import dev.patika.vetManagementSystem.core.result.ListResult;
import dev.patika.vetManagementSystem.core.result.Result;
import dev.patika.vetManagementSystem.core.result.ResultData;
import dev.patika.vetManagementSystem.dto.request.animal.SaveAnimalRequest;
import dev.patika.vetManagementSystem.dto.request.animal.UpdateAnimalRequest;
import dev.patika.vetManagementSystem.dto.response.animal.AnimalResponse2;
import dev.patika.vetManagementSystem.dto.response.availableDateResponse.AvailableDateResponse2;
import dev.patika.vetManagementSystem.entities.Animal;
import dev.patika.vetManagementSystem.entities.Customer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/animal")
@RequiredArgsConstructor
public class AnimalController {

    private final IAnimalService animalService;
    private final IModelMapperService modelMapper;
    private final ICustomerService customerService;


    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public  ResultData<List<AnimalResponse2>> getAllAnimals() {
        return animalService.getAllAnimals();
    }


    @GetMapping("/customer/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse2>> getAnimalByCustomerName(@PathVariable("name") String name) {
        return animalService.getAnimalByCustomerName(name);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AnimalResponse2> save(@Valid @RequestBody SaveAnimalRequest saveAnimalRequest) {
        try {
            Animal response = this.modelMapper.forRequest().map(saveAnimalRequest, Animal.class);
            Customer customer = this.customerService.get(saveAnimalRequest.getCustomer().getId());
            response.setCustomer(customer);
            this.animalService.save(response);
            return ResultHelper.created(this.modelMapper.forResponse().map(response, AnimalResponse2.class));

        }catch (Exception e) {
            return ResultData.error(Msg.VALIDATE_ERROR, "500");
        }

    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ListResult<AnimalResponse2> findByName2(@PathVariable("name") String name){
        return animalService.findByName2(name);
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") int id) {
        boolean isDeleted = this.animalService.delete(id);

        if (isDeleted) {
            return ResultHelper.ok();
        } else {
            return ResultHelper.notFoundError(Msg.NOT_FOUND);
        }
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse2> update(@Valid @RequestBody UpdateAnimalRequest updateAnimalRequest) {
        Long animalId = updateAnimalRequest.getId();
        try {
            Animal updatedAnimal = this.animalService.get(animalId);
            this.modelMapper.forRequest().map(updateAnimalRequest, updatedAnimal);
            Customer customer = this.customerService.get(updateAnimalRequest.getCustomer().getId());
            updatedAnimal.setCustomer(customer);
            this.animalService.update(updatedAnimal);
            AnimalResponse2 animalResponse = this.modelMapper.forResponse().map(updatedAnimal, AnimalResponse2.class);
            return ResultHelper.success(animalResponse);
        }catch (Exception e) {
            return ResultData.error(Msg.NOT_FOUND, "404");
        }

    }


}

