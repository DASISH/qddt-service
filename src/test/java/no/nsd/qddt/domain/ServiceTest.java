package no.nsd.qddt.domain;

import no.nsd.qddt.domain.classes.interfaces.BaseService;

/**
 * Mirror of the {@link BaseService} interface to provide default
 * test method all services must support.
 */
public interface ServiceTest {

    void testCount() throws Exception;
    void testExists() throws Exception;
    void testFindOne() throws Exception;
    void testSave() throws Exception;
    void testDelete() throws Exception;

}


