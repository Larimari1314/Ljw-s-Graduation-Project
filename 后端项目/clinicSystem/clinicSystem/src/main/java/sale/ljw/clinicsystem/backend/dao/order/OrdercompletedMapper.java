package sale.ljw.clinicsystem.backend.dao.order;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;
import sale.ljw.clinicsystem.backend.pojo.order.Ordercompleted;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86155
* @description 针对表【orderCompleted】的数据库操作Mapper
* @createDate 2022-08-07 16:10:49
* @Entity sale.ljw.clinicsystem.backend.pojo.order.Ordercompleted
*/
@Mapper
@Repository
public interface OrdercompletedMapper extends BaseMapper<Ordercompleted> {

}




