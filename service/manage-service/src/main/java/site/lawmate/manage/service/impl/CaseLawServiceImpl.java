package site.lawmate.manage.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import site.lawmate.manage.domain.dto.CaseLawDetailDto;
import site.lawmate.manage.domain.dto.CaseLawDto;
import site.lawmate.manage.domain.dto.SearchCriteriaDto;
import site.lawmate.manage.repository.CaseLawRepository;
import site.lawmate.manage.service.CaseLawService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaseLawServiceImpl implements CaseLawService {
    private final CaseLawRepository caselawRepository;


    @Override
    public Page<CaseLawDto> getCaseLawList(PageRequest pageRequest) {
        return caselawRepository.getCaseLawList(pageRequest);
    }

    @Override
    public CaseLawDetailDto getCaseLawDetail(String serialNumber) {
        return caselawRepository.getCaseLawDetail(serialNumber);
    }

    @Override
    public List<CaseLawDto> getCaseLawListByKeyword(SearchCriteriaDto criteria) {
        return caselawRepository.getCaseLawListByKeyword(criteria);
    }


}
