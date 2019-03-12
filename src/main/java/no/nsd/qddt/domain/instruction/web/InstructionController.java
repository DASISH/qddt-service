package no.nsd.qddt.domain.instruction.web;

import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instruction.InstructionService;
import no.nsd.qddt.domain.instruction.json.InstructionJsonEdit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@RequestMapping("/instruction")
public class InstructionController {

    private final InstructionService service;

    @Autowired
    public InstructionController(InstructionService service) {
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public InstructionJsonEdit get(@PathVariable("id") UUID id) {
        return new InstructionJsonEdit(service.findOne(id));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public InstructionJsonEdit update(@RequestBody Instruction instruction) {
        return new InstructionJsonEdit(service.save(instruction));
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public InstructionJsonEdit create(@RequestBody Instruction instruction) {
        return new InstructionJsonEdit(service.save(instruction));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }



    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<InstructionJsonEdit>> getBy(@RequestParam(value = "description",defaultValue = "%") String description,
                                                      Pageable pageable, PagedResourcesAssembler assembler) {

        Page<InstructionJsonEdit> instructions = service.findByDescriptionLike(description, pageable)
            .map( i -> new InstructionJsonEdit(i)  );
        return new ResponseEntity<>(assembler.toResource(instructions), HttpStatus.OK);
    }


}

