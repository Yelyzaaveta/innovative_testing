package chnu.edu.APILAB.controller;


/*
 @author  Orynchuk Yelyzaveta
  @project   APILAB
  @class  ItemController
  @version  1.0.0 
  @since 27/09/2026 - 19.09
*/

import chnu.edu.APILAB.model.dto.ItemDto;
import chnu.edu.APILAB.model.dto.response.ApiResponse;
import chnu.edu.APILAB.model.dto.response.BaseMetaData;
import chnu.edu.APILAB.model.dto.response.PaginationMetaData;
import chnu.edu.APILAB.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{code}")
    @Operation(summary = "Get one item by its code", description = "200 - found, 404 - no item with this code")
    public ResponseEntity<ApiResponse<BaseMetaData, ItemDto>> getItemByCode(@PathVariable String code) {
        return respond(itemService.getItemByCode(code));
    }

    @GetMapping
    @Operation(
            summary = "Get ALL items (in the order they were added), PAGEABLE. Default 5 items per page",
            description = "200 - page returned (empty list if there are no items), "
                    + "400 - negative page or wrong size, 404 - page number is bigger than the last page"
    )
    public ResponseEntity<ApiResponse<PaginationMetaData, ItemDto>> getItemsPage(
            @Parameter(description = "Request parameter. Page of paginated result, starts from zero")
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @Parameter(description = "Request parameter. Number of items on a page, from 1 to 100")
            @RequestParam(name = "size", defaultValue = "5", required = false) int size
    ) {
        return respond(itemService.getPage(page, size));
    }

    @PostMapping
    @Operation(summary = "Create an item", description = "201 - created, 400 - name, code or quantity is wrong, 409 - an item with this code already exists")
    public ResponseEntity<ApiResponse<BaseMetaData, ItemDto>> createItem(@RequestBody ItemDto item) {
        return respond(itemService.createItem(item));
    }

    @PutMapping("/{code}")
    @Operation(
            summary = "Update an item",
            description = "200 - updated, 400 - name, code or quantity is wrong, 404 - no item with this code, "
                    + "409 - the new code belongs to another item"
    )
    public ResponseEntity<ApiResponse<BaseMetaData, ItemDto>> updateItem(
            @PathVariable String code, @RequestBody ItemDto item) {
        return respond(itemService.updateItem(code, item));
    }

    @DeleteMapping("/{code}")
    @Operation(summary = "Delete an item", description = "200 - deleted (data is empty), 404 - no item with this code")
    public ResponseEntity<ApiResponse<BaseMetaData, ItemDto>> deleteItem(@PathVariable String code) {
        return respond(itemService.deleteItemByCode(code));
    }

    private <M extends BaseMetaData, D> ResponseEntity<ApiResponse<M, D>> respond(ApiResponse<M, D> response) {
        return ResponseEntity.status(response.getMeta().getCode()).body(response);
    }
}
