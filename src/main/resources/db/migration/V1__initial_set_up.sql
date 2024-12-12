CREATE TABLE IF NOT EXISTS FLOWCHART (
ID INT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
NAME VARCHAR(255) NOT NULL COMMENT 'Flowchart Name',
IS_ACTIVE BOOLEAN DEFAULT TRUE NOT NULL COMMENT 'Flag for deletion',
CREATED_ON DATETIME NOT NULL COMMENT 'Audit field for created date time',
UPDATED_ON DATETIME DEFAULT NULL COMMENT 'Audit field for updated date time',
PRIMARY KEY (ID));

CREATE TABLE IF NOT EXISTS NODE (
ID INT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
FLOWCHART_ID INT NOT NULL COMMENT 'Flowchart Id',
NODE_VALUE VARCHAR(255) NOT NULL COMMENT 'Flowchart value',
TYPE VARCHAR(50) NOT NULL COMMENT 'Node type',
CREATED_ON DATETIME NOT NULL COMMENT 'Audit field for created date time',
UPDATED_ON DATETIME DEFAULT NULL COMMENT 'Audit field for updated date time',
PRIMARY KEY (ID));

CREATE TABLE IF NOT EXISTS EDGE (
ID INT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
FLOWCHART_ID INT NOT NULL COMMENT 'Flowchart Id',
SOURCE_NODE_ID INT NOT NULL COMMENT 'Source node id',
TARGET_NODE_ID INT NOT NULL COMMENT 'Target node id',
CREATED_ON DATETIME NOT NULL COMMENT 'Audit field for created date time',
UPDATED_ON DATETIME DEFAULT NULL COMMENT 'Audit field for updated date time',
PRIMARY KEY (ID));