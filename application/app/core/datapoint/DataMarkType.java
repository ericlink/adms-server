package core.datapoint;
/*
To indicate the presence or lack of Ketones, Press 1
To indicate Small, Press 1
To indicate Medium, Press 2
To indicate Large, Press 3
To indicate No Ketones, Press 4 (0)
To inidate a Sick Day, Press 2
To indicate Suspected Onset, Press 1
To indicate Medium-type Sick Day, Press 2
To indicate Very sick including loss of fluids, Press 3
To indicate that the patient is feeling better, Press 4
To indicate Exercise, Press 3
To indicate Low, Press 1
To indicate Medium, Press 2
To indicate High, Press 3
To indicate an injection or bolus of Short Acting Insulin, Press 4
Enter a number between 0.00 and 999.00 to record the short acting insulin given, and press the # key
To indicate an injection of Long Acting Insulin, Press 5
Enter a number between 0.00 and 999.00 to record the long acting insulin given, and press the # key
To indicate or update the current total daily basal insulin, Press 6.  Entering a value here will mark a change in the basal profile.
Enter a number between 0.00 and 999.00 to record the total daily basal insulin given, and press the # key.
To indicate that the insulin pump infusion site has been changed, Press 7
To indicate  intake of Carbohydrates, Press 8
Enter a number between 0 and 999 to record the amount of carbohydrates associated with this timeframe, and press the # key.
Enter 00 to indicate the withholding of normally  scheduled carbohydrates, and press the # key
To indicate a suspected influence on the blood sugar level, Press 9
To indicate excitement, Press 1
To indicate tiredness, Press 2
To indicate missed insulin, Press 3
To indicate too much insulin, Press 4
To indicate excessive heatPress 5
To indicate travel, Press 6
To indicate other medications, Press 7
To indicate unusual changes to your lifestyle or daily schedule, Press 8
To indicate Errors on your meter or problems with your test strips, Press 9
 */

public enum DataMarkType {

    KEYTONE, //  1;
    SICK_DAY, //  2;
    EXERCISE, //  3;
    SHORT_ACTING_INSULIN, //  4;
    LONG_ACTING_INSULIN, //  5;
    TOTAL_DAILY_BASAL_INSULIN, //  6;
    INSULIN_PUMP_INFUSION_SITE_CHANGED, //  7;
    CARBOHYDRATES_INTAKE_CHANGED, //  8;
    INFLUENCE                              //  9;
}
