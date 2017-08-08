package no.nsd.qddt.domain.author.web;

import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.author.AuthorService;
import no.nsd.qddt.domain.study.StudyService;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramService;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
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
 * @author Stig Norland
 */
@RestController
@RequestMapping(value = "/author")
public class AuthorController {

    private final AuthorService authorService;
    private final SurveyProgramService surveyService;
    private final StudyService studyService;
    private final TopicGroupService topicService;

    @Autowired
    public AuthorController(AuthorService authorService,
                            SurveyProgramService surveyService,
                            StudyService studyService,
                            TopicGroupService topicService) {
        this.authorService = authorService;
        this.surveyService = surveyService;
        this.studyService = studyService;
        this.topicService = topicService;
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Author get(@PathVariable("id") UUID id) {
        return authorService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Author update(@RequestBody Author author) {
        return authorService.save(author);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Author create(@RequestBody Author author) {
        return authorService.save(author);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        authorService.delete(id);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/", method = RequestMethod.GET ,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<Author>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Author> authors = authorService.findAllPageable(pageable);
        return new ResponseEntity<>(assembler.toResource(authors), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/combine", method = RequestMethod.GET, params = { "authorId" })
    public Author addAuthor(@RequestParam("authorId") UUID authorId
                        ,@RequestParam("surveyId") UUID surveyId
                        ,@RequestParam("studyId") UUID studyId
                        ,@RequestParam("topicId") UUID topicId ){
        Author author = authorService.findOne(authorId);

        if (surveyId != null)
            author.addSurvey(surveyService.findOne(surveyId));
        else if (studyId != null)
            author.addStudy(studyService.findOne(studyId));
        else if (topicId != null)
            author.addTopic(topicService.findOne(topicId));
//        else if (conceptId != null)
//            author.addConcept(conceptService.findOne(conceptId));

        return authorService.save(author);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/decombine", method = RequestMethod.GET, params = { "authorId"})
    public Author removeQuestion(@RequestParam("authorId") UUID authorId
            ,@RequestParam("surveyId") UUID surveyId
            ,@RequestParam("studyId") UUID studyId
            ,@RequestParam("topicId") UUID topicId
            ,@RequestParam("conceptId") UUID conceptId ){

        Author author = authorService.findOne(authorId);

        if (surveyId != null)
            author.removeSurvey(surveyService.findOne(surveyId));
        else if (studyId != null)
            author.removeStudy(studyService.findOne(studyId));
        else if (topicId != null)
            author.removeTopic(topicService.findOne(topicId));
//        else if (conceptId != null)
//            author.removeConcept(conceptService.findOne(conceptId));
//        else
//            throw new Exception("Missing parameter");
        return authorService.save(author);
    }

}