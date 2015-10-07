package no.nsd.qddt.domain;

/**
 * Mirror of the {@link BaseService} interface to provide default
 * test method all services must support.
 */
public interface ServiceTest {

    void testCount() throws Exception;
    void testExists() throws Exception;
    void testFindOne() throws Exception;
    void testSave() throws Exception;
    void testSaveAll() throws Exception;
    void testDelete() throws Exception;
    void testDeleteAll() throws Exception;

}


