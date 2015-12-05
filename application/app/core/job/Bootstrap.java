package core.job;

import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Bootstrap extends Job {

    @Override
    public void doJob() {
        redirectJuliLogging();
    }

    private void redirectJuliLogging() {
        // startup code here
        Logger.info("doJob");
        Logger.redirectJuli = true;
        Logger.setUp("DEBUG");
    }
}
