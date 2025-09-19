spring:
  datasource:
    url: jdbc:h2:mem:demo;DB_CLOSE_DELAY=-1
    driverClassName: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

dynamia:
  app:
    name: __APP_NAME__
    short-name: __APP_NAME__
    default-skin: Green

server:
  port: 8080
