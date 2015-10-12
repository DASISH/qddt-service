package no.nsd.qddt.domain.othermaterial.web;

import no.nsd.qddt.domain.AbstractController;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/othermaterial")
public class OtherMaterialController {

    private OtherMaterialService otherMaterialService;

    @Autowired
    public OtherMaterialController(OtherMaterialService otherMaterialService){
        this.otherMaterialService = otherMaterialService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public OtherMaterial get(@PathVariable("id") UUID id) {
        return otherMaterialService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public OtherMaterial update(@RequestBody OtherMaterial responseDomain) {
        return otherMaterialService.save(responseDomain);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public OtherMaterial create(@RequestBody OtherMaterial responseDomain) {
        return otherMaterialService.save(responseDomain);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        otherMaterialService.delete(id);
    }
}
