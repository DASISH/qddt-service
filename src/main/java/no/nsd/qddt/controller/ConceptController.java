package no.nsd.qddt.controller;

import no.nsd.qddt.controller.audit.BaseAuditController;
import no.nsd.qddt.domain.Concept;
import no.nsd.qddt.service.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/concept")
public class ConceptController extends AbstractAuditController<Concept> { }
