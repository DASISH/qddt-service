package no.nsd.qddt.domain.responsedomain.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.category.HierarchyLevel;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.responsedomain.Code;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import no.nsd.qddt.domain.responsedomain.ResponseKind;
import no.nsd.qddt.utils.builders.CategoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Stig Norland
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class ResponseDomainControllerTest extends ControllerWebIntegrationTest {

    @Autowired
    private ResponseDomainService entityService;

    @Autowired
    private CategoryService categoryService;

    private ResponseDomain rd;

    private String rootId;

    private String missingId;

    private String groupId;


    private Category saved;

    @Override
    public void setup() {
        super.setup();

        super.getBeforeSecurityContext().createSecurityContext();

        Category rootCategory = new CategoryBuilder()
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MIXED)
                .setLabel("Scale 1-5 with labels & missing").createCategory();
        Category group = new CategoryBuilder()
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.SCALE)
                .setLabel("Scale 1-5 with labels").createCategory();
                group.setInputLimit(1,5,1);
        group.getChildren().add(new CategoryBuilder()
                .setLabel("Very Happy").createCategory());
        group.getChildren().add(new CategoryBuilder()
                .setLabel("Happy").createCategory());
        group.getChildren().add(new CategoryBuilder()
                .setLabel("Between").createCategory());
        group.getChildren().add(new CategoryBuilder()
                .setLabel("UnHappy").createCategory());
        group.getChildren().add(new CategoryBuilder()
                .setLabel("Very Unhappy").createCategory());

        group = categoryService.save(group);
        groupId = group.getId().toString();

        rootCategory.getChildren().add(group);

        group = new CategoryBuilder().setLabel("NO-ANSWER")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MISSING_GROUP)
                .createCategory();
        group.getChildren().add(new CategoryBuilder()
                .setLabel("N/A").createCategory());
        group.getChildren().add(new CategoryBuilder()
                .setLabel("Don't know").createCategory());
        group.getChildren().add(new CategoryBuilder()
                .setLabel("Don't want to").createCategory());
        group= categoryService.save(group);
        missingId = group.getId().toString();

        rootCategory.getChildren().add(group);
        saved = categoryService.save(rootCategory);

        rootId = saved.getId().toString();
        super.getBeforeSecurityContext().destroySecurityContext();

    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get("/responsedomain/"+rd.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdate() throws Exception {

        String retval = mvc.perform(get("/category/"+ rootId).header("Authorization", "Bearer " + accessToken)).andReturn().getResponse().getContentAsString();

        rd = new ResponseDomain();
        rd.setName("YEAH");
        PopulateCatCodes(rd,saved);
        rd.setManagedRepresentation(saved);
        rd.setResponseKind(ResponseKind.MIXED);

        retval = mvc.perform(post("/responsedomain").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(rd)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }

    private int i=0;
    private void PopulateCatCodes(ResponseDomain rd, Category current){
        if (current.getHierarchyLevel() == HierarchyLevel.ENTITY){
            current.setCode(new Code(Integer.toString(i++)));
        }

        for (Category cat : current.getChildren()) {
            PopulateCatCodes(rd,(Category) cat);
        }

    }



    @Test
    public void testCreate() throws Exception {
        ResponseDomain aEntity = new ResponseDomain();
        aEntity.setName("Posted entity");

        mvc.perform(post("/responsedomain/create").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(aEntity)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(aEntity.getName())))
                .andExpect(jsonPath("$.changeKind", is( AbstractEntityAudit.ChangeKind.CREATED.toString())))
                .andExpect(status().isCreated());
    }

    @Test
    public void testMixedCreate() throws Exception {
        ResponseDomain aEntity = new ResponseDomain();
        aEntity.setName("base");
        aEntity.setResponseKind(ResponseKind.SCALE);
        aEntity.setManagedRepresentation(categoryService.findOne(UUID.fromString(groupId)));
        aEntity =entityService.save(aEntity);

        mvc.perform(get("/responsedomain/createmixed?responseDomainId="+aEntity.getId()+"&missingId="+missingId).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isCreated())
                .andDo(print());

    }



    @Test
    public void testDelete() throws Exception {
        mvc.perform(post("/responsedomain/delete/"+rootId).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        assertFalse("Instruction should no longer exist", entityService.exists(UUID.fromString(rootId)));
    }

    @Test
    public void testFind() throws Exception {

        ResponseDomain rd = new ResponseDomain();
        rd.setResponseKind(ResponseKind.LIST);
        rd.setManagedRepresentation(categoryService.save(new CategoryBuilder()
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.SCALE)
                .setLabel("Scale 1-5 with labels").createCategory()));
        rd.setName("test1");
        entityService.save(rd);
        ResponseDomain rd2 = new ResponseDomain();
        rd2.setManagedRepresentation(categoryService.save(new CategoryBuilder().setLabel("list-5")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.LIST)
                .createCategory()));
        rd2.setName("test2");
        rd2.setResponseKind(ResponseKind.SCALE);
        entityService.save(rd2);

        ResponseDomain rd3 = new ResponseDomain();
        rd3.setManagedRepresentation(categoryService.save(new CategoryBuilder()
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.MIXED)
                .setLabel("mixed5").createCategory()));
        rd3.setName("test3");
        rd3.setResponseKind(ResponseKind.MIXED);
        entityService.save(rd3);

        ResponseDomain rd4 = new ResponseDomain();
        rd4.setManagedRepresentation(categoryService.save(new CategoryBuilder()
                .setHierarchy(HierarchyLevel.ENTITY)
                .setType(CategoryType.NUMERIC)
                .setLabel("numeric labels").createCategory()));
        rd4.setName("test4");
        rd4.setResponseKind(ResponseKind.NUMERIC);
        entityService.save(rd4);


        mvc.perform(get("/responsedomain/page/search?ResponseKind=LIST").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());

        mvc.perform(get("/responsedomain/page/search?ResponseKind=SCALE").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());

        mvc.perform(get("/responsedomain/page/search?ResponseKind=MIXED").header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
