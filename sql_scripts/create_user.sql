/* Creating a user with name spring_app */
DROP USER IF EXISTS `spring_app`@`localhost`;
/* Identified by ..... to set password */
CREATE USER `spring_app`@`localhost` IDENTIFIED BY 'spring_app';
GRANT ALL PRIVILEGES ON  `spring_app`.* TO `spring_app`@`localhost`;