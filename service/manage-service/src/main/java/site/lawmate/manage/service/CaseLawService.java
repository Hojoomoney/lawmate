package site.lawmate.manage.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import site.lawmate.manage.domain.dto.CaseLawDetailDto;
import site.lawmate.manage.domain.dto.CaseLawDto;
import site.lawmate.manage.domain.dto.SearchCriteriaDto;

import java.util.List;

public interface CaseLawService {
    Page<CaseLawDto> getCaseLawList(PageRequest pageRequest);

    CaseLawDetailDto getCaseLawDetail(String serialNumber);
    List<CaseLawDto> getCaseLawListByKeyword(SearchCriteriaDto criteria);


}
