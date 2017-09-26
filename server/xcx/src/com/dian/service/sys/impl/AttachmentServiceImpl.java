package com.dian.service.sys.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dian.dao.sys.AttachmentDao;
import com.dian.model.sys.Attachment;
import com.dian.service.sys.AttachmentService;

import core.service.BaseService;


@Service
public class AttachmentServiceImpl extends BaseService<Attachment> implements AttachmentService {

	private AttachmentDao attachmentDao;

	@Resource
	public void setAttachmentDao(AttachmentDao attachmentDao) {
		this.attachmentDao = attachmentDao;
		this.dao = attachmentDao;
	}

	@Override
	public List<Object[]> queryFlowerList(String epcId) {
		return attachmentDao.queryFlowerList(epcId);
	}

	@Override
	public void deleteAttachmentByForestryTypeId(Long forestryTypeId) {
		attachmentDao.deleteAttachmentByForestryTypeId(forestryTypeId);
	}

}
