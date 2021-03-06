package com.zxy.web.framework.locus.service;

import com.zxy.web.framework.locus.model.Artery;
import com.zxy.web.framework.locus.repository.jpa.ArteryDao;
import com.zxy.web.module.core.orm.util.DynamicSpecifications;
import com.zxy.web.module.core.orm.util.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * ArteryService
 *
 * @author James
 */
@Component
@Transactional(readOnly = true)
public class ArteryService {

    private ArteryDao arteryDao;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void save(Artery artery) {
        arteryDao.save(artery);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void delete(String id) {
        arteryDao.delete(id);
    }

    public Page<Artery> getArteryByPage(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                        String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Artery> spec = buildSpecification(searchParams);
        return arteryDao.findAll(spec, pageRequest);
    }

    private Specification<Artery> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Artery> spec = DynamicSpecifications.bySearchFilter(filters.values(), Artery.class);
        return spec;

    }

    private PageRequest buildPageRequest(int pageNumber, int pageSize, String sortType) {
        Sort sort = null;
        if ("auot".equals(sortType)) {
            sort = new Sort(Sort.Direction.DESC, "createDate");
        } else if ("patientName".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, "patientName");
        }

        return new PageRequest(pageNumber - 1, pageSize, sort);
    }

    public List<Artery> findAll() {
        List<Artery> list = (List<Artery>) arteryDao.findAll();

        return list;
    }

    public Artery getArtery(String id) {
        return arteryDao.findOne(id);
    }

    public ArteryDao getArteryDao() {
        return arteryDao;
    }

    @Autowired
    public void setArteryDao(ArteryDao arteryDao) {
        this.arteryDao = arteryDao;
    }
}
