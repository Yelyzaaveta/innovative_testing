package chnu.edu.APILAB.service;


/*
 @author  Orynchuk Yelyzaveta
  @project   APILAB
  @class  ItemService
  @version  1.0.0 
  @since 27/09/2026 - 19.05
*/

import chnu.edu.APILAB.model.Item;
import chnu.edu.APILAB.model.dto.ItemDto;
import chnu.edu.APILAB.model.dto.response.ApiResponse;
import chnu.edu.APILAB.model.dto.response.BaseMetaData;
import chnu.edu.APILAB.model.dto.response.PaginationMetaData;
import chnu.edu.APILAB.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @PostConstruct
    public void init() {
        // seed only into an empty file, so the data saved before the restart is not lost
        if (itemRepository.count() > 0) {
            return;
        }
     List<Item> items = List.of(
             new Item("Milk", "00001", 1),
             new Item("Bread", "00002", 2),
             new Item("Sugar", "00003", 3),
             new Item("Salt", "00004", 1),
             new Item("Meat", "00005", 1),
             new Item("Chips", "00006", 3),
             new Item("Coca-Cola", "00007", 1),
             new Item("Hot dog", "00008", 1)
     );
     itemRepository.saveAll(items);
    }

    private static final int MAX_PAGE_SIZE = 100;

    public ApiResponse<BaseMetaData, ItemDto> getItemByCode(String code) {
        return itemRepository.findByCode(code)
                .map(item -> ok(ItemDto.from(item)))
                .orElseGet(() -> itemNotFound(code));
    }

    public ApiResponse<PaginationMetaData, ItemDto> getPage(int page, int size) {
        if (page < 0) {
            return pageError(400, "Page number cannot be negative, got " + page);
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            return pageError(400, "Page size must be between 1 and " + MAX_PAGE_SIZE + ", got " + size);
        }

        List<Item> all = itemRepository.findAll();

        int totalElements = all.size();
        int totalPages = (totalElements + size - 1) / size;

        // an empty collection is not an error: page 0 is simply an empty list
        if (page > 0 && page >= totalPages) {
            return pageError(404, "Page " + page + " does not exist, total pages: " + totalPages);
        }

        int from = Math.min(page * size, totalElements);
        int to = Math.min(from + size, totalElements);

        PaginationMetaData metaData = new PaginationMetaData();
        metaData.setNumber(page);
        metaData.setSize(size);
        metaData.setTotalElements(totalElements);
        metaData.setTotalPages(totalPages);
        metaData.setFirst(page == 0);
        metaData.setLast(page >= totalPages - 1);

        List<ItemDto> list = all.subList(from, to).stream().map(ItemDto::from).toList();
        return new ApiResponse<PaginationMetaData, ItemDto>(metaData, list);
    }

    public ApiResponse<BaseMetaData, ItemDto> createItem(ItemDto dto) {
        String problem = validate(dto);
        if (problem != null) {
            return error(400, problem);
        }
        if (itemRepository.findByCode(dto.getCode()).isPresent()) {
            return codeTaken(dto.getCode());
        }

        Item saved = itemRepository.save(dto.toItem());
        return new ApiResponse<BaseMetaData, ItemDto>(new BaseMetaData(201, true), ItemDto.from(saved));
    }

    public ApiResponse<BaseMetaData, ItemDto> updateItem(String code, ItemDto dto) {
        Item existing = itemRepository.findByCode(code).orElse(null);
        if (existing == null) {
            return itemNotFound(code);
        }

        String problem = validate(dto);
        if (problem != null) {
            return error(400, problem);
        }
        // the code can be changed, but not to a code of another item
        if (!dto.getCode().equals(code) && itemRepository.findByCode(dto.getCode()).isPresent()) {
            return codeTaken(dto.getCode());
        }

        existing.setName(dto.getName());
        existing.setCode(dto.getCode());
        existing.setQuantity(dto.getQuantity());
        return ok(ItemDto.from(itemRepository.save(existing)));
    }

    public ApiResponse<BaseMetaData, ItemDto> deleteItemByCode(String code) {
        Item existing = itemRepository.findByCode(code).orElse(null);
        if (existing == null) {
            return itemNotFound(code);
        }

        itemRepository.deleteById(existing.getId());
        return new ApiResponse<BaseMetaData, ItemDto>(new BaseMetaData(200, true));
    }

    //----------------  helpers ---------------------------

    // returns the text of the first problem found, or null if the item is fine
    private String validate(ItemDto dto) {
        if (dto == null) {
            return "Request body is missing";
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            return "Item name is required";
        }
        if (dto.getCode() == null || !dto.getCode().matches("\\d{5}")) {
            return "Item code must be exactly 5 digits, for example 00001";
        }
        if (dto.getQuantity() < 1) {
            return "Item quantity must be at least 1, got " + dto.getQuantity();
        }
        return null;
    }

    private ApiResponse<BaseMetaData, ItemDto> ok(ItemDto dto) {
        return new ApiResponse<BaseMetaData, ItemDto>(new BaseMetaData(200, true), dto);
    }

    private ApiResponse<BaseMetaData, ItemDto> error(int code, String message) {
        return new ApiResponse<BaseMetaData, ItemDto>(new BaseMetaData(code, false, message));
    }

    private ApiResponse<BaseMetaData, ItemDto> itemNotFound(String code) {
        return error(404, "Item with code '" + code + "' was not found");
    }

    private ApiResponse<BaseMetaData, ItemDto> codeTaken(String code) {
        return error(409, "Item with code '" + code + "' already exists");
    }

    private ApiResponse<PaginationMetaData, ItemDto> pageError(int code, String message) {
        PaginationMetaData metaData = new PaginationMetaData();
        metaData.setCode(code);
        metaData.setSuccess(false);
        metaData.setErrorMessage(message);
        return new ApiResponse<PaginationMetaData, ItemDto>(metaData);
    }
}
