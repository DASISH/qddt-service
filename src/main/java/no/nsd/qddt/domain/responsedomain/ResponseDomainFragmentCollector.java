package no.nsd.qddt.domain.responsedomain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static no.nsd.qddt.domain.responsedomain.ResponseKind.*;

/**
 * @author Stig Norland
 */
public class ResponseDomainFragmentCollector {


    private static final Map<ResponseKind, Map<String,String>> myMap;
    static {
        Map<ResponseKind, Map<String,String>> aMap = new HashMap<>( 6 );
        aMap.put(DATETIME, new HashMap<>());
        aMap.put(TEXT,new HashMap<>());
        aMap.put(NUMERIC, new HashMap<>());
        aMap.put(LIST, new HashMap<>());
        aMap.put(SCALE,new HashMap<>());
        aMap.put(MIXED, new HashMap<>());
        myMap = Collections.unmodifiableMap(aMap);
    }

    public Map<String, String> get(ResponseKind responseKind) {
        return myMap.get( responseKind );
    }

}
