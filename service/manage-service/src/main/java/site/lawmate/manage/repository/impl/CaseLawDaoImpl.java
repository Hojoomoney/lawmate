package site.lawmate.manage.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import site.lawmate.manage.domain.dto.CaseLawDetailDto;
import site.lawmate.manage.domain.dto.CaseLawDto;
import site.lawmate.manage.domain.dto.SearchCriteriaDto;
import site.lawmate.manage.domain.model.QCaseLaw;
import site.lawmate.manage.domain.model.QCaseLawDetail;
import site.lawmate.manage.repository.CaseLawDao;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CaseLawDaoImpl implements CaseLawDao {

    private final JPAQueryFactory factory;
    private final QCaseLaw caseLaw = QCaseLaw.caseLaw;
    private final QCaseLawDetail caseLawDetail = QCaseLawDetail.caseLawDetail;
    @Override
    public Page<CaseLawDto> getCaseLawList(PageRequest pageRequest) {
        List<CaseLawDto> caseLawList = factory.select(
                        Projections.fields(CaseLawDto.class,
                                caseLaw.serialNumber,
                                caseLaw.caseName,
                                caseLaw.caseNumber,
                                caseLaw.dateOfDecision
                        )
                )
                .from(caseLaw)
                .orderBy(caseLaw.serialNumber.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        // 전체 데이터 수 계산
        long total = factory.select(caseLaw.count())
                .from(caseLaw)
                .fetchOne();

        return new PageImpl<>(caseLawList, pageRequest, total);
    }

    @Override
    public CaseLawDetailDto getCaseLawDetail(String serialNumber) {
        return factory.select(
                Projections.fields(CaseLawDetailDto.class,
                        caseLawDetail.id,
                        caseLawDetail.caseLaw,
                        caseLawDetail.court,
                        caseLawDetail.caseType,
                        caseLawDetail.summary,
                        caseLawDetail.detail)
        )
                .from(caseLawDetail)
                .where(caseLawDetail.caseLaw.serialNumber.eq(serialNumber))
                .fetchOne();
    }

    @Override
    public List<CaseLawDto> getCaseLawListByKeyword(SearchCriteriaDto criteria) {
        BooleanBuilder builder = new BooleanBuilder();

        if (criteria.getSerialNumber() != null && !criteria.getSerialNumber().isEmpty()) {
            builder.and(caseLaw.serialNumber.containsIgnoreCase(criteria.getSerialNumber()));
        }
        if (criteria.getCaseNumber() != null && !criteria.getCaseNumber().isEmpty()) {
            builder.and(caseLaw.caseNumber.containsIgnoreCase(criteria.getCaseNumber()));
        }
        if (criteria.getCaseName() != null && !criteria.getCaseName().isEmpty()) {
            builder.and(caseLaw.caseName.containsIgnoreCase(criteria.getCaseName()));
        }

        return factory.select(
                        Projections.fields(CaseLawDto.class,
                                caseLaw.serialNumber,
                                caseLaw.caseName,
                                caseLaw.caseNumber,
                                caseLaw.dateOfDecision
                        )
                )
                .from(caseLaw)
                .where(builder)
                .orderBy(caseLaw.serialNumber.desc())
                .fetch();
    }

}
