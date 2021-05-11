package com.pnode.crm.workbench.service;

import com.pnode.crm.vo.PaginationVO;
import com.pnode.crm.workbench.domain.Activity;
import com.pnode.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity a);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity a);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean updateRemark(ActivityRemark ar);

    boolean saveRemark(ActivityRemark ar);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByName(String aname);
}
