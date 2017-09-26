package com.dian.dao.sys;

import java.util.List;

import com.dian.model.sys.Attachment;

import core.dao.Dao;


public interface AttachmentDao extends Dao<Attachment> {

	List<Object[]> queryFlowerList(String epcId);

	void deleteAttachmentByForestryTypeId(Long forestryTypeId);

}
