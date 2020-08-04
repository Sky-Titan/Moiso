drop schema MOISO_server;
CREATE SCHEMA `MOISO_server`;
use MOISO_server;
create table userTbl(									/*사용자 관리 테이블*/
	user_id VARCHAR(16) NOT NULL,						
    user_pw VARCHAR(35) NOT NULL,
    user_email VARCHAR(320) NOT NULL,
    user_nickname VARCHAR(10) NOT NULL,
    PRIMARY KEY (user_id)
);
create table groupTbl(									/*그룹 관리 테이블*/
	group_id int(10) NOT NULL AUTO_INCREMENT,			
    group_name VARCHAR(30) NOT NULL,
    group_ip INT UNSIGNED NOT NULL,
    group_leader VARCHAR(16) NOT NULL,					
	PRIMARY KEY(group_id),						
    FOREIGN KEY(group_leader) REFERENCES userTbl(user_id)
);

CREATE TABLE mettingTbl(								/*회의 관리 테이블*/
	metting_id INT(10) NOT NULL AUTO_INCREMENT,
    metting_start DATETIME DEFAULT NOW(),
    metting_end DATETIME,
    group_id INT(10) NOT NULL,
    PRIMARY KEY(metting_id),
    FOREIGN KEY(group_id) REFERENCES groupTbl(group_id)
);

CREATE TABLE group_memberTbl(							/*그룹 맴버 관리 테이블*/
	group_id INT(10) NOT NULL,
    member_id VARCHAR(16) NOT NULL,
    FOREIGN KEY(group_id) REFERENCES groupTbl(group_id),
    FOREIGN KEY(member_id) REFERENCES userTbl(user_id)    
);

CREATE TABLE metting_memberTbl(							/*회의 맴버 관리 테이블*/
	metting_id INT(10) NOT NULL,
    member_id VARCHAR(16) NOT NULL,
    FOREIGN KEY(metting_id) REFERENCES mettingTbl(metting_id),
    FOREIGN KEY(member_id) REFERENCES group_memberTbl(member_id)    
);