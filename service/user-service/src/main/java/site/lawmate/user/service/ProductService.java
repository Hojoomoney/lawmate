package site.lawmate.user.service;

import site.lawmate.user.domain.dto.ProductDto;
import site.lawmate.user.domain.model.Product;

public interface ProductService extends CommandService<ProductDto>, QueryService<ProductDto> {

    default Product dtoToEntity(ProductDto dto) {
        return Product.builder()
                .itemName(dto.getItemName())
                .price(dto.getPrice())
                .build();
    }

    default ProductDto entityToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .itemName(product.getItemName())
                .price(product.getPrice())
                .build();
    }
}
