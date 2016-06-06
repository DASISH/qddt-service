package no.nsd.qddt.domain.code.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import no.nsd.qddt.domain.responsedomain.ResponseKind;
import no.nsd.qddt.utils.builders.CategoryBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Stig Norland
 */
public class ResponseDomainCategoryControllerTest extends ControllerWebIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ResponseDomainService responseDomainService;

    private ResponseDomain responseDomain;

    @Override
    public void setup() {
        super.setup();

        super.getBeforeSecurityContext().createSecurityContext();

        Category rootCategory = new CategoryBuilder().setName("GENDER")
                .setHierarchy(HierarchyLevel.GROUP_ENTITY)
                .setType(CategoryType.LIST)
                .setLabel("Gender").createCategory();
        rootCategory.addChild(new CategoryBuilder().setName("FEMALE")
                .setLabel("Female").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("MAN")
                .setLabel("Man").createCategory());
        rootCategory.addChild(new CategoryBuilder().setName("TRANSGENDER")
                .setLabel("Transgender").createCategory());
        rootCategory = categoryService.save(rootCategory);


        // Create a responsdomain with a categoryschema.
        responseDomain = new ResponseDomain();
        responseDomain.setName("Sex mandatory answer");
        responseDomain.setResponseKind(ResponseKind.LIST);
        responseDomain.setManagedRepresentation(rootCategory);
        responseDomain = responseDomainService.save(responseDomain);

        super.getBeforeSecurityContext().destroySecurityContext();

    }


    @Test
    public void testUpdate() throws Exception {
//        entity.setCodeValue(entity.getCodeValue() + " edited");
//
//        mvc.perform(post("/code").header("Authorization", "Bearer " + accessToken)
//                .contentType(rest.getContentType())
//                .content(rest.json(entity)))
//                .andExpect(content().contentType(rest.getContentType()))
//                .andExpect(jsonPath("$.codeValue", is(entity.getCodeValue())))
//                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT.toString())))
//                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        Code aEntity = new Code();
        aEntity.setCodeValue("Posted entity");

        mvc.perform(post("/responsedomaincode/create").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(aEntity)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.codeValue", is(aEntity.getCodeValue())))
                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.CREATED.toString())))
                .andExpect(status().isCreated());
    }


}