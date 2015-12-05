package core.report;

import helper.activation.ByteArrayDataSource;
import models.User;

/**
 * This is the business interface for ReportManager enterprise bean.
 */
public interface ReportManager {

    ByteArrayDataSource getDayOverDayReport(ReportOutputType outputType, User user);
}
