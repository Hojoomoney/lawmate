package site.lawmate.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.ProductDto;
import site.lawmate.user.domain.model.Product;
import site.lawmate.user.repository.ProductRepository;
import site.lawmate.user.service.ProductService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public Messenger save(ProductDto productDto) {
        entityToDto((productRepository.save(dtoToEntity(productDto))));
        return Messenger.builder()
                .message(productRepository.existsById(productDto.getId()) ? "SUCCESS" : "FAILURE")
                .build();
    }

    @Transactional
    @Override
    public Messenger delete(Long id) {
        productRepository.deleteById(id);
        return Messenger.builder()
                .message(productRepository.findById(id).isPresent() ? "FAILURE" : "SUCCESS")
                .build();
    }

    @Transactional
    @Override
    public Messenger update(ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(productDto.getId());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            Product modifyProduct = product.toBuilder()
                    .itemName(productDto.getItemName())
                    .price(productDto.getPrice())
                    .build();
            productRepository.save(modifyProduct);
            return Messenger.builder().message("SUCCESS").build();
        } else {
            return Messenger.builder().message("FAILURE").build();
        }
    }

    @Override
    public List<ProductDto> findAll(PageRequest pageRequest) {
        return productRepository.findAllByOrderByIdAsc().stream().map(this::entityToDto).toList();
    }

    @Override
    public Optional<ProductDto> findById(Long id) {
        return productRepository.findById(id).map(this::entityToDto);
    }

    @Override
    public Messenger count() {
        return Messenger.builder()
                .message(productRepository.count() + "").build();
    }

    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}
