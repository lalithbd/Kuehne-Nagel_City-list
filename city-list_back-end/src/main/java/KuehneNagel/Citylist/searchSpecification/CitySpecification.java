package KuehneNagel.Citylist.searchSpecification;

import KuehneNagel.Citylist.model.City;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static KuehneNagel.Citylist.util.Constant.PERCENTAGE_SIGN;
import static KuehneNagel.Citylist.util.Constant.SEARCH_COLUMN_NAME;

public class CitySpecification implements Specification<City> {

    private final String searchValue;

    public CitySpecification(String searchValue) {
        this.searchValue = searchValue;
    }


    //Criteria query for searching
    @Override
    public Predicate toPredicate(Root<City> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.like(root.get(SEARCH_COLUMN_NAME), PERCENTAGE_SIGN + searchValue + PERCENTAGE_SIGN);
    }

}
