package core.event;

import core.medicaldevice.MedicalDevice;
import core.datapoint.DataPoint;
import core.module.NetworkType;
import core.feature.userfeatureprofile.UserRiskAlertProfile;
import core.feature.IntensiveManagementProtocol;
import core.feature.RiskAlertProfile;
import core.type.ComparisonType;
import core.module.Module;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.*;
import play.test.*;
import models.*;

public class BloodGlucoseNotificationListenerTest extends UnitTest {

    @Test
    public void testBgn() {
        Fixtures.deleteAll();
        BloodGlucoseNotificationListener bgnl = new BloodGlucoseNotificationListener();
        User u = new User();
        u.setHandle("handle");
        u.setLogin("login");
        u.setType(UserType.PARTICIPANT);
        u = u.save();
        MedicalDevice md = new MedicalDevice();
        md.setUser(u);
        md.setSerialNumber("SN");
        md = md.save();
        Module module = new Module();
        module.setPin("pin");
        module.setNetworkType(NetworkType.MediSIM);
        module = module.save();
        DataPoint dp = new DataPoint();
        dp.setTimestamp(new Date());
        dp.setValue(100);
        dp.setUser(u);
        dp.setMedicalDevice(md);
        dp.setModule(module);
        dp.setOriginated(new Date());
        dp = dp.save();

        IntensiveManagementProtocol imp = new IntensiveManagementProtocol();
        imp.setName("imp");
        imp = imp.save();
        RiskAlertProfile fp = new RiskAlertProfile();
        fp.setComparisonType(ComparisonType.GREATER_THAN);
        fp.setComparisonValue(BigDecimal.ZERO);
        fp.setResult(BigDecimal.valueOf(1));
        fp.setHours(48);
        fp.setName("fp");
        fp.setIntensiveManagementProtocol(imp);
        fp = fp.save();
        UserRiskAlertProfile urap = new UserRiskAlertProfile();
        urap.setFeatureProfile(fp);
        urap.setUser(u);
        urap = urap.save();
        bgnl.onNewDataPoint(dp);
    }
}
