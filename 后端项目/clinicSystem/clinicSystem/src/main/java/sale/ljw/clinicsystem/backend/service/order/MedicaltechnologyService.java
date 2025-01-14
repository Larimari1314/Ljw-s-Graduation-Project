package sale.ljw.clinicsystem.backend.service.order;

import org.springframework.web.multipart.MultipartFile;
import sale.ljw.clinicsystem.backend.form.basic.admin.EditMedicalTechnologyByAdmin;
import sale.ljw.clinicsystem.backend.form.basic.admin.FindAllMedicalTechnologyByAdmin;
import sale.ljw.clinicsystem.backend.form.basic.admin.FindMedicalTechnologyOrderByAdmin;
import sale.ljw.clinicsystem.backend.form.basic.patient.FindMedicalTechnologyListByPatient;
import sale.ljw.clinicsystem.backend.pojo.order.Medicaltechnology;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 86155
* @description 针对表【medicalTechnology】的数据库操作Service
* @createDate 2022-09-21 07:13:48
*/
public interface MedicaltechnologyService extends IService<Medicaltechnology> {

    String getMedicalProjectNameList();

    String findMedicalTechnologyList(FindMedicalTechnologyListByPatient medicalTechnology);

    String findDoctorDutyTimeList(String doctorId);

    String findAllMedicalTechnology(FindAllMedicalTechnologyByAdmin medicalTechnology);

    String editMedicalTechnology(EditMedicalTechnologyByAdmin medicalTechnology, MultipartFile multipartFile);

    String findMedicalTechnologyOrder(FindMedicalTechnologyOrderByAdmin medicalTechnologyOrder);
}
