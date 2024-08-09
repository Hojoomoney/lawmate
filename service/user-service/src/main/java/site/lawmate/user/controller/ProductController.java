package site.lawmate.user.controller;


import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.ProductDto;
import site.lawmate.user.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/product")
@Slf4j
@Controller
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
})
public class ProductController {
    private final ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<Messenger> saveProduct(@RequestBody ProductDto dto) {
        log.info("product save 파라미터: {}", dto);
        return ResponseEntity.ok(productService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProductDto>> findById(@PathVariable("id") Long id) {
        log.info("product 정보 조회 진입 id: {} ", id);
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("product 전체 조회 진입 page: {} size: {}", page, size);
        return ResponseEntity.ok(productService.findAll(PageRequest.of(page, size)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Messenger> update(@RequestBody ProductDto dto) {
        log.info("update product 진입 성공: {} ", dto.toString());
        return ResponseEntity.ok(productService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Messenger> delete(@PathVariable("id") Long id) {
        log.info("delete product id: {} ", id);
        return ResponseEntity.ok(productService.delete(id));
    }

}
