/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.stylefeng.guns.modular.system.dao;

import cn.stylefeng.guns.core.common.node.ZTreeNode;
import cn.stylefeng.guns.modular.system.model.Dept;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2017-07-11
 */
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree(@Param("deptId") Integer deptId);

    /**
     * 获取所有部门列表
     */
    List<Map<String, Object>> list(@Param("condition") String condition,
                                   @Param("deptId") Integer deptId);

    /**
     * 根据工号获取所属部门
     *
     * @param userId 工号
     * @return
     */
    Dept getDeptByUserId(@Param("userId") String userId);

    int updateChildAttendParam(@Param("deptId") String deptId,
                               @Param("attendTimes") Integer attendTimes,
                               @Param("startWorkTime") String startWorkTime,
                               @Param("endWorkTime") String endWorkTime,
                               @Param("startRestTime") String startRestTime,
                               @Param("endRestTime") String endRestTime,
                               @Param("startOverTime") String startOverTime,
                               @Param("leaveTime") Integer leaveTime,
                               @Param("email1") String email1,
                               @Param("email2") String email2,
                               @Param("email3") String email3,
                               @Param("sendEmailCycle") String sendEmailCycle,
                               @Param("workDay") String workDay);

    List<Dept> getDeptBySendEmailCycle(@Param("cycle") String cycle);
}