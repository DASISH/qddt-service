package domain.agency;

import domain.AbstractServiceTest;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.agency.AgencyService;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AgencyServiceTest extends AbstractServiceTest {

    @Autowired
    private AgencyService agencyService;

    @Before
    public void setup() {
        super.setService(agencyService);

        List<Agency> agencyList= new ArrayList<>();
        Agency agency = new Agency();
        agency.setName("Test Agency One");
        agencyList.add(agency);

        agency = new Agency();
        agency.setName("Test Agency Two");
        agencyList.add(agency);

        agency = new Agency();
        agency.setName("Test Agency Three");
        agencyList.add(agency);
        agencyService.save(agencyList);
    }
}