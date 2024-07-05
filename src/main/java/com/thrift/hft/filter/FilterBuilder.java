package com.thrift.hft.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class FilterBuilder<T> {

    private final List<Specification<T>> specifications = new ArrayList<>();

    public Specification<T> build() {
        if (specifications.isEmpty()) {
            return null;
        }
        return specifications.stream().skip(1).reduce(specifications.get(0), Specification::and);
    }

    public FilterBuilder<T> in(String path, Collection collection) {
        final Specification<T> lessThenDate = (root, query, cb) -> getPath(root, path).in(collection);
        return addForNotNullParam(collection, lessThenDate);
    }

    public <C extends Comparable<? super C>> FilterBuilder<T> equals(String path, C value) {
        Specification<T> equals = (root, query, cb) -> cb.equal(getPath(root, path), value);
        return addForNotNullParam(value, equals);
    }

    public FilterBuilder<T> equalsTimeAsDate(String path, LocalDate date) {
        final Specification<T> equalsTimeAsDateSpec = Specifications.equalsTimeAsDate(path, date);
        return addForNotNullParam(date, equalsTimeAsDateSpec);
    }
    public FilterBuilder<T> equalsTimeAsDate(String path, Date date) {
        final Specification<T> equalsTimeAsDateSpec = Specifications.equalsTimeAsDate(path, date);
        return addForNotNullParam(date, equalsTimeAsDateSpec);
    }

    public FilterBuilder<T> equalsTimeAsDateTypeColumn(String path, LocalDate date) {
        final Specification<T> equalsTimeAsDateSpec = Specifications.equalsTimeAsDateTypeColumn(path, date);
        return addForNotNullParam(date, equalsTimeAsDateSpec);
    }

    public <C extends Comparable<? super C>> FilterBuilder<T> greaterThanOrEqualTo(String path, C value) {
        final Specification<T> greaterThanOrEqualTo = (Specification<T>) (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(path), value);
        return addForNotNullParam(value, greaterThanOrEqualTo);
    }

    public FilterBuilder<T> likeWithFieldConcat(String value, String delimiter, String... fields) {

        Specification<T> inSpec = (root, query, cb) -> cb
                .like(concatFields(root, cb, delimiter, fields), "%" + value + "%");
        return addForNotNullParam(value, inSpec);
    }

    public FilterBuilder<T> like(String path, String value) {
        if (!StringUtils.isEmpty(value)) {
            final Specification<T> like = (root, query, cb) -> cb.like(root.get(path), "%" + value + "%");
            specifications.add(like);
        }
        return this;
    }

    public FilterBuilder<T> likeForEmbeddedObjects(String path, String value) {
        if (!StringUtils.isEmpty(value)) {
            final Specification<T> like = (root, query, cb) -> cb.like(getPath(root, path).as(String.class), "%" + value + "%");
            specifications.add(like);
        }
        return this;
    }

    public <C extends Comparable<? super C>> FilterBuilder<T> greaterThan(String path, C value) {
        final Specification<T> lessThenDate = (root, query, cb) -> cb.greaterThan((Path<C>) getPath(root, path), value);
        return addForNotNullParam(value, lessThenDate);
    }

    public FilterBuilder<T> greaterThanOrEqualTo(String path, LocalDate value) {
        final Specification<T> greaterThanOrEqualTo = Specifications.greaterOrEqualsTimeAsDate(path, value);
        return addForNotNullParam(value, greaterThanOrEqualTo);
    }

    public FilterBuilder<T> greaterThanOrEqualToDateTypeColumn(String path, LocalDate value) {
        final Specification<T> greaterThanOrEqualTo = Specifications.greaterOrEqualsTimeAsDateTypeColumn(path, value);
        return addForNotNullParam(value, greaterThanOrEqualTo);
    }

    public FilterBuilder<T> greaterThanOrEqualToDateTypeColumn(String path, Date value) {
        final Specification<T> greaterThanOrEqualTo = Specifications.greaterOrEqualsTimeAsDateTypeColumn(path, value);
        return addForNotNullParam(value, greaterThanOrEqualTo);
    }

    public <C extends Comparable<? super C>> FilterBuilder<T> lessThanOrEqualTo(String path, C value) {
        final Specification<T> lessThenDate = (root, query, cb) -> cb.lessThanOrEqualTo(
                (Path<C>) getPath(root, path), value);
        return addForNotNullParam(value, lessThenDate);
    }

    public FilterBuilder<T> lessThanOrEqualTo(String path, LocalDate value) {
        final Specification<T> lessThenDate = Specifications.lessOrEqualsTimeAsDate(path, value);
        return addForNotNullParam(value, lessThenDate);
    }
    public FilterBuilder<T> lessThanOrEqualToDateTypeColumn(String path, LocalDate value) {
        final Specification<T> lessThenDate = Specifications.lessOrEqualsTimeAsDateTypeColumn(path, value);
        return addForNotNullParam(value, lessThenDate);
    }

    public FilterBuilder<T> lessThanOrEqualToDateTypeColumn(String path, Date value) {
        final Specification<T> lessThenDate = Specifications.lessOrEqualsTimeAsDateTypeColumn(path, value);
        return addForNotNullParam(value, lessThenDate);
    }

    public <C extends Comparable<? super C>> FilterBuilder<T> hasParam1AndParam2(String path1,String path2,String param1, String param2){
        final Specification<T> hasParam1AndParam2Spec = Specifications.hasParam1AndParam2(path1, path2, param1, param2);
        return addForNotNullParam(param1,param2,hasParam1AndParam2Spec);
    }

    private Expression<String> concatFields(Root<T> root, CriteriaBuilder cb, String delimiter,
                                            String... fields) {
        Expression<String> concat = null;
        for (String field : fields) {
            if (concat == null) {
                concat = getPath(root, field).as(String.class);
            } else {
                concat = cb.concat(cb.concat(concat, delimiter), field);
            }
        }
        return concat;
    }

    private FilterBuilder<T> addForNotNullParam(Object param, Specification<T> specification) {
        if (param != null) {
            specifications.add(specification);
        }
        return this;
    }

    private FilterBuilder<T> addForNotNullParam(Object param1,Object param2, Specification<T> specification) {
        if(param1 !=null && param2 !=null) {
            specifications.add(specification);
        }
        return this;
    }

    private Path<?> getPath(Path<?> root, String path) {
        final String[] paths = path.split("\\.");
        for (String attribute : paths) {
            root = root.get(attribute);
        }
        return root;
    }

    public FilterBuilder<T> dateLessThanOrEqualTo(String path, Date value) {
        final Specification<T> dateLessThanOrEqualTo = Specifications.dateLessThanOrEqualTo(path, value);
        return addForNotNullParam(value, dateLessThanOrEqualTo);
    }
    public FilterBuilder<T> dateGreaterThanOrEqualTo(String path, Date value) {
        final Specification<T> dateGreaterThanOrEqualTo = Specifications.dateGreaterThanOrEqualTo(path, value);
        return addForNotNullParam(value, dateGreaterThanOrEqualTo);
    }
}
