package lk.cmh.web.cmh.search_history;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    public List<SearchHistory> getAllSearchHistory(String email) {
        return searchHistoryRepository.findByUser_Email(email);
    }

}
