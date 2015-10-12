package no.nsd.qddt.domain.instruction.web;

import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instruction.InstructionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@RequestMapping("/instruction")
public class InstructionController {

    private InstructionService instructionService;

    @Autowired
    public InstructionController(InstructionService instructionService) {
        this.instructionService = instructionService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Instruction get(@PathVariable("id") UUID id) {
        return instructionService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public Instruction update(@RequestBody Instruction instruction) {
        return instructionService.save(instruction);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Instruction create(@RequestBody Instruction instruction) {
        return instructionService.save(instruction);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        instructionService.delete(id);
    }
}

