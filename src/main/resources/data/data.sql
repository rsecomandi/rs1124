CREATE TABLE TOOL_INFO (
                           ID BINARY(16) DEFAULT NOT NULL PRIMARY KEY,
                           TOOL_CODE VARCHAR2(10) UNIQUE,
                           TOOL_TYPE VARCHAR2(20),
                           BRAND VARCHAR2(20));

INSERT INTO TOOL_INFO VALUES (UUID(),  'CHNS', 'Chainsaw', 'Stihl');
INSERT INTO TOOL_INFO VALUES (UUID(), 'LADW', 'Ladder', 'Weber');
INSERT INTO TOOL_INFO VALUES (UUID(), 'JAKD', 'Jackhammer', 'DeWalt');
INSERT INTO TOOL_INFO VALUES (UUID(), 'JAKR', 'Jackhammer', 'Ridgid');

CREATE TABLE CHARGE_RULE (
                             ID UUID DEFAULT NOT NULL PRIMARY KEY,
                             TOOL_TYPE VARCHAR(20) UNIQUE,
                             DAILY_CHARGE DECIMAL(10,2),
                             WEEKDAY_CHARGE BOOLEAN,
                             WEEKEND_CHARGE BOOLEAN,
                             HOLIDAY_CHARGE BOOLEAN);

INSERT INTO CHARGE_RULE VALUES (UUID(), 'Ladder', '1.99', TRUE, TRUE, FALSE);
INSERT INTO CHARGE_RULE VALUES (UUID(), 'Chainsaw', '1.49', TRUE, FALSE, TRUE);
INSERT INTO CHARGE_RULE VALUES (UUID(), 'Jackhammer', '2.99', TRUE, FALSE, FALSE);
