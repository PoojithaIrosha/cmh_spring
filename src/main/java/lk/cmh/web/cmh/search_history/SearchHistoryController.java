package lk.cmh.web.cmh.search_history;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/search-history")
@RequiredArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    @GetMapping
    public List<SearchHistory> getSearchHistory(@NonNull @Param("email") String email) {
        return searchHistoryService.getAllSearchHistory(email);
    }

}
