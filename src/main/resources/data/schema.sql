CREATE TABLE TOOL_INFO (
                           ID BINARY(16) DEFAULT NOT NULL PRIMARY KEY,
                           TOOL_CODE VARCHAR2(10) UNIQUE,
                           TOOL_TYPE VARCHAR2(20),
                           BRAND VARCHAR2(20));

CREATE TABLE CHARGE_RULE (
                             ID UUID DEFAULT NOT NULL PRIMARY KEY,
                             TOOL_TYPE VARCHAR(20) UNIQUE,
                             DAILY_CHARGE DECIMAL(10,2),
                             WEEKDAY_CHARGE BOOLEAN,
                             WEEKEND_CHARGE BOOLEAN,
                             HOLIDAY_CHARGE BOOLEAN);