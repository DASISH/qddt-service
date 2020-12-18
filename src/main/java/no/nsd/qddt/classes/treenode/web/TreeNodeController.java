package no.nsd.qddt.classes.treenode.web;

import no.nsd.qddt.classes.treenode.TreeNode;
import no.nsd.qddt.classes.treenode.TreeNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@RequestMapping("/treenode")
public class TreeNodeController {

    private final TreeNodeService service;

    @Autowired
    public TreeNodeController(TreeNodeService service) {
        this.service = service;
    }

    @GetMapping(value = "{id}")
    public TreeNode get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }

    @PutMapping(value = "")
    public TreeNode update(@RequestBody TreeNode treeNode) {
        return service.save(treeNode);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "")
    public TreeNode create(@RequestBody TreeNode treeNode) {
        return service.save(treeNode);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }


}

