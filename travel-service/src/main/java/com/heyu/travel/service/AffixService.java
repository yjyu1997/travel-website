package com.heyu.travel.service;

import com.heyu.travel.req.AffixVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Description 文件上传业务
 */
public interface AffixService {
    /**
     * @Description 文件上传
     * @param multipartFile 上传对象
     * @param affixVo 附件对象
     * @return AffixVo
     * @throws IOException io异常抛出
     */
    AffixVo upLoad(MultipartFile multipartFile,
                   AffixVo affixVo) throws IOException;

    /**
     * @Description 为上传绑定对应的业务Id
     * @param  affixVo 附件对象
     * @return 是否成功绑定
     */
    Boolean bindBusinessId(AffixVo affixVo);

    /**
     * @Description 按业务ID查询附件
     * @param  affixVo 附件对象
     * @return 符合条件AffixVo列表
     */
    List<AffixVo> findAffixByBusinessId(AffixVo affixVo);
}
