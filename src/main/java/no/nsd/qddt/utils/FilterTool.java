package no.nsd.qddt.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class FilterTool {

    public static PageRequest defaultSort(Pageable pageable, String... args){
        assert pageable != null;
        Sort sort;
        if (pageable.getSort() == null )
            sort = defaultSort(args);
        else {
            sort = filterSort(pageable.getSort(),"responseDomain.name");
        }
        return  new PageRequest(pageable.getPageNumber()
            ,pageable.getPageSize()
            ,sort);
    }

    private static Sort filterSort(Sort source, String... args){
        List<String> filterwords = Arrays.asList( args );
        List<Sort.Order> orders = new ArrayList<>( 0 );
        source.iterator().forEachRemaining( o -> {
            if (!filterwords.contains( o.getProperty() ))
                orders.add( o );
        });

        return new Sort( orders);
    }


    private static Sort defaultSort(String... args){
        return new Sort(
            Arrays.stream(args).map(s-> {
                String[] par = s.split(" ");
                if (par.length > 1)
                    return new Sort.Order(Sort.Direction.fromString(par[1]), par[0]);
                else
                    return new Sort.Order(Sort.Direction.ASC, par[0]);
            }).collect(Collectors.toList()));
    }


    public static PageRequest defaultOrModifiedSort(Pageable pageable, String... args) {
        assert pageable != null;
        Sort sort;
        if (pageable.getSort() == null )
            sort = defaultSort(args);
        else
            sort = modifiedSort(pageable.getSort());
        return new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),sort);

//        System.out.println(pageRequest.toString());
//        if (pageRequest==null) throw new Exception("pageRequest is null");
//        return pageRequest;
    }

    private static Sort modifiedSort(Sort sort){
        List<Sort.Order> orders = new LinkedList<>();
        sort.forEach(o->{
            if(o.getProperty().equals("modified")) {
                orders.add(new Sort.Order(o.getDirection(), "updated"));
            } else if (o.getProperty().equals("responseDomainName")) {
                orders.add(new Sort.Order(o.getDirection(), "responsedomain_name"));
            } else if (o.getProperty().equals("questionName")) {
                orders.add(new Sort.Order(o.getDirection(), "question_name"));
            } else if (o.getProperty().equals("questionText")) {
                orders.add(new Sort.Order(o.getDirection(), "question_text"));
            } else if (o.getProperty().equals("status.label")) {
                orders.add(new Sort.Order(o.getDirection(), "statuslabel")); 
            } else
                orders.add(o);
        });
        return new Sort(orders);
    }

}
