package com.lite.blackdream.business.service;

import com.lite.blackdream.business.domain.*;
import com.lite.blackdream.business.parameter.datamodel.*;
import com.lite.blackdream.business.repository.*;
import com.lite.blackdream.framework.exception.AppException;
import com.lite.blackdream.framework.component.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedList;

/**
 * @author LaineyC
 */
@Service
public class DataModelServiceImpl extends BaseService implements DataModelService{

    @Autowired
    private DataModelRepository dataModelRepository;

    @Autowired
    private DynamicModelRepository dynamicModelRepository;

    @Autowired
    private GeneratorInstanceRepository generatorInstanceRepository;

    @Autowired
    private GeneratorRepository generatorRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DataModel create(DataModelCreateRequest request) {
        DataModel dataModel = new DataModel();
        dataModel.setId(idWorker.nextId());
        dataModel.setName(request.getName());
        dataModel.setIsDelete(false);
        dataModel.setIsExpand(request.getIsExpand());

        Long userId = request.getAuthentication().getUserId();
        User user = new User();
        user.setId(userId);
        dataModel.setUser(user);

        Long dynamicModelId = request.getDynamicModelId();
        DynamicModel dynamicModelPersistence = dynamicModelRepository.selectById(dynamicModelId);
        if(dynamicModelPersistence == null){
            throw new AppException("动态模型不存在");
        }
        dataModel.setDynamicModel(dynamicModelPersistence);

        Long generatorInstanceId = request.getGeneratorInstanceId();
        GeneratorInstance generatorInstancePersistence = generatorInstanceRepository.selectById(generatorInstanceId);
        if(generatorInstancePersistence == null){
            throw new AppException("生成器实例不存在");
        }
        dataModel.setGeneratorInstance(generatorInstancePersistence);

        Long rootId = request.getRootId();
        DataModel rootPersistence = dataModelRepository.selectById(rootId);
        if(rootPersistence == null){
            throw new AppException("root不存在");
        }

        Long parentId = request.getParentId();
        DataModel parentPersistence = dataModelRepository.selectById(parentId, rootPersistence);
        if(parentPersistence == null){
            throw new AppException("parent不存在");
        }
        DataModel parent = new DataModel();
        parent.setId(parentPersistence.getId());
        dataModel.setParent(parent);

        dataModel.setProperties(request.getProperties());
        dataModel.setAssociation(request.getAssociation());

        dataModelRepository.insert(dataModel, rootPersistence);
        return dataModel;
    }

    @Override
    public DataModel delete(DataModelDeleteRequest request) {
        Long id = request.getId();

        Long rootId = request.getRootId();
        DataModel rootPersistence = dataModelRepository.selectById(rootId);
        if(rootPersistence == null){
            throw new AppException("root不存在");
        }

        Long userId = request.getAuthentication().getUserId();
        if(!userId.equals(rootPersistence.getUser().getId())){
            throw new AppException("权限不足");
        }

        GeneratorInstance generatorInstancePersistence = generatorInstanceRepository.selectById(rootPersistence.getGeneratorInstance().getId());
        if(generatorInstancePersistence == null){
            throw new AppException("生成器实例不存在");
        }

        DataModel dataModelPersistence = dataModelRepository.selectById(id, rootPersistence);
        if(dataModelPersistence == null) {
            throw new AppException("数据模型不存在");
        }
        if(!dataModelPersistence.getChildren().isEmpty()){
            throw new AppException("有子数据模型不能删除");
        }

        dataModelRepository.delete(dataModelPersistence, rootPersistence);
        return dataModelPersistence;
    }

    @Override
    public DataModel get(DataModelGetRequest request) {
        Long id = request.getId();

        Long rootId = request.getRootId();
        DataModel rootPersistence = dataModelRepository.selectById(rootId);
        if(rootPersistence == null){
            throw new AppException("root不存在");
        }

        DataModel dataModelPersistence = dataModelRepository.selectById(id, rootPersistence);
        if(dataModelPersistence == null) {
            throw new AppException("数据模型不存在");
        }
        DataModel dataModel = new DataModel();
        dataModel.setId(dataModelPersistence.getId());
        dataModel.setName(dataModelPersistence.getName());
        dataModel.setProperties(dataModelPersistence.getProperties());
        dataModel.setAssociation(dataModelPersistence.getAssociation());
        dataModel.setIsExpand(dataModelPersistence.getIsExpand());
        dataModel.setDynamicModel(dataModelPersistence.getDynamicModel());
        dataModel.setGeneratorInstance(dataModelPersistence.getGeneratorInstance());
        dataModel.setParent(dataModelPersistence.getParent());
        User userPersistence = userRepository.selectById(dataModelPersistence.getUser().getId());
        dataModel.setUser(userPersistence);
        return dataModel;
    }

    @Override
    public DataModel update(DataModelUpdateRequest request) {
        Long id = request.getId();

        Long rootId = request.getRootId();
        DataModel rootPersistence = dataModelRepository.selectById(rootId);
        if(rootPersistence == null){
            throw new AppException("root不存在");
        }

        Long userId = request.getAuthentication().getUserId();
        if(!userId.equals(rootPersistence.getUser().getId())){
            throw new AppException("权限不足");
        }

        GeneratorInstance generatorInstancePersistence = generatorInstanceRepository.selectById(rootPersistence.getGeneratorInstance().getId());
        if(generatorInstancePersistence == null){
            throw new AppException("生成器实例不存在");
        }

        DataModel dataModelPersistence = dataModelRepository.selectById(id, rootPersistence);
        if(dataModelPersistence == null) {
            throw new AppException("数据模型不存在");
        }
        dataModelPersistence.setIsExpand(request.getIsExpand());
        dataModelPersistence.setName(request.getName());

        dataModelPersistence.setProperties(request.getProperties());
        dataModelPersistence.setAssociation(request.getAssociation());
        dataModelRepository.update(dataModelPersistence);
        return dataModelPersistence;
    }

    @Override
    public DataModel tree(DataModelTreeRequest request) {
        Long rootId = request.getRootId();
        DataModel rootPersistence = dataModelRepository.selectById(rootId);
        if(rootPersistence == null){
            throw new AppException("root不存在");
        }

        DataModel root = new DataModel();
        root.setId(rootPersistence.getId());
        root.setName(rootPersistence.getName());
        root.setIsExpand(rootPersistence.getIsExpand());
        root.setDynamicModel(rootPersistence.getDynamicModel());

        LinkedList<DataModel> sourceStack = new LinkedList<>();
        sourceStack.push(rootPersistence);
        LinkedList<DataModel> targetStack = new LinkedList<>();
        targetStack.push(root);
        while (!sourceStack.isEmpty()) {
            DataModel sourceDataModel = sourceStack.pop();
            DataModel targetDataModel = targetStack.pop();
            sourceDataModel.getChildren().forEach(dataModel -> {
                sourceStack.push(dataModel);
                DataModel newDataModel = new DataModel();
                newDataModel.setId(dataModel.getId());
                newDataModel.setName(dataModel.getName());
                newDataModel.setIsExpand(dataModel.getIsExpand());
                newDataModel.setDynamicModel(dataModel.getDynamicModel());
                newDataModel.setUser(dataModel.getUser());
                targetDataModel.getChildren().add(newDataModel);
                targetStack.push(newDataModel);
            });
        }
        return root;
    }

    @Override
    public void sort(DataModelSortRequest request) {

    }

}