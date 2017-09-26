package com.dian.service.sys;

import java.util.List;

import com.dian.model.sys.Attachment;

import core.service.Service;

public interface AttachmentService extends Service<Attachment> {

	List<Object[]> queryFlowerList(String epcId);

	void deleteAttachmentByForestryTypeId(Long forestryTypeId);

}
