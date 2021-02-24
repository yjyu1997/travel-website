package com.heyu.travel.service.impl;

import com.heyu.travel.config.SnowflakeIdWorker;
import com.heyu.travel.constant.RedisConstant;
import com.heyu.travel.mapper.AffixMapper;
import com.heyu.travel.pojo.Affix;
import com.heyu.travel.pojo.AffixExample;
import com.heyu.travel.req.AffixVo;
import com.heyu.travel.service.AffixService;
import com.heyu.travel.service.RedisCacheService;
import com.heyu.travel.utils.BeanConv;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


/**
 * @Description 文件上传服务实现
 */
@Log4j2
@Service
public class AffixServiceImpl implements AffixService {
    @Autowired
    AffixMapper affixMapper;

    @Autowired
    RedisCacheService redisCacheService;

    @Autowired
    SnowflakeIdWorker snowflakeIdWorker;

    @Value("upLoad.pathRoot")
    String pathRoot;

    @Value("upLoad.webSite")
    String webSite;


   /**
     * @param multipartFile 上传对象
     * @param affixVo       附件对象
     * @return AffixVo
     * @throws IOException io异常抛出
     * @Description 文件上传
     */
    @Override
    public AffixVo upLoad(MultipartFile multipartFile, AffixVo affixVo) throws IOException {
        //判断文件是否为空
        if(multipartFile == null){
            return null;
        }
        String businessType = affixVo.getBusinessType();
        //关联业务
        affixVo.setBusinessType(businessType);
        //原始上传的文件名称aaa.jpg
        String originalFilename = multipartFile.getOriginalFilename();
        //后缀名.jpg
        String suffix = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf('.'));
        affixVo.setSuffix(suffix);
        //文件名称
        String fileName = String.valueOf(snowflakeIdWorker.nextId());
        affixVo.setFileName(fileName);
        //构建访问路径
        String pathUrl = businessType + "/" +fileName+ suffix;
        //判断业务类型文件夹是否存在
        File file = new File(pathRoot+businessType);
        if(!file.exists()){
            file.mkdirs();
        }
        file = new File(pathRoot+pathUrl);
        multipartFile.transferTo(file);

        pathUrl = webSite + pathUrl;
        affixVo.setPathUrl(pathUrl);
        affixMapper.insert(BeanConv.toBean(affixVo, Affix.class));
        return affixVo;
    }

    /**
     * @param affixVo 附件对象
     * @return 是否成功绑定
     * @Description 为上传绑定对应的业务Id
     */
    @Override
    public Boolean bindBusinessId(AffixVo affixVo) {
        Affix affix = BeanConv.toBean(affixVo, Affix.class);
        //延时双删
        String key = RedisConstant.AFFIXSERVICE_FINDAFFIXBYBUSINESSID;
        redisCacheService.deleListCache(key + affixVo.getBusinessId());
        int rows = affixMapper.updateByPrimaryKeySelective(affix);
        boolean flag = rows > 0;
        if (flag) {
            try{
                Thread.sleep(500);
                redisCacheService.deleListCache(key + affixVo.getBusinessId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * @param affixVo 附件对象
     * @return 符合条件AffixVo列表
     * @Description 按业务ID查询附件
     */
    @Override
    public List<AffixVo> findAffixByBusinessId(AffixVo affixVo) {
        String key = RedisConstant.AFFIXSERVICE_FINDAFFIXBYBUSINESSID;
        return redisCacheService.listCache(() -> {
            AffixExample affixExample = new AffixExample();
            affixExample.createCriteria().andBusinessIdEqualTo(affixVo.getBusinessId());
            List<Affix> affixes = affixMapper.selectByExample(affixExample);
            return BeanConv.toBeanList(affixes, AffixVo.class);
        }, key+affixVo.getBusinessId());
    }
}
