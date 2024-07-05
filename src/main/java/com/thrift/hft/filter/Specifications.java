package com.thrift.hft.filter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Specifications {

    public static <T> Specification<T> equalsTimeAsDate(String path, LocalDate date) {
        Date convertedDate = java.sql.Date.valueOf(date);
        return (root, query, cb) -> cb.equal(root.get(path), convertedDate);
    }

    public static <T> Specification<T> equalsTimeAsDate(String attribute, Date date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null; // Return null specification if date is null
            }
            Expression<Date> truncatedDate = cb.function("DATE", Date.class, root.get(attribute));
            return cb.equal(truncatedDate, cb.literal(date));
        };
    }

    public static <T> Specification<T> greaterOrEqualsTimeAsDate(String path, LocalDate date) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(cb.function(
                "CONVERT",
                String.class,
                new HibernateInlineExpression(cb, "creationDate"),
                new HibernateInlineExpression(cb, path)
        ), date.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    public static <T> Specification<T> lessOrEqualsTimeAsDate(String path, LocalDate date) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(cb.function(
                "CONVERT",
                String.class,
                new HibernateInlineExpression(cb, "creationDate"),
                new HibernateInlineExpression(cb, path)
        ), date.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    public static <T> Specification<T> equalsTimeAsDateTypeColumn(String path, LocalDate date) {
        return (root, query, cb) -> cb.equal(cb.function(
                "CONVERT",
                String.class,
                new HibernateInlineExpression(cb, "creationDate"),
                new HibernateInlineExpression(cb, path)
        ), date);
    }

    public static <T> Specification<T> greaterOrEqualsTimeAsDateTypeColumn(String path, LocalDate date) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(cb.function(
                "CONVERT",
                String.class,
                new HibernateInlineExpression(cb, "creationDate"),
                new HibernateInlineExpression(cb, path)
        ), date.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    public static <T> Specification<T> greaterOrEqualsTimeAsDateTypeColumn(String path, Date date) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(path), date);
    }

    public static <T> Specification<T> lessOrEqualsTimeAsDateTypeColumn(String path, LocalDate date) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(cb.function(
                "CONVERT",
                String.class,
                new HibernateInlineExpression(cb, "creationDate"),
                new HibernateInlineExpression(cb, path)
        ), date.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    public static <T> Specification<T> lessOrEqualsTimeAsDateTypeColumn(String path, Date date) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(path), date);
    }

    public static <T> Specification<T> andReduce(List<Specification<T>> specifications) {
        if (specifications.isEmpty()) {
            return null;
        }
        return specifications.stream().skip(1).reduce(specifications.get(0), Specification::and);
    }

    public static <T> Specification<T> hasParam1AndParam2(String path1, String path2, String param1, String param2) {
        return (root, query, criteriaBuilder) -> {
            // Create predicates for each parameter value
            Predicate param1Predicate = criteriaBuilder.equal(root.get(path1), param1);
            Predicate param2Predicate = criteriaBuilder.equal(root.get(path2), param2);
            // Combine the predicates with AND
            return criteriaBuilder.and(param1Predicate, param2Predicate);
        };
    }

    public static <T> Specification<T> dateGreaterThanOrEqualTo(String attribute, Date date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null; // Return null specification if date is null
            }
            Expression<Date> truncatedDate = cb.function("DATE", Date.class, root.get(attribute));
            return cb.greaterThanOrEqualTo(truncatedDate, cb.literal(date));
        };
    }

    public static <T> Specification<T> dateLessThanOrEqualTo(String attribute, Date date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null; // Return null specification if date is null
            }
            Expression<Date> truncatedDate = cb.function("DATE", Date.class, root.get(attribute));
            return cb.lessThanOrEqualTo(truncatedDate, cb.literal(date));
        };
    }
}