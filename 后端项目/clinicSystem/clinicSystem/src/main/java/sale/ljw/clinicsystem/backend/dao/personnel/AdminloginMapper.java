package sale.ljw.clinicsystem.backend.dao.personnel;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;
import sale.ljw.clinicsystem.backend.pojo.personnel.Adminlogin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86155
* @description 针对表【adminLogin】的数据库操作Mapper
* @createDate 2022-08-07 16:00:14
* @Entity sale.ljw.clinicsystem.backend.pojo.personnel.Adminlogin
*/
@Mapper
@Repository
public interface AdminloginMapper extends BaseMapper<Adminlogin> {

}




