package no.nsd.qddt.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class FilterTool {

    public static PageRequest defaultSort(Pageable pageable, String... args){
        Sort sort;
        if (pageable.getSort() == null )
            sort = defaultSort(args);
        else
            sort = pageable.getSort();

        return  new PageRequest(pageable.getPageNumber()
                ,pageable.getPageSize()
                ,sort);
    }


    public static Sort defaultSort(String... args){
        return new Sort(
                Arrays.stream(args).map(s-> {
                    String[] par = s.split(" ");
                    if (par.length > 1)
                        return new Sort.Order(Sort.Direction.fromString(par[1]), par[0]);
                    else
                        return new Sort.Order(Sort.Direction.ASC, par[0]);
                }).collect(Collectors.toList()));
    }


}
