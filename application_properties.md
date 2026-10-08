spring.application.name=collaborative-editor
server.port=8080

# Database Settings (MySQL Configuration)
spring.datasource.url=jdbc:mysql://localhost:3306/collab_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root

# Hibernate ORM Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect