# Face

Quickly expose your database information through the API via simple JSON configuration files.

## To start

To get started you will need to know how to use JSON files and know the SQL language...

### Prerequisites

- Java 8
- Sql Server or Oracle Sql or MySql or PostgreSQL

### Installation

_Windows_:
[The steps to install program....](https://docs.microsoft.com/en-us/dotnet/framework/windows-services/how-to-install-and-uninstall-services)

### Structure
```bash
├── config                                   # main configuration folder
│   │── datasource                           # datasource configuration folder
│   │   ├── ...                              
│   │   └── ...
│   ├── security                             # security configuration folder
│   │   ├── ...                              
│   │   └── ...
│   ├── ...                                  
│   └── ...
└── face.jar                                 # Java Archive

Test with GET localhost:9100/api/get-user-data-example?gender=M
```
### Demo

```bash

# Structure

├── config                                   
│   │── datasource                           
│   │   ├── user-datasource-example.json     # example configuration for datasource 
│   │   └── ...
│   ├── security                             
│   │   ├── user-security-example.json       # example configuration for security 
│   │   └── ...
│   ├── get-user-data-example.json           # example configuration for API
│   └── ...
└── face.jar        

# user-datasource-example.json 
{
    "type": "TSQL",                          # MYSQL (MySql), TSQL (Sql Server), PLSQL (Oracle Sql), POSTGRES (PostgreSQL)
    "server": "IP_ADRESSS",
    "port": null,
    "database": "DB_NAME",
    "user": "USER_NAME",
    "password": "MY_PASSWORD"
}

# user-security-example.json 
{
    "Authorization": "MY_AUTH_CODE"
}

# get-user-data-example.json
{
    "datasource": "user-datasource-example",
    
    "query": {
        "select": "firstname, lastname, birthday, gender",
        "from": "users",
        "where": "gender = ':gender'",
        "orderby": "id"
    },
    
   "security": "user-security-example"
}

```

![Creation of the database and insertion of data](https://github.com/chrispindjako/face/blob/main/demo/Capture-0.PNG?raw=true)

![Test 1](https://github.com/chrispindjako/face/blob/main/demo/Capture-1.PNG?raw=true)

![Test 2](https://github.com/chrispindjako/face/blob/main/demo/Capture-2.PNG?raw=true)

## Made with

* [Spring Boot](https://spring.io/projects/spring-boot) - Spring Boot is an open source Java-based framework used to create a micro Service.
* [Spring Tools](https://spring.io/tools) - Spring Tools is the next generation of Spring tooling for your favorite coding environment

## Contributing

If you want to contribute, read the file [CONTRIBUTING.md](https://github.com/chrispindjako/face) to know how to do it.

## Versions

**Last release version :** 1.0
**Last version :** 1.0
Liste des versions : [Click to view](https://github.com/chrispindjako/face/tags)
## Auteurs

* **Chrispin Djako** _alias_ [@chrispindjako](https://github.com/chrispindjako)

Read the list of [contributors](https://github.com/chrispindjako/face/contributors) to see who helped with the project!

## License

This project is licensed ``Apache License 2.0`` - view file [LICENSE](https://github.com/chrispindjako/face/blob/main/LICENSE) for more information

