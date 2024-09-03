package com.thrift.hft.entity;

import com.thrift.hft.audit.Auditable;
import com.thrift.hft.dto.CartDTO;
import com.thrift.hft.dto.ProductDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.annotations.CascadeType.ALL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
public class Cart extends Auditable<String> {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(unique = true)
    String id;
    Long userId;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "cart_product",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    List<Product> productList = new ArrayList<>();
    @Builder.Default
    BigDecimal cartAmount =BigDecimal.ZERO;
    @Builder.Default
    Boolean isOrdered = Boolean.FALSE;


    public CartDTO getCartDTO(){
        List<ProductDTO> productDTOS =new ArrayList<>();
        if (!productList.isEmpty()){
            productDTOS=   productList.stream().map(p-> {
                try {
                    return p.getProductDTO();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        }
        return  new CartDTO(id,userId,cartAmount,isOrdered,productDTOS);
    }

}
