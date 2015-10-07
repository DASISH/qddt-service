package no.nsd.qddt.domain.instruction.web;

import no.nsd.qddt.domain.instruction.InstructionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/instruction")
public class InstructionController {

    private
    InstructionService instructionService;

    @Autowired
    public InstructionController(InstructionService instructionService) {
        this.instructionService = instructionService;
    }
}

